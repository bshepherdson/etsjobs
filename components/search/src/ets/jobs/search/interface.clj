(ns ets.jobs.search.interface
  (:require
   [ets.jobs.search.core :as core]))

(defn profiles [game]
  (core/all-profiles game))

#_(defn time-info [game profile-id]
  (core/jobs-query
    game profile-id
    [:time/local
     :time/cest
     :time/zone-name]))

(defn parse-latest-save [game profile-id]
  (core/parse-latest-save game profile-id))

(comment
  (core/global-query :ets2 [:regions/all])
  (core/jobs-query :ets2 "426973686F7032" [:regions/all])
  (time-info :ets2 "426973686F7032")
  (job-offers :ets2 "426973686F7032")
  )
