(ns ets.jobs.ets2.interface
  (:require
   [ets.jobs.ets2.achievements :as achievements]
   [ets.jobs.ets2.map :as map]))

#_(def ets-meta achievements/ets-meta)

#_(def cargos        map/cargos)
#_(def cities        map/cities)
#_(def company-names map/company-names)
#_(def flags         @#'map/flags)

#_(defn human-name [slug]
    (map/human-name slug))

#_(def achievement-job-flags
  achievements/achievement-job-flags)

#_(def pathom-index
  (pci/register [map/index achievements/index]))
