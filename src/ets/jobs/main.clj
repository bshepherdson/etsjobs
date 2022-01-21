(ns ets.jobs.main
  (:require
    [clojure.java.io :as io]
    [ets.jobs.core :as jobs]
    [ets.jobs.map :as map]
    [ets.jobs.web :as web]
    [ring.adapter.jetty :as jetty])
  (:gen-class))

(defn print-ach [[k jobs]]
  (let [{:keys [region name]} (get jobs/achievements k)]
    (println (str region ": " name))
    (doseq [{:keys [origin destination sender recipient cargo distance]} jobs]
      (printf "  %-14s   %-30s  ->   %-30s\n      %4dkm         %-28s         %s\n"
              cargo    (map/human-name origin) (map/human-name destination)
              distance
              (or (map/company-names sender) sender)
              (or (map/company-names recipient) recipient)))
    (println "\n")))

(defn selected-profile [dir profile-name]
  (first (filter #(= profile-name (:name %))
                 (jobs/profiles (io/file dir)))))

(def port
  "This is TS (Truck Sim) in ASCII."
  8483)

(defn cli-output [dir profile-name & _]
  (->> (selected-profile dir profile-name)
       :dir
       jobs/parse-latest
       jobs/achievable-jobs
       (map print-ach)
       doall))

(defn start []
  (jetty/run-jetty web/handler
                   {:port  port
                    :join? false}))

(defn -main [& _]
  (jetty/run-jetty web/handler {:port port}))

(comment
  (start)
  )

