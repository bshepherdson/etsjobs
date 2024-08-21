(ns ets.jobs.sii.binary
  "Functions for decoding the SII binary file format."
  (:require
   [camel-snake-kebab.core :as csk]))

(defn- sr-string
  "32-bit length, then UTF-8 encoded, unterminated string."
  [buf]
  (let [len (.getInt buf)
        bs  (byte-array len)]
    (.get buf bs)
    (String. bs)))

(defn- encoded-char [c]
  (get " 0123456789abcdefghijklmnopqrstuvwxyz_" c))

(defn- sr-encoded-string
  "Single 64-bit number. Ignore top bit, then it's a base-38 value."
  [buf]
  (loop [x  (bit-and 0x7fffffffffffffff (.getLong buf))
         cs []]
    (let [c (mod x 38)]
      (if (zero? c)
        (apply str cs)
        (recur (quot x 38) (conj cs (encoded-char c)))))))

(defn- sr-array-of [f]
  (fn [buf]
    (let [len (.getInt buf)]
      (mapv (fn [_] (f buf)) (range len)))))

(defn- sr-float [buf]
  (.getFloat buf))

(defn- sr-vec [n f]
  (fn [buf]
    (mapv (fn [_] (f buf)) (range n))))

(defn- sr-int [buf]
  (.getInt buf))

