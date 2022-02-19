(ns ets.jobs.search.interface
  (:require
    [ets.jobs.search.core :as core]))

(defn achievable-jobs [game s]
  (core/achievable-jobs game s))

(def game-meta core/game-meta)

(defn local-time [s]
  (core/local-time s))

(defn parse-latest [profile]
  (core/parse-latest profile))

(defn profiles [dir]
  (core/profiles dir))

