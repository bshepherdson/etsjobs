(ns ets.jobs.database.core
  (:require
    [datahike.api :as d]
    [ets.jobs.database.schema :as schema]))

(defn create [path]
  (let [cfg  {:store              {:backend :file
                                   :path    path}
              :initial-tx         schema/schema
              :schema-flexibility :write}]
    (when-not (d/database-exists? cfg)
      (d/create-database cfg))
    cfg))

(defn open [cfg]
  (d/connect cfg))

(defn close [conn]
  (d/release conn))


(comment
  (def base-cfg {:store              {:backend :file
                                      :path    "etsjobs"}
                 :initial-tx         schema/schema
                 :schema-flexibility :write})

  (d/database-exists? base-cfg)
  (d/delete-database base-cfg)
  (d/create-database base-cfg)
  (def theconn (open base-cfg))
  (d/datoms @theconn :aevt)
  )
