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

(def game-paths
  {:ets2 "Euro Truck Simulator 2"
   :ats  "American Truck Simulator"})

(defn windows-path [game]
  ["Documents" (game-paths game) "profiles"])
(defn mac-path [game]
  ["Library" "Application Support" (game-paths game) "profiles"])
(defn linux-path [game]
  [".local" "share" (game-paths game) "profiles"])

(defn profile-root
  "Finds the root profile directory by platform.
  The game is either :ets2 or :ats.
  On Windows, it's HOME/Documents/Euro Truck Simulator 2/profiles.
  On Mac, HOME/Library/ApplicationSupport/Euro Truck Simulator 2/profiles.
  On Linux, HOME/.local/share/Euro Truck Simulator 2"
  [game]
  (let [home (System/getProperty "user.home")
        os   (System/getProperty "os.name")
        path (cond
               (str/includes? os "Windows") (windows-path game)
               (str/includes? os "Linux")   (linux-path   game)
               (str/includes? os "Mac")     (mac-path     game)
               :else (throw (Exception. (str "Unknown OS " os))))]
    (reduce #(File. %1 %2) home path)))


