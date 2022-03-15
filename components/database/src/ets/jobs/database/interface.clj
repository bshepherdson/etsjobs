(ns ets.jobs.database.interface
  (:require
    [datahike.core :as d]
    [ets.jobs.database.core :as core]
    [ets.jobs.database.queries :as q]
    [ets.jobs.database.updates :as updates]))

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

(defn progress-for [db id ach]
  (q/progress-for db id ach))

(defn industry-standard
  "Returns the EID for the industry standard record for this profile and
  location, or a tempid if there isn't one yet."
  [db id loc]
  (q/industry-standard db id loc))

; Updates
(defn update-industry-standard [db id body]
  (updates/update-industry-standard db id body))

(defn put-location [db id achievement location]
  (updates/put-location db id achievement location))

(defn delete-location [db id achievement location]
  (updates/delete-location db id achievement location))

(defn update-count [db id achievement n]
  (updates/update-count db id achievement n))

(defn update-completion [db id achievement completed?]
  (updates/update-completion db id achievement completed?))

