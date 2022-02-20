(ns ets.jobs.database.core
  (:require
    [datalevin.core :as d]
    [ets.jobs.database.schema :as schema]))

(defn open [path]
  (d/get-conn path schema/schema))

