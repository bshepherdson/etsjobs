(ns ets.jobs.util
  (:require
    [clojure.java.io :as io])
  (:import
    [java.io ByteArrayInputStream]))

(defn latest-save [dir]
  (->> (file-seq dir)
       (filter #(= "game.sii" (.getName %)))
       (apply max-key #(.lastModified %))))

(defn spit-binary [f buf]
  (with-open [out (io/output-stream f)]
    (let [in (ByteArrayInputStream. (.array buf))]
      (io/copy in out))))

