(ns ets.jobs.ats.interface
  (:require
   [datahike.api :as d]
   [ets.jobs.database.interface :as database]
   [ets.jobs.ats.achievements :as achievements]
   [ets.jobs.ats.ingest :as ingest]
   [ets.jobs.ats.map :as map]
   [ets.jobs.ats.oversize :as oversize]))

(defn ^:private new-database
  "Create a new in-memory database with both schema and initial data loaded."
  []
  (let [conn (database/new-database "ats")]
    ;; TODO: Consider reading map data from the SCS files as well?
    (d/transact conn {:tx-data @map/initial-data})
    (d/transact conn {:tx-data @oversize/tx-oversize})
    conn))

(defn ingest-sii
  "Given a decrypted, parsed save game SII file (a seq of block maps), construct
  a new in-memory database and populate it with this game's data."
  [sii-blocks]
  (let [conn (new-database)]
    (ingest/ingest-sii conn sii-blocks)
    conn))

(defn achievement-groups
  "List of specs for the various groups of achievements."
  []
  achievements/achievement-groups)

(defn achievement-info
  "Queries for relevant jobs and the current progress of the achievement.
  
  Returns `{:jobs [...], :progress {...}}`.
 
  `:jobs` is a list of job offers which are available and would contribute to
  the achievement.
  
  `:progress` is a map with one of several kinds, indicated by `:type`:
  - `:set/*` returns `{:completed [...], :needed [...]}`, with the inner values
    determined by `:set/city`, `:set/cargo`, `:set/company`.
  - `:count` returns `{:completed 3, :total 10}` for 3 / 10 progress.
  - `:frequencies` returns `{label count-structure}`, ie. a set of `:count`
    style structures labeled by the segment of the job
  - `:special/*` are custom"
  [db cheevo-id]
  (achievements/achievement-info db cheevo-id))
