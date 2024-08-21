(ns ets.jobs.database.interface
  (:require
   [datahike.api :as d]
   [ets.jobs.database.schema :as schema]))

(defn new-database
  ""
  [])

(def ^:private config
 {:store              {:backend :mem}
  :schema-flexibility :write})

(defn new-database
  "Create a new in-memory database with the schema loaded.
  
  Does not include the initial data for the specific game. Expects to be called
  from the `ats` or `ets2` components to include that initial data."
  [db-id]
  (let [cfg  (assoc-in config [:store :id] db-id)
        _    (when (d/database-exists? cfg)
               (d/delete-database cfg))
        _    (d/create-database cfg)
        conn (d/connect cfg)]
      ;; Transacting the schemas.
      (d/transact conn {:tx-data schema/schema})
      conn))
