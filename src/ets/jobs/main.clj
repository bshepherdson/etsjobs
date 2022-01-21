(ns ets.jobs.main
  (:require
    [clojure.java.io :as io]
    [ets.jobs.core :as jobs]
    [ets.jobs.decrypt :as decrypt]
    [ets.jobs.map :as map]
    [ets.jobs.sii-file :as sf]
    [ets.jobs.util :as util])
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

(defn find-jobs [profile]
  (->> (:dir profile)
       (io/file)
       util/latest-save
       (#(do (prn %) %)) ; Dumps the filename.
       decrypt/decode
       sf/parse-sii
       jobs/achievable-jobs))

(defn -main [dir profile-name & _]
  (->> (selected-profile dir profile-name)
       find-jobs
       (map print-ach)
       doall))

