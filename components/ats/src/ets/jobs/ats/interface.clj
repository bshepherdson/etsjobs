(ns ets.jobs.ats.interface
  (:require
    [ets.jobs.ats.achievements :as ach]
    [ets.jobs.ats.map :as map]))

(def ats-meta ach/ats-meta)

(def cargos        map/cargos)
(def cities        map/cities)
(def company-names map/company-names)

(defn human-name [slug]
  (map/human-name slug))

