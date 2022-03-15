(ns ets.jobs.database.queries
  (:require
    [datahike.core :as d]))

(defn fetch-profile [db id]
  ;[* :standard/_progress [*]]
  (d/pull db '[* {:progress/_profile [* {:standard/_progress [*]}]}]
          [:profile/id id])
  #_(d/q '[:find (pull ?profile [:profile/id :profile/name :profile/game])
         :in $ ?id :where
         [?profile :profile/id ?id]]
       db id))

