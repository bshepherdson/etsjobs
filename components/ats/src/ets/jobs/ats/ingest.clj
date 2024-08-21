(ns ets.jobs.ats.ingest
  "Consume SII contents to produce a Datahike transaction.
  
  The values can come in any order, so this can recursively construct values."
  (:require
   [datahike.api :as d]
   [ets.jobs.ats.map :as map]))

(defn- location-tx [city company]
  (let [city (map/canonical-city-name city)]
    {:location/company [:company/ident company]
     :location/city    [:city/ident city]
     :db/id            (str "location__" city "." company)}))

(defn- location-slug->ref [slug]
  (let [regex                 #"^(?:company\.volatile\.)?([^\.]+)\.([^\.]+)$"
        [_whole company city] (re-matches regex slug)]
    (location-tx city company)))

(defn- cargo-slug->ref [slug]
  (let [[_whole cargo] (re-matches #"^cargo\.([^\.]+)$" slug)]
    [:cargo/ident cargo]))

(defmulti ^:private ingest-block
  (fn [block]
    (-> block :sii/struct :name)))

(defmethod ingest-block :default [_block]
  nil)

;; These really correspond to locations, more than to companies in the abstract.
(defmethod ingest-block "company" [block]
  (let [[_company _volatile company-slug city-slug] (:sii/block-id block)
        canonical-city (map/canonical-city-name city-slug)]
    (when (and (not= city-slug canonical-city)
               (seq (:job-offer block)))
      (throw (ex-info "Can't happen: company found with job offers but with a replaced city"
                      {:original-city city-slug
                       :renamed-city  canonical-city
                       :job-offers    (:job-offer block)})))
    ;; So we skip the dead-named cities which are empty.
    (when (= city-slug canonical-city)
      [(merge (location-tx city-slug company-slug)
              {:job/_source (vec (for [job-id (:job-offer block)]
                                   {:sii/block-id job-id}))})])))

(defmethod ingest-block "job_offer_data"
  [{:keys [urgency shortest-distance-km expiration-time target]
    [_prefix cargo] :cargo
    block-id        :sii/block-id}]
  ;; There are non-jobs in the list for reasons I don't grok.
  ;; These have urgency -1, which is used to filter them out.
  #_(prn "expiration-time" expiration-time (type expiration-time))
  (when (pos? urgency)
    [{:sii/block-id          (long block-id)
      :job/cargo             [:cargo/ident cargo]
      :job/target            (location-slug->ref target)
      :job/distance-km       (long shortest-distance-km)
      :job/urgency           (long (inc urgency))
      :offer/expiration-time (long expiration-time)}]))

;; profit_log and profit_log_entry are for other drivers
;; delivery_log and delivery_log_entry are for own driving

(defmethod ingest-block "delivery_log" [block]
  (map-indexed (fn [i block-id]
                 {:delivery/index i
                  :sii/block-id   block-id})
               (:entries block)))

(defmethod ingest-block "delivery_log_entry" [block]
  (let [[completed-at source target cargo xp profit
         distance-km damage-ratio time-to-expiry urgency
         auto-parked? parking-option company-truck?
         _advert-profit fines started-at _vehicle
         _planned-distance-km job-type route-name
         _mystery1 _double-trailer? cargo-mass _cargo-units] (:params block)]
    ;; These are all strings as they come out of the file, so they need number parsing.
    (when (not= job-type "freerm")
      [(merge
         {:job/type                    (keyword "job.type" job-type)
          :job/cargo                   (cargo-slug->ref cargo)
          :job/distance-km             (parse-long distance-km)
          ;; Deliveries urgency is 0, 1 or 2. Bump it to 1-3.
          :job/urgency                 (inc (parse-long urgency))
          :delivery/start-time         (parse-long started-at)
          :delivery/end-time           (parse-long completed-at)
          :delivery/remaining-time     (parse-long time-to-expiry)
          :delivery/xp                 (parse-long xp)
          :delivery/profit             (parse-long profit)
          :delivery/fines              (parse-long fines)
          :delivery/damage-ratio       (parse-double damage-ratio)
          :delivery/company-truck?     (= company-truck? "1")
          :delivery/parking-difficulty (if (= auto-parked? "1")
                                         0
                                         (parse-long parking-option))
          :delivery/cargo-mass-kg      (parse-double cargo-mass)
          :sii/block-id                (:sii/block-id block)}
         (when (not= job-type "spec_oversize")
           {:job/source (location-slug->ref source)
            :job/target (location-slug->ref target)})

         ;; And a bonus field for special transport routes.
         (when (not-empty route-name)
           {:job.special/route {:db/id              (str "special_route__" route-name)
                                :route.special/name route-name}}))])))

(defn- jobs-without-target [db]
  (d/q '[:find ?job :where
         [?job :job/id _]
         [(missing? ?job :job/target)]]
       db))

(defn- ingest-cleanup
  "Some companies have jobs in their listings which are not real.
  
  This function runs a new transaction that deletes any extra junk."
  [conn]
  (d/transact conn {:tx-data (for [job-eid (jobs-without-target (d/db conn))]
                               [:db/retractEntity job-eid])}))

(defn- ingestion-tx [blocks]
  (into [] (mapcat ingest-block) blocks))

(defn ingest-sii [conn blocks]
  (doto conn
    (d/transact {:tx-data (ingestion-tx blocks)})
    (ingest-cleanup)
    #_conn))
