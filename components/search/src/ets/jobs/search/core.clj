(ns ets.jobs.search.core
  (:require
    [clojure.java.io :as io]
    [clojure.string :as str]
    [com.wsscode.pathom.viz.ws-connector.core :as pvc]
    [com.wsscode.pathom.viz.ws-connector.pathom3 :as p.connector]
    [com.wsscode.pathom3.connect.built-in.resolvers :as pbir]
    [com.wsscode.pathom3.connect.indexes :as pci]
    [com.wsscode.pathom3.connect.operation :as pco]
    [com.wsscode.pathom3.interface.eql :as p.eql]
    [ets.jobs.ats.interface :as ats]
    [ets.jobs.decrypt.interface :as decrypt]
    [ets.jobs.ets2.interface :as ets2]
    [ets.jobs.files.interface :as files]
    [ets.jobs.sii.interface :as sii])
  (:import
    [java.io File]))

;; :game is :ets2 or :ats

(pco/defresolver profile-root [{:keys [game]}]
  {::pco/input  [:game]
   ::pco/output [:profile-root]}
  {:profile-root (files/profile-root game)})

(defn profile-info [dir]
  (let [basics  (->> (File. dir "profile.sii")
                     decrypt/decode
                     sii/parse-profile-basics)]
    (assoc basics :profile/dir dir :profile/id (.getName dir))))

(pco/defresolver profiles [{:keys [profile-root]}]
  {::pco/input  [:profile-root]
   ::pco/output [:profiles]}
  {:profiles (into []
                   (comp (filter #(.isDirectory %))
                         (filter #(not= (first (.getName %)) \.))
                         (map profile-info))
                   (.listFiles profile-root))})

(pco/defresolver profile [{:keys [profile/id profiles]}]
  {::pco/input  [:profile/id :profiles]
   ::pco/output [:profile/name :profile/map
                 :profile/dir  :profile/saved-at]}
  (->> profiles
       (filter #(= (:profile/id %) id))
       first))

(pco/defresolver latest-save [{:keys [profile/dir]}]
  {::pco/input  [:profile/dir]
   ::pco/output [:profile/save-file]}
  {:profile/save-file (-> dir io/file files/latest-save)})


(defn- economy->job-offers [{:economy/keys [companies game-time]}]
  (let [offers (for [company                          companies
                     {[_ [_ cargo]] :cargo
                      exp           :expiration-time
                      distance      :shortest-distance-km
                      :keys [target urgency]
                      :as   offer}                    (:job-offer company)
                     :when (and (pos? exp)
                                (> exp game-time))]
                 (let [[_ _ sender-company sender-city] (:sii/block-id company)
                       [target-company target-city]     (str/split target #"\.")]
                   {:job/id              (:sii/block-id offer)
                    :job/origin          {:city/id    sender-city
                                          :company/id sender-company}
                    :job/destination     {:city/id    target-city
                                          :company/id target-company}
                    :job/cargo           {:cargo/id   cargo}
                    :job/expires-in-mins (- exp game-time)
                    :job/distance        distance
                    :job/urgency         urgency}))]
    (into {} (map (juxt :job/id identity)) offers)))

(pco/defresolver economy [{:keys [profile/save-file]}]
  {::pco/cache-store ::economy-cache
   ::pco/cache-key   (fn [{:keys [profile/id]} {:keys [profile/saved-at]}]
                       [id saved-at])
   ::pco/input       [:profile/id :profile/save-file :profile/saved-at]
   ::pco/output      [:economy/job-offers :economy/game-time
                      :economy/time-zone  :economy/time-zone-name]}
  (let [economy    (-> save-file
                       decrypt/decode
                       sii/parse-sii
                       first
                       (select-keys [:companies :game-time :time-zone :time-zone-name])
                       (update-keys #(keyword "economy" (name %))))]
    (-> economy
        (dissoc :economy/companies)
        (assoc :economy/job-offers (economy->job-offers economy)))))

;; Game time =================================================================
;; The game tracks sim time as minutes from the epoch: Week 1, Monday, 00:00Z.
;; This breaks the time down into a handier map:
;; {:week 12, :day "Wednesday", :hour 0, :min 12}
(def day-length  (* 60 24))
(def week-length (* day-length 7))

(def days ["Mon" "Tue" "Wed" "Thu" "Fri" "Sat" "Sun"])

(defn- time-breakdown [epoch-mins]
  (let [in-week (mod epoch-mins week-length)
        in-day  (mod epoch-mins day-length)]
    {:week (inc (quot epoch-mins week-length))
     :day  (nth days (quot in-week day-length))
     :hour (quot in-day 60)
     :mins (mod epoch-mins 60)}))

;; The time in the user's present time zone.
(pco/defresolver local-time [{:economy/keys [game-time time-zone]}]
  {::pco/output [:time/local]}
  {:time/local (time-breakdown (+ game-time time-zone -120))})

;; The time in the canonical CEST time zone.
(pco/defresolver cest-time [{:economy/keys [game-time]}]
  {::pco/output [:time/cest]}
  {:time/cest (time-breakdown game-time)})

(pco/defresolver time-zone-name [{:economy/keys [time-zone-name]}]
  {:time/zone-name (->> time-zone-name
                        (re-matches #"@@tz_(\w+)@@")
                        second
                        str/upper-case)})

;; Jobs ======================================================================
(def job-shape
  [:job/id
   {:job/origin         [:city/id :company/id]}
   {:job/destination    [:city/id :company/id]}
   {:job/cargo          [:cargo/id]}
   :job/expires-in-mins
   :job/distance
   :job/urgency])

(pco/defresolver all-jobs [{jobs :economy/job-offers}]
  {::pco/output [{:jobs/all-available [:job/id]}]}
  {:jobs/all-available (mapv #(select-keys % [:job/id]) (vals jobs))})

(def ^:private connect-parser? true)

(def economy-parsing-env
  (-> (pci/register [;; Profiles
                     latest-save profile profile-root profiles
                     ;; Economy
                     economy])
      #_(pci/register ets2/pathom-index)
      (assoc ::economy-cache (atom {}))
      (cond-> #_env connect-parser?
        (p.connector/connect-env {::pvc/parser-id `economy-parsing-env}))))

(def ^:private base-env
  (pci/register [;; Sim Time
                 cest-time local-time time-zone-name
                 ;; Jobs
                 (pbir/env-table-resolver
                   :economy/job-offers :job/id
                   job-shape)
                 all-jobs]))

(def ^:private ats-env
  (cond-> (pci/register base-env ats/pathom-index)
    connect-parser? (p.connector/connect-env {::pvc/parser-id `job-search-env})))

(def ^:private ets2-env
  (cond-> (pci/register base-env ets2/pathom-index)
    connect-parser? (p.connector/connect-env {::pvc/parser-id `job-search-env})))

(defn- job-search-env [game]
  (case game
    :ats  ats-env
    :ets2 ets2-env))

(defn global-query [game query]
  (p.eql/process economy-parsing-env {:game game} query))

(defn- parse-economy [game profile-id]
  (p.eql/process
    economy-parsing-env
    {:game game
     :profile/id profile-id}
    [:economy/game-time
     :economy/time-zone
     :economy/time-zone-name
     :economy/job-offers]))

(defn profile-query [game profile-id query]
  (p.eql/process
    (job-search-env game)
    {:game       game
     :profile/id profile-id}
    query))

(defn jobs-query [game profile-id query]
  (p.eql/process
    (job-search-env game)
    (merge {:game       game
            :profile/id profile-id}
           (parse-economy game profile-id))
    query))
