(ns ets.jobs.database.queries
  (:require
    [datahike.core :as d]))

(defn fetch-profile [db id]
  (d/pull db '[* {:progress/_profile [* {:standard/_progress [*]}]}]
          [:profile/id id]))

(defn progress-for [db id ach]
  (or (d/q '[:find ?progress .
             :in $ ?profile-id ?ach :where
             [?profile :profile/id ?profile-id]
             [?progress :progress/profile ?profile]
             [?progress :progress/achievement ?ach]]
           db id ach)
      "standard-industry"))

(defn industry-standard [db id loc]
  (or (d/q '[:find ?std .
             :in $ ?profile-id ?loc :where
             [?profile :profile/id ?profile-id]
             [?progress :progress/profile ?profile]
             [?std :standard/progress ?progress]
             [?std :standard/location ?loc]]
           db id loc)
      "new-standard"))

