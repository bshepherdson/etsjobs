(ns ets.jobs.search.interface
  (:require
   [ets.jobs.search.core :as core]))

(defn profiles [game]
  (:profiles (core/global-query game [:profiles])))

(defn time-info [game profile-id]
  (core/jobs-query
    game profile-id
    [:time/local
     :time/cest
     :time/zone-name]))

(defn jobs-query [game profile query]
  (core/jobs-query game profile query))

(comment
  (core/global-query :ets2 [:regions/all])
  (core/jobs-query :ets2 "426973686F7032" [:regions/all])
  (time-info :ets2 "426973686F7032")
  (job-offers :ets2 "426973686F7032")
  )
