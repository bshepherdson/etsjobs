(ns hotreload
  (:require
    [ets.jobs.web :as web]
    [ring.adapter.jetty :as jetty]
    [ring.middleware.reload :as reload])
  (:gen-class))

(def dev-handler
  (reload/wrap-reload #'web/handler))

(defn start []
  (jetty/run-jetty dev-handler {:port 18483 :join? false}))

(comment
  (start))
