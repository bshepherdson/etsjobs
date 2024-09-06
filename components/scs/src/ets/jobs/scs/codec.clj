(ns ets.jobs.scs.codec
  (:refer-clojure :exclude [file-seq slurp])
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]
   [gloss.core :as g]
   [gloss.core.codecs :as gc]
   [gloss.core.structure :as gs]
   [gloss.io :as gio])
  (:import
   [java.nio ByteBuffer ByteOrder]
   [java.nio.channels FileChannel$MapMode]
   [java.util.zip Inflater]
   ets.jobs.scs.CityHash))

(defn- check [frame test]
  (g/header
    frame
    (fn [x]
      (test x) ;; Throws if there's a problem.
      ;; We've already ready the value.
      g/nil-frame)
    (constantly g/nil-frame)))

(g/defcodec hash-method
  (-> (g/string :utf-8 :length 4)
      (check (fn [hash-fn]
               (when (not= hash-fn "CITY")
                 (throw (ex-info "Unknown hash function" {:hash-fn hash-fn})))))))

(g/defcodec platform
  (gc/enum :ubyte :pc :xbox/one :xbox/series :ps4 :ps5))

(g/defcodec header-v2
  (gc/ordered-map
    :salt                       :uint16-le    ; 0
    :hash-method                hash-method   ; CITY
    :entry-count                :uint32-le    ; 0x0366     = 870
    :entry-table-len            :uint32-le    ; 0x2541     = 9537
    :metadata-count             :uint32-le    ; 0x112b     = 4395
    :metadata-table-len         :uint32-le    ; 0x1c0f     = 7183
    :entry-table-start          :uint64-le    ; 0x014e4140 = 21905728
    :metadata-table-start       :uint64-le    ; 0x014e6690 = 21915280
    :security-descriptor-offset :uint32-le    ; 0x80       = 128
    :platform                   platform))

(g/defcodec preamble
  (gc/ordered-map :magic   (g/string :utf-8 :length 4)
                  :version :uint16-le))

(defn- mmap [path-or-file]
  (let [chan (-> (io/file path-or-file)
                 (java.io.FileInputStream.)
                 (.getChannel))]
    (-> (.map chan FileChannel$MapMode/READ_ONLY 0 (.size chan))
        (.order ByteOrder/LITTLE_ENDIAN))))

(defn- slice [^ByteBuffer buf ^long pos ^long len]
  (.limit buf (+ pos len))
  (.position buf pos)
  buf)

(defn- unzip
  ([^ByteBuffer buf]
   (let [inflater (doto (Inflater. false)
                    (.setInput buf))
         zip-len  (.remaining buf)
         out      (ByteBuffer/allocate (* 16 zip-len))]
     (.inflate inflater out)
     (.flip out)
     out))
  ([^ByteBuffer buf ^long pos ^long len]
   (unzip (slice buf pos len))))

(g/defcodec entry-table-entry-v2
  (gc/ordered-map
    :hash       :int64-le
    :meta-index :uint32-le
    :meta-count :uint16-le
    :flags      :uint16-le))

(defn- read-entry-table-v2
  [^ByteBuffer buf {:keys [entry-table-start entry-table-len]}]
  (let [table (unzip buf entry-table-start entry-table-len)]
    (->> table
         (gio/decode (g/repeated entry-table-entry-v2 :prefix :none))
         (sort-by :meta-index))))

(g/defcodec metadata-entry-type
  (gc/enum :ubyte
           {:image            1   
            :sample           2   
            :mip-proxy        3   
            :inline-directory 4   
            :plain            128 
            :directory        129 
            :mip0             130 
            :mip1             131 
            :mip-tail         132}))

(defn- no-writing [_]
  (throw (ex-info "writing is not supported" {})))

(def ^:private uint24
  (gs/compile-frame
    [:ubyte :ubyte :ubyte]
    no-writing
    (fn [[b0 b1 b2]]
      (->> b0
           (bit-or (bit-shift-left b1 8))
           (bit-or (bit-shift-left b2 16))))))

(defmulti ^:private metadata-entry-frame identity)

