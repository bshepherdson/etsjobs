(ns ets.jobs.database.interface
  (:require
    [datahike.core :as d]
    [ets.jobs.database.core :as core]
    [ets.jobs.database.queries :as q]))

(defn create [path]
  (core/create path))

(defn open [cfg]
  (core/open cfg))

(defn close [conn]
  (core/close conn))

(defn transact [conn tx-data]
  (d/transact conn tx-data))

; Queries
(defn fetch-profile [db id]
  (q/fetch-profile db id))
