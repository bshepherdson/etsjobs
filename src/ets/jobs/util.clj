(ns ets.jobs.util
  (:require
    [clojure.java.io :as io]
    [clojure.string :as str])
  (:import
    [java.io ByteArrayInputStream File]))

(defn latest-save [dir]
  (->> (file-seq dir)
       (filter #(= "game.sii" (.getName %)))
       (apply max-key #(.lastModified %))))

(defn spit-binary [f buf]
  (with-open [out (io/output-stream f)]
    (let [in (ByteArrayInputStream. (.array buf))]
      (io/copy in out))))

(def ^:private windows-path
  ["Documents" "Euro Truck Simulator 2" "profiles"])
(def ^:private mac-path
  ["Library" "Application Support" "Euro Truck Simulator 2" "profiles"])
(def ^:private linux-path
  [".local" "share" "Euro Truck Simulator 2" "profiles"])

(defn profile-root
  "Finds the root profile directory by platform.
  On Windows, it's HOME/Documents/Euro Truck Simulator 2/profiles.
  On Mac, HOME/Library/ApplicationSupport/Euro Truck Simulator 2/profiles.
  On Linux, HOME/.local/share/Euro Truck Simulator 2"
  []
  (let [home (System/getProperty "user.home")
        os   (System/getProperty "os.name")
        path (cond
               (str/includes? os "Windows") windows-path
               (str/includes? os "Linux") linux-path
               (str/includes? os "Mac") mac-path
               :else (throw (Exception. (str "Unknown OS " os))))]
    (reduce #(File. %1 %2) home path)))