(defn- metadata-entry-frame-basic [type]
  (gs/compile-frame
    (gc/ordered-map
      :compressed-bytes uint24
      :flags            :ubyte
      :size             :uint32-le
      :unknown2         :uint32-le
      :offset-block     :uint32-le)
    #(dissoc % :type)
    #(assoc % :type type)))

(defmethod metadata-entry-frame :default [type]
  (throw (ex-info "Unknown metadata entry type" {:type type})))

(defmethod metadata-entry-frame :plain [type]
  (metadata-entry-frame-basic type))

(defmethod metadata-entry-frame :directory [type]
  (metadata-entry-frame-basic type))

(defmethod metadata-entry-frame :image [_]
  (gc/ordered-map
    :unknown1         :uint64-le
    :texture/width    (gs/compile-frame :uint16-le dec inc)
    :texture/height   (gs/compile-frame :uint16-le dec inc)
    :flags/image      :uint32-le
    :flags/sample     :uint32-le
    :compressed-bytes uint24
    :flags            :ubyte
    :unknown3         (g/finite-block 8)
    :offset-block     :uint32-le))

(g/defcodec metadata-entry-v2
  (g/header
    [uint24 metadata-entry-type]
    (fn [[index type]]
      (gs/compile-frame (metadata-entry-frame type)
                        no-writing
                        #(assoc % :index index)))
    no-writing))

(defn- read-metadata-table-v2
  [^ByteBuffer buf {:keys [metadata-table-start metadata-table-len]}]
  (let [table (unzip buf metadata-table-start metadata-table-len)]
    (gio/decode (g/repeated metadata-entry-v2 :prefix :none)
                table)))

(def ^:private block-size 16)

(defn- compressed? [entry]
  (-> entry
      :meta
      :flags
      (bit-and 0x10)
      pos?))

(defn- entry-content [scs {metadata :meta :as entry}]
  (let [pos (* block-size (:offset-block metadata))] 
    (if (compressed? entry)
      ;; TODO: Include the size, for efficient unzipping.
      (unzip (:buf scs) pos (:compressed-bytes metadata))
      (slice (:buf scs) pos (:size metadata)))))

;; A U32 length, that many :ubytes, then length strings of the given lengths!
;; We use a nested `g/header` to express that combo.
(g/defcodec directory-codec-v2
  (g/header
    :uint32-le
    (fn [n-strs]
      (g/header
        (gs/compile-frame (vec (repeat n-strs :ubyte)))
        (fn [lengths]
          (gs/compile-frame
            (vec (for [len lengths]
                   (g/string :utf-8 :length len)))))
        no-writing))
    no-writing))

(defn- directory-v2 [scs entry]
  (let [content (entry-content scs entry)
        paths   (gio/decode directory-codec-v2 content)
        {dirs  true
         files false} (group-by #(= (first %) \/) paths)]
    {:subdirs (mapv #(subs % 1) dirs)
     :files   files}))

(defn- remove-trailing-slash [path]
  (if (str/ends-with? path "/")
    (subs path 0 (dec (count path)))
    path))

(defn- remove-leading-slash [path]
  (if (str/starts-with? path "/")
    (subs path 1)
    path))

(defn- hash-path [scs path]
  (let [salt   (-> scs :header :salt)
        path   (remove-leading-slash path)
        salted (if (zero? salt)
                 path
                 (str salt path))]
    (CityHash/cityHash64 (.getBytes salted) 0 (count salted))))

(defn- path->entry [scs path]
  (let [path   (remove-trailing-slash path)
        hashed (hash-path scs path)]
    (get (:entry-table scs) hashed)))

(defn directory-listing [scs path]
  (when-let [entry (path->entry scs path)]
    (directory-v2 scs entry)))

(defn file-seq
  ([scs] (file-seq scs ""))
  ([scs path]
   (let [{:keys [files subdirs]} (directory-listing scs path)]
     (lazy-cat (for [f (sort files)]
                 (str path "/" f))
               (mapcat #(file-seq scs (str path "/" %)) (sort subdirs))))))

(defn- ->string [^ByteBuffer buf]
  (let [arr (byte-array (.remaining buf))]
    (.get buf arr)
    (String. arr)))

(defn file-contents
  "Returns a `ByteBuffer` for the file's contents.
  
  For a string, call `slurp`."
  [scs path]
  (some->> (path->entry scs path)
           (entry-content scs)))

(defn slurp [scs path]
  (some-> (file-contents scs path)
          ->string))

(defn- index-by [f seqable]
  (into {} (map (juxt f identity)) seqable))

(defn scs-file
  "Map in and index an SCS file, by its path or `File`."
  [path-or-file]
  (let [buf                     (mmap path-or-file)
        {:keys [magic version]} (gio/decode preamble (slice buf 0 6))
        _ (when (not= magic "SCS#")
            (throw (ex-info "Unknown magic number - not an SCS file"
                            {:magic magic})))
        _ (when (not= version 2)
            (throw (ex-info "Only SCS version 2 files are supported now"
                            {:version version})))
        header   (gio/decode header-v2 (slice buf 6 43))
        entries  (read-entry-table-v2 buf header)
        metadata (index-by :index (read-metadata-table-v2 buf header))]
    {:buf            buf 
     :header         header
     :metadata-table metadata
     :entry-table    (->> (for [{:keys [hash meta-count meta-index] :as entry} entries
                                :let [meta_ (get metadata (+ meta-index meta-count))]]
                            [hash (assoc entry :meta meta_)])
                          (into {}))}))
