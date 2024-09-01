(ns ets.jobs.files.core
  (:require
    [clojure.java.io :as io]
    [clojure.string :as str])
  (:import
    [java.io ByteArrayInputStream File]))

(defn- before-last [s sub]
  (if-let [sep (str/last-index-of s sub)]
    (subs s 0 sep)
    ""))

(defn- after-last [s sub]
  (if-let [sep (str/last-index-of s sub)]
    (subs s (inc sep))
    s))

(defn dirname [path-str]
  (before-last path-str "/"))

(defn basename [path-str]
  (after-last path-str "/"))

(defn strip-extension [path-str]
  (before-last path-str "."))

(defn latest-save [dir]
  (->> (file-seq dir)
       (filter #(= "game.sii" (.getName %)))
       (apply max-key #(.lastModified %))))

(defn from-buf [buf]
  (String. (.array buf)))

(defn spit-binary [f buf]
  (with-open [out (io/output-stream f)]
    (let [in (ByteArrayInputStream. (.array buf))]
      (io/copy in out))))

(def game-paths
  {:ets2 "Euro Truck Simulator 2"
   :ats  "American Truck Simulator"})

(defn- home-dir []
  (System/getProperty "user.home"))

(defn- operating-system []
  (let [os (System/getProperty "os.name")]
    (cond
      (str/includes? os "Windows") :windows
      (str/includes? os "Linux")   :linux
      (str/includes? os "Mac")     :mac
      :else (throw (Exception. (str "Unknown OS " os))))))

(defn- profile-path [game]
  (let [game-path (game-paths game)]
    (case (operating-system)
      :windows ["Documents" game-path "profiles"]
      :linux   [".local" "share" game-path "profiles"]
      :mac     ["Library" "Application Support" game-path "profiles"])))

(defn ->file
  "Given a `root` dir - a `File` or string - and a seq of 0 or more string paths,
  return a `File` for the specified path.
  
  The 1-arity will use the first item in the seq as the root."
  ([dirs]
   (let [[root & tail] dirs]
     (->file root tail)))
  ([root dirs]
   (let [root (if (instance? File root)
                root
                (File. root))]
     (reduce #(File. %1 %2) root dirs))))

(defn profile-root
  "Finds the root profile directory by platform.
  The game is either :ets2 or :ats.
  On Windows, it's HOME/Documents/Euro Truck Simulator 2/profiles.
  On Mac, HOME/Library/ApplicationSupport/Euro Truck Simulator 2/profiles.
  On Linux, HOME/.local/share/Euro Truck Simulator 2"
  [game]
  (->> (profile-path game)
       (->file (home-dir))))

(defn- try-paths [paths]
  (->> paths
       (map ->file)
       (filter #(.exists %))
       first))

(defn- steamapps-path [game]
  (case (operating-system)
    :windows (try-paths
               [["C:/" "Program Files" "Steam" "steamapps" "common"]
                ["C:/" "Program Files (x86)" "Steam" "steamapps" "common"]])
    :linux   [(home-dir) ".local" "share" "Steam" "steamapps" "common"]
    :mac     [(home-dir) "Library" "Application Support" "Steam" "steamapps" "common"]))

(defn game-root
  "Finds the root of the game's own directory by platform.
  `game` is either :ets2 or :ats.
  On Windows, this is either `Program Files/Steam/steamapps/common/$GAME` or
  `Program Files (x86)/Steam/steamapps/common/$GAME`.
  On Linux, `$HOME/.local/share/Steam/steamapps/common/$GAME`.
  On Mac, `$HOME/Library/Application Support/Steam/steamapps/common/$GAME`."
  [game]
  (let [path (conj (steamapps-path game) (game-paths game))
        file (->file path)]
    file))

(defn scs-files
  "Given the game (`:ats` or `:ets2`), search the game's root directory for
  all `*.scs` files and return a list of their names.
  
  Excludes the colossal base*.scs files for speed, since we don't need them."
  [game]
  (->> (game-root game)
       file-seq
       (filter #(str/ends-with? % ".scs"))
       (map str)
       (map basename)
       (remove #(str/starts-with? % "base"))))
