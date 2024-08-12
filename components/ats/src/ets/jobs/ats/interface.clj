(ns ets.jobs.ats.interface
  (:require
   [com.wsscode.pathom3.connect.indexes :as pci]
   [ets.jobs.ats.achievements :as achievements]
   [ets.jobs.ats.map :as map]))

(def achievement-job-flags
  achievements/achievement-job-flags)

(def pathom-index
  (pci/register [achievements/index map/index]))
