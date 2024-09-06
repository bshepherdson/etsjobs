(ns ets.jobs.scs.interface
  "Implements HashFS to read the .scs files.
 
  See [TruckLib docs](https://sk-zk.github.io/trucklib/master/docs/TruckLib.HashFs/hashfs.html)
  for reference."
  (:refer-clojure :exclude [file-seq slurp])
  (:require
   [ets.jobs.files.interface :as files]
   [ets.jobs.sii.interface :as sii]
   [ets.jobs.scs.codec :as codec]))

(defn scs-file
  "Given a path **relative to the ATS/ETS2 directory**, parse that SCS file and
  return it."
  [game scs-filename]
  (try
    (when-let [file (files/scs-file game scs-filename)]
      (codec/scs-file file))
    (catch Exception e
      (if (= (ex-message e) "Couldn't read SCS file.")
        nil
        (throw e)))))

(defn directory-listing
  "Given an SCS buffer (as returned by `scs-file`) and a `path` within it, return
  a map `{:subdirs [\"foo\" ...], :files [\"bar\" ...]}` within it."
  [scs path]
  (codec/directory-listing scs path))

(defn root-directory
  "Return the `directory-listing` for the root directory of this SCS file.
  
  Nearly all SCS files have a root directory, but a few do not. In that case
  this will return nil."
  [scs]
  (directory-listing scs "/"))

(defn slurp
  "Given an SCS file (as returned by `scs-file`) and a path within it, read
  that file and return its contents as a string.
  
  If you want to parse it as a binary or text SII file, use `scs->binary-sii`
  and `scs->text-sii` instead."
  [scs path]
  (codec/slurp scs path))

(defn scs->binary-sii
  "Given an SCS file (as returned by `scs-file`) and a path within it, read
  that file and parse its contents as a **binary** SII file."
  [scs path]
  (let [buf (codec/file-contents scs path)]
    (sii/parse-sii buf)))

(defn scs->text-sii
  "Given an SCS file (as returned by `scs-file`) and a path within it, read
  that file and parse its contents as a **text** SII file."
  [scs path]
  (let [dir (files/dirname path)]
    (when-let [content (codec/slurp scs path)]
      (sii/parse-with-includes content
                               (fn [filename]
                                 (let [inc-path (if (= \/ (first filename))
                                                  filename
                                                  (str dir "/" filename))]
                                   (or (codec/slurp scs inc-path)
                                       (throw (ex-info "Failed to read include file"
                                                       {:requested-file filename
                                                        :include-path   inc-path
                                                        :pwd            dir})))))))))

(defn- locale-file [scs filename]
  (if-let [[{:keys [key val]}] (scs->text-sii scs filename)]
    (zipmap key val)
    (throw (ex-info "Failed to parse locale" {:locale filename}))))

(defn locale
  ([game] (locale game "en_us"))
  ([game lang]
   (let [scs (scs-file game "locale.scs")]
     (reduce merge {}
             (for [base ["local.sii" "local.override.sii"]]
               (locale-file scs (str "/locale/" lang "/" base)))))))

(defn file-seq
  "Returns a list of all files (not directories) in the given SCS file.
  
  The sub-path is optional. Returns the paths sorted by name."
  ([scs] (codec/file-seq scs))
  ([scs path] (codec/file-seq scs path)))
