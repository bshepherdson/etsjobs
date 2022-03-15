(ns ets.jobs.database.updates
  (:require
    [datahike.api :as d]
    [ets.jobs.database.queries :as q]))

(defn update-industry-standard
  [db id {:keys [standard/location standard/count]}]
  [{:db/id             (q/industry-standard db id location)
    :standard/progress (q/progress-for      db id :industry-standard)
    :standard/location location
    :standard/count    count}])

(defn put-location [db id achievement location]
  (let [progress (q/progress-for db id achievement)]
    [[:db/add progress :progress/location location]]))

(defn delete-location [db id achievement location]
  (let [progress (q/progress-for db id achievement)]
    [[:db/retract progress :progress/location location]]))

(defn update-count [db id achievement n]
  (let [progress (q/progress-for db id achievement)]
    [{:db/id                progress
      :progress/profile     [:profile/id id]
      :progress/achievement achievement
      :progress/count       n}]))

(defn update-completion [db id achievement completed?]
  (let [progress (q/progress-for db id achievement)]
    [{:db/id                progress
      :progress/profile     [:profile/id id]
      :progress/achievement achievement
      :progress/complete?   completed?}]))