(defn- sr-vec8s-biased
  "7-element float vector, with a bonus 'bias' shift."
  [buf]
  (let [raw ((sr-vec 8 sr-float) buf)]
    ::biased-vec8s-are-not-supported
    #_(vec (concat (take 3 raw) (drop 4 raw)))))

(defn- sr-uint [buf]
  (sr-int buf))

(defn- sr-uint16 [buf]
  (long (.getShort buf)))

(defn- sr-int64 [buf]
  (.getLong buf))
(defn- sr-uint64 [buf]
  (sr-int64 buf))

(defn- sr-byte-bool [buf]
  (not= 0 (.get buf)))

(defn- sr-ordinal-string [buf]
  [::ordinal (sr-int buf)])

(defn- sr-id [buf]
  (let [len (.get buf)]
    (if (= -1 len)
      (sr-uint64 buf)
      (mapv (fn [_] (sr-encoded-string buf)) (range len)))))

(def ^:private value-handlers
  {0x01 sr-string
   0x02 (sr-array-of sr-string)
   0x03 sr-encoded-string
   0x04 (sr-array-of sr-encoded-string)
   0x05 sr-float
   0x06 (sr-array-of sr-float)
   0x07 (sr-vec 2 sr-float)
   0x09 (sr-vec 3 sr-float)
   0x0a (sr-array-of (sr-vec 3 sr-float))
   0x11 (sr-vec 3 sr-int)
   0x12 (sr-array-of (sr-vec 3 sr-int))
   0x17 (sr-vec 4 sr-float)
   0x18 (sr-array-of (sr-vec 4 sr-float))
   ; XXX This is assuming version 2, which is probably safe for recent saves?
   0x19 sr-vec8s-biased
   0x1a (sr-array-of sr-vec8s-biased)
   0x25 sr-int
   0x26 (sr-array-of sr-int)
   0x27 sr-uint
   0x28 (sr-array-of sr-uint)
   0x2b sr-uint16
   0x2c (sr-array-of sr-uint16)
   0x2f sr-uint
   0x31 sr-int64
   0x32 (sr-array-of sr-int64)
   0x33 sr-uint64
   0x34 (sr-array-of sr-uint64)
   0x35 sr-byte-bool
   0x36 (sr-array-of sr-byte-bool)
   0x37 sr-ordinal-string

   0x39 sr-id
   0x3b sr-id
   0x3d sr-id
   0x3a (sr-array-of sr-id)
   0x3c (sr-array-of sr-id)})

(defn- sr-value
  "Expects buf to be pointing at a value type. Reads and returns the value."
  [buf]
  (let [typ (.getInt buf)]
    ((get value-handlers typ) buf)))


(defn- read-ordinal-strings
  "Options for an ordinal is stored as an array of
  [key string]; this is converted a Clojure map."
  [buf]
  (let [len (sr-int buf)]
    (into {} (map (fn [_] [(sr-int buf) (sr-string buf)])
                  (range len)))))

(defn- read-field
  "Each field is defined as uint32 value type, string name, and any
  field-type-specific data.
  I think only ordinal string fields have fields.

  Names are converted the keywords."
  [buf]
  (let [typ (sr-uint buf)]
    (if (zero? typ)
      nil
      {:type typ
       :name (csk/->kebab-case-keyword (sr-string buf))
       :data (if (= typ 0x37)
               (read-ordinal-strings buf)
               nil)})))

(defn- read-struct-fields
  [buf]
  (loop [fs []]
    (let [f (read-field buf)]
      (if (nil? f)
        fs
        (recur (conj fs f))))))


(defn- read-struct-block
  "Buffer is pointed after the type, at the validity bool."
  [buf]
  (let [valid? (sr-byte-bool buf)]
    (if valid?
      (let [id     (sr-int buf)
            name   (sr-string buf)
            fields (read-struct-fields buf)]
        {:id id :name name :fields fields})
      nil)))

#_(missing-handler s buf typ id name type)

(defn- missing-handler [buf struct-type block-id field-name field-type]
  (throw (ex-info (format "Error parsing block %d (struct type %d) at field %s (type $%02x)"
                          block-id struct-type field-name field-type)
                  {:struct-type struct-type
                   :block-id    block-id
                   :field-type  field-type
                   :buf         buf})))

(def ^:private ^:dynamic *seen* nil)

(def ^:private ref?  #{0x39 0x3b 0x3d})
(def ^:private refs? #{0x3a 0x3c})

(defn- data-block
  "Expects the buffer positioned just after the block type, which is
  passed in.
  Data blocks consist of the type (already read), an ID, and its field values.

  Returned as a map with keywordized keys."
  [structs refs buf typ]
  (let [id     (sr-id buf)
        struct (get structs typ)]
    (reduce (fn [m {:keys [name type]}]
              (if-let [handler (get value-handlers type)]
                (assoc m name (handler buf))
                (missing-handler buf typ id name type)
                #_(cond
                    (not type)    m [id m] ; No fields left.
                    (not handler)
                    :else         (recur fields)
                    #_(cond
                        (ref? type)
                        (ref?  type) (recur fields (assoc m name (lookup refs value)))
                        (refs? type) (recur fields (assoc m name (mapv #(lookup refs %) value)))
                        :else))))
            {:sii/struct   struct
             :sii/block-id id}
            (:fields struct))))

(defn parse-sii-raw
  "Parses the binary SII file, returning a map from IDs to maps for each block."
  [buf]
  ; Skip over the header
  (.position buf 8)
  (binding [*seen* (atom #{})] ; Block IDs which have been referenced from another block.
    (loop [structs {}   ; Structure numbers to struct maps.
           refs    {}]  ; Block IDs to the block map, for all blocks.
      (let [typ (sr-int buf)]
        (if (zero? typ)
          ;; Structure block
          (if-let [sdef (read-struct-block buf)]
            ;; Valid structure
            (recur (assoc structs (:id sdef) sdef)
                   refs)
            ;; Bad structure block = EOF. Return the unseen refs, ie. the top level.
            (let [seen @*seen*]
              (for [[id value] refs
                    :when (not (seen id))]
                value)))
          ;; Data block, parse it based on its structure number.
          (let [block (data-block structs refs buf typ)]
            (recur structs
                   (assoc refs (:sii/block-id block) block))))))))

(declare ^:private build-block)

(defn- lookup [{:keys [cooked raw seen] :as s} block-id]
  (cond
    (= block-id [])        nil
    (get @cooked block-id) (do (swap! seen conj block-id)
                               (get @cooked block-id))
    (get raw block-id)     (do (build-block s (get raw block-id))
                               (recur s block-id))
    :else [:sii/ref block-id]))

(defn- build-field [s block {:keys [name type] :as _struct-field}]
  (cond
    (ref? type)  (assoc block name (lookup s (get block name)))
    (refs? type) (assoc block name (mapv #(lookup s %) (get block name)))
    :else        block))

(defn- build-block [s block]
  (let [struct (:sii/struct block)
        cooked (reduce (partial build-field s) block (:fields struct))]
    (swap! (:cooked s) assoc (:sii/block-id block) cooked)
    s))

(defn parse-sii
  "Parses a binary SII file. Returns a sequence of maps, holding all the
  top-level values in the file. By top-level, I mean all the data blocks which
  were not referenced from another data block.

  Every map has two standard keys, the only namespaced keys present:
  - `:sii/block-id` giving the ID of the block
  - `:sii/structure` holding the map for this structure (nil for structures)"
  [buf]
  (let [;; Map of blocks by their IDs, without any inlining.
        raw    (parse-sii-raw buf)
        s      (reduce build-block
                       {:raw    (into {}
                                      (map (juxt :sii/block-id identity))
                                      raw)
                        :cooked (atom {})
                        :seen   (atom #{})}
                       raw)
        cooked @(:cooked s)
        seen   @(:seen s)]
    (keep (fn [[id block]]
               (when-not (seen id)
                 block))
          cooked)))
