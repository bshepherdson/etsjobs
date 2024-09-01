(ns ets.jobs.files.interface
  (:require
   [clojure.string :as str]
   [ets.jobs.files.core :as core]))

(defn latest-save [dir]
  (core/latest-save dir))

(defn from-buf [buf]
  (core/from-buf buf))

(defn profile-root
  "Finds the root profile directory by platform.
  The game is either :ets2 or :ats.
  On Windows, it's HOME/Documents/Euro Truck Simulator 2/profiles.
  On Mac, HOME/Library/ApplicationSupport/Euro Truck Simulator 2/profiles.
  On Linux, HOME/.local/share/Euro Truck Simulator 2"
  [game]
  (core/profile-root game))

(defn game-root
  "Finds the root of the game's own directory by platform.
  `game` is either :ets2 or :ats."
  [game]
  (core/game-root game))

(defn ->file
  "Given a `root` dir - a `File` or string - and a seq of 0 or more string paths,
  return a `File` for the specified path.
  
  The 1-arity will use the first item in the seq as the root."
  ([dirs]
   (core/->file dirs))
  ([root dirs]
   (core/->file root dirs)))

(defn scs-file
  "Given the `game` keyword and an SCS filename (eg. `dlc_arizona.scs`) returns
  a `File` for that SCS file."
  [game scs-filename]
  (let [root (core/game-root game)
        file (core/->file root [scs-filename])]
    (if (.exists file)
      file
      (throw (ex-info "Couldn't read SCS file."
                      {:game         game
                       :root         root
                       :root-exists? (.exists root)
                       :scs-file     scs-filename})))))

(defn scs-files
  "Given the game (`:ats` or `:ets2`), search the game's root directory for
  all `*.scs` files and return a list of their names.
  
  Excludes the colossal base*.scs files for speed, since we don't need them."
  [game]
  (core/scs-files game))

(defn dirname [path-str]
  (core/dirname path-str))

(defn basename [path-str]
  (core/basename path-str))

(defn strip-extension [path-str]
  (core/strip-extension path-str))
