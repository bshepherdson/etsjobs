(ns ets.jobs.scs.interface
  "Implements HashFS to read the .scs files.
 
  See [TruckLib docs](https://sk-zk.github.io/trucklib/master/docs/TruckLib.HashFs/hashfs.html)
  for reference."
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

(defn scs-file-contents
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
                                 (codec/slurp scs (str dir "/" filename)))))))

(comment
  #_(def f (scs-file :ats "base_vehicle.scs"))
  (def f (scs-file :ats #_"def.scs"
                   "dlc_freightliner_cascadia2019.scs"))
  

  (directory-listing f "/def/vehicle/truck/freightliner.cascadia2019/engine")

  (println (codec/slurp f "def/country/california.sui"))
  (->> (scs->text-sii f "/def/vehicle/truck/freightliner.cascadia2019/engine/dd16_600.sii")
       )
  *e
  ;; T [lb.ft] = ( P [hp] x 5252 ) / N [RPM]
  ;; P [hp] = T.N / 5252
  
  (println (scs-file-contents f "def/city/sacramento.sui"))
  (println (scs-file-contents f "def/city/sacramento.sui"))
  
  (def fs (:files (directory-listing f "map/usa")))
  (scs-file-contents f "map/usa.mbd")

  (directory-listing f "map")

  (->> fs
       (remove #(clojure.string/starts-with? % "sec"))
       (sort fs))
  )
