(ns ets.jobs.ats.interface
  (:require
   [datahike.api :as d]
   [ets.jobs.database.interface :as database]
   [ets.jobs.ats.achievements :as achievements]
   [ets.jobs.ats.ingest :as ingest]
   [ets.jobs.ats.map :as map]))

(defn ^:private new-database
 "Create a new in-memory database with both schema and initial data loaded."
 []
 (let [conn (database/new-database "ats")]
  (d/transact conn {:tx-data map/initial-data})
  conn))

(defn ingest-sii
 "Given a decrypted, parsed save game SII file (a seq of block maps), construct
 a new in-memory database and populate it with this game's data."
 [sii-blocks]
 (let [conn (new-database)]
  (ingest/ingest-sii conn sii-blocks)
  conn))

(def achievement-groups
 "List of specs for the various groups of achievements."
 achievements/achievement-groups)

(defn relevant-jobs
  "Queries for relevant jobs - that is, job offers which are available and
  would contribute to the achievement."
  [db cheevo-id]
  (achievements/relevant-jobs db cheevo-id))

(defn achievement-progress
  "Returns an achievement-specific data structure giving the progress.
  
  - Sets of sources (most common) get `{:completed #{...}, :needed #{...}}.
      - :needed is optional
  - Counts get `{:completed 5, :required 10}`
  - Frequencies get a map of `{key Count-structure}`
  - Special cases are special"
  [db cheevo-id]
  (achievements/achievement-progress db cheevo-id))
