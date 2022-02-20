(ns ets.jobs.database.interface
  (:require
    [ets.jobs.database.core :as core]))

(defn open [path]
  (core/open path))

