(ns ets.jobs.ets2.interface
  (:require
    [ets.jobs.ets2.achievements :as achievements]
    [ets.jobs.ets2.map :as map]))

(def ets-meta achievements/ets-meta)

(def cargos        map/cargos)
(def cities        map/cities)
(def company-names map/company-names)
(def flags         map/flags)

(defn human-name [slug]
  (map/human-name slug))

