(ns ets.jobs.main
  (:require
    [ets.jobs.http.interface :as web]
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

