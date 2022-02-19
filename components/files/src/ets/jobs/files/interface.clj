(ns ets.jobs.files.interface
  (:require
    [ets.jobs.files.core :as core]))

(defn latest-save [dir]
  (core/latest-save dir))

(defn profile-root
  "Finds the root profile directory by platform.
  The game is either :ets2 or :ats.
  On Windows, it's HOME/Documents/Euro Truck Simulator 2/profiles.
  On Mac, HOME/Library/ApplicationSupport/Euro Truck Simulator 2/profiles.
  On Linux, HOME/.local/share/Euro Truck Simulator 2"
  [game]
  (core/profile-root game))

