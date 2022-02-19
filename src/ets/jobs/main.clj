(ns ets.jobs.main
  (:require
    [clojure.java.io :as io]
    [ets.jobs.core :as jobs]
    [ets.jobs.ets2.achievements :as achs]
    [ets.jobs.ets2.map :as map]
    [ets.jobs.web :as web]
    [ring.adapter.jetty :as jetty])
  (:gen-class))

(def port
  "This is TS (Truck Sim) in ASCII."
  8483)

(defn start []
  (jetty/run-jetty web/handler
                   {:port  port
                    :join? false}))

(defn -main [& _]
  (jetty/run-jetty web/handler {:port port}))

(comment
  (start)
  )

