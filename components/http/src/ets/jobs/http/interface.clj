(ns ets.jobs.http.interface
  (:require
    [ets.jobs.http.core :as core]))

(defn handler [req]
  (core/handler req))
