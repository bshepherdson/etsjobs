(ns ets.jobs.search.core
  (:require
    [clojure.java.io :as io]
    [clojure.string :as str]
    [com.wsscode.pathom.viz.ws-connector.core :as pvc]
    [com.wsscode.pathom.viz.ws-connector.pathom3 :as p.connector]
    [com.wsscode.pathom3.connect.built-in.resolvers :as pbir]
    [com.wsscode.pathom3.connect.indexes :as pci]
    [com.wsscode.pathom3.connect.operation :as pco]
    [com.wsscode.pathom3.connect.planner :as pcp]
    [com.wsscode.pathom3.interface.eql :as p.eql]
    [com.wsscode.pathom3.interface.smart-map :as psm]
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

(pco/defresolver economy [{:keys [profile/id profile/save-file profile/saved-at]}]
  {::pco/cache-store ::economy-cache
   ::pco/cache-key   (fn [{:keys [profile/id]} {:keys [profile/saved-at]}]
                       [id saved-at])
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

#_(def jobs-table-sample
  [#:job{:id 105553124415792,
         :origin {:city/id "calais", :company/id "lkwlog"},
         :destination {:city/id "kobenhavn", :company/id "norr_food"},
         :cargo #:cargo{:id "goat_cheese"},
         :expires-in-mins 9,
         :distance 1239,
         :urgency 0},
   #:job{:id 105553124418080,
         :origin {:city/id "targu_mures", :company/id "rock_eater"},
         :destination {:city/id "cluj_napoca", :company/id "cemelt_fla"},
         :cargo #:cargo{:id "gravel"},
         :expires-in-mins 10,
         :distance 269,
         :urgency 2},
   #:job{:id 105553124416416,
         :origin {:city/id "calais", :company/id "stokes"},
         :destination {:city/id "london", :company/id "sellplan"},
         :cargo #:cargo{:id "milk"},
         :expires-in-mins 10,
         :distance 126,
         :urgency 2},
   #:job{:id 105553124418496,
         :origin {:city/id "calais", :company/id "nbfc"},
         :destination {:city/id "frankfurt", :company/id "fle"},
         :cargo #:cargo{:id "kerosene"},
         :expires-in-mins 270,
         :distance 673,
         :urgency 2},
   #:job{:id 105553124413920,
         :origin {:city/id "iasi", :company/id "imp_otel"},
         :destination {:city/id "targu_mures", :company/id "fallow"},
         :cargo #:cargo{:id "floorpanels"},
         :expires-in-mins 183,
         :distance 256,
         :urgency 1},
   #:job{:id 105553124414752,
         :origin {:city/id "iasi", :company/id "kaarfor"},
         :destination {:city/id "hunedoara", :company/id "lognstick"},
         :cargo #:cargo{:id "used_packag"},
         :expires-in-mins 47,
         :distance 475,
         :urgency 1},
   #:job{:id 105553124413712,
         :origin {:city/id "iasi", :company/id "imp_otel"},
         :destination {:city/id "pirdop", :company/id "st_roza_bg"},
         :cargo #:cargo{:id "hipresstank"},
         :expires-in-mins 263,
         :distance 717,
         :urgency 2},
   #:job{:id 105553124417040,
         :origin {:city/id "calais", :company/id "stokes"},
         :destination {:city/id "london", :company/id "posped"},
         :cargo #:cargo{:id "med_equip"},
         :expires-in-mins 8,
         :distance 127,
         :urgency 1},
   #:job{:id 105553124414960,
         :origin {:city/id "iasi", :company/id "kaarfor"},
         :destination {:city/id "bucuresti", :company/id "lognstick"},
         :cargo #:cargo{:id "empty_palet"},
         :expires-in-mins 121,
         :distance 377,
         :urgency 0},
   #:job{:id 105553124416208,
         :origin {:city/id "calais", :company/id "lkwlog"},
         :destination {:city/id "paris", :company/id "tradeaux"},
         :cargo #:cargo{:id "cheese"},
         :expires-in-mins 148,
         :distance 267,
         :urgency 0},
   #:job{:id 105553124416624,
         :origin {:city/id "calais", :company/id "stokes"},
         :destination {:city/id "southampton", :company/id "transinet"},
         :cargo #:cargo{:id "chem_sorb_c"},
         :expires-in-mins 65,
         :distance 238,
         :urgency 2},
   #:job{:id 105553124415376,
         :origin {:city/id "calais", :company/id "lkwlog"},
         :destination {:city/id "amsterdam", :company/id "lkwlog"},
         :cargo #:cargo{:id "milk"},
         :expires-in-mins 270,
         :distance 451,
         :urgency 2},
   #:job{:id 105553124414544,
         :origin {:city/id "iasi", :company/id "kaarfor"},
         :destination {:city/id "kosice", :company/id "lkwlog"},
         :cargo #:cargo{:id "used_plast"},
         :expires-in-mins 25,
         :distance 838,
         :urgency 0},
   #:job{:id 105553124418288,
         :origin {:city/id "calais", :company/id "nbfc"},
         :destination {:city/id "lehavre", :company/id "cont_port_fr"},
         :cargo #:cargo{:id "sodhydro"},
         :expires-in-mins 89,
         :distance 301,
         :urgency 2},
   #:job{:id 105553124415584,
         :origin {:city/id "calais", :company/id "lkwlog"},
         :destination {:city/id "rennes", :company/id "lisette_log"},
         :cargo #:cargo{:id "mtl_coil"},
         :expires-in-mins 271,
         :distance 594,
         :urgency 2},
   #:job{:id 105553124417872,
         :origin {:city/id "targu_mures", :company/id "rock_eater"},
         :destination {:city/id "krakow", :company/id "sanbuilders"},
         :cargo #:cargo{:id "marb_blck2"},
         :expires-in-mins 616,
         :distance 912,
         :urgency 0},
   #:job{:id 105553124414336,
         :origin {:city/id "iasi", :company/id "kaarfor"},
         :destination {:city/id "hunedoara", :company/id "lognstick"},
         :cargo #:cargo{:id "used_plast_c"},
         :expires-in-mins 141,
         :distance 475,
         :urgency 0},
   #:job{:id 105553124414128,
         :origin {:city/id "iasi", :company/id "imp_otel"},
         :destination {:city/id "bucuresti", :company/id "domdepo"},
         :cargo #:cargo{:id "wallpanels"},
         :expires-in-mins 24,
         :distance 413,
         :urgency 2},
   #:job{:id 105553124416832,
         :origin {:city/id "calais", :company/id "stokes"},
         :destination {:city/id "olbia", :company/id "c_navale"},
         :cargo #:cargo{:id "oil"},
         :expires-in-mins 313,
         :distance 1416,
         :urgency 0},
   #:job{:id 105553124417456,
         :origin {:city/id "targu_mures", :company/id "rock_eater"},
         :destination {:city/id "hunedoara", :company/id "lognstick"},
         :cargo #:cargo{:id "driller"},
         :expires-in-mins 148,
         :distance 234,
         :urgency 1}])

#_(pco/defresolver jobs-raw [{:economy/keys [companies game-time]}]
  {::pco/input  [:economy/companies :economy/game-time]
   ::pco/output [{:jobs/raw job-shape}]}
  {:jobs/raw
   (->> (for [company                          companies
              {[_ [_ cargo]] :cargo
               exp           :expiration-time
               distance      :shortest-distance-km
               :keys [target urgency]
               :as   order}                    (:job-offer company)
              :when (and (pos? exp)
                         (> exp game-time))]
          (let [[_ _ sender-company sender-city] (:sii/block-id company)
                [target-company target-city]     (str/split target #"\.")]
            {:job/id              (:sii/block-id order)
             :job/origin          {:city/id    sender-city
                                   :company/id sender-company}
             :job/destination     {:city/id    target-city
                                   :company/id target-company}
             :job/cargo           {:cargo/id   cargo}
             :job/expires-in-mins (- exp game-time)
             :job/distance        distance
             :job/urgency         urgency}))
        ;; XXX: Remove this debugging helper that shortens the returns.
        #_(take 20)
        vec
        #_pco/final-value)})

#_(pco/defresolver jobs-table [{jobs :jobs/raw}]
  {::pco/input  [:jobs/raw]
   ::pco/output [{:jobs/table job-shape}]}
  {:jobs/table
   (into {} (for [job jobs]
              [(:job/id job) job]))})

(pco/defresolver all-jobs [{jobs :economy/job-offers} _inputs]
  {::pco/output [{:jobs/all-available [:job/id]}]}
  {:jobs/all-available (mapv #(select-keys % [:job/id]) (vals jobs))})

#_(def jobs-table-sample
  {105553124415792
   #:job{:id 105553124415792,
         :origin {:city/id "calais", :company/id "lkwlog"},
         :destination {:city/id "kobenhavn", :company/id "norr_food"},
         :cargo #:cargo{:id "goat_cheese"},
         :expires-in-mins 9,
         :distance 1239,
         :urgency 0},
   105553124418080
   #:job{:id 105553124418080,
         :origin {:city/id "targu_mures", :company/id "rock_eater"},
         :destination {:city/id "cluj_napoca", :company/id "cemelt_fla"},
         :cargo #:cargo{:id "gravel"},
         :expires-in-mins 10,
         :distance 269,
         :urgency 2},
   105553124416416
   #:job{:id 105553124416416,
         :origin {:city/id "calais", :company/id "stokes"},
         :destination {:city/id "london", :company/id "sellplan"},
         :cargo #:cargo{:id "milk"},
         :expires-in-mins 10,
         :distance 126,
         :urgency 2},
   105553124418496
   #:job{:id 105553124418496,
         :origin {:city/id "calais", :company/id "nbfc"},
         :destination {:city/id "frankfurt", :company/id "fle"},
         :cargo #:cargo{:id "kerosene"},
         :expires-in-mins 270,
         :distance 673,
         :urgency 2},
   105553124413920
   #:job{:id 105553124413920,
         :origin {:city/id "iasi", :company/id "imp_otel"},
         :destination {:city/id "targu_mures", :company/id "fallow"},
         :cargo #:cargo{:id "floorpanels"},
         :expires-in-mins 183,
         :distance 256,
         :urgency 1},
   105553124414752
   #:job{:id 105553124414752,
         :origin {:city/id "iasi", :company/id "kaarfor"},
         :destination {:city/id "hunedoara", :company/id "lognstick"},
         :cargo #:cargo{:id "used_packag"},
         :expires-in-mins 47,
         :distance 475,
         :urgency 1},
   105553124413712
   #:job{:id 105553124413712,
         :origin {:city/id "iasi", :company/id "imp_otel"},
         :destination {:city/id "pirdop", :company/id "st_roza_bg"},
         :cargo #:cargo{:id "hipresstank"},
         :expires-in-mins 263,
         :distance 717,
         :urgency 2},
   105553124417040
   #:job{:id 105553124417040,
         :origin {:city/id "calais", :company/id "stokes"},
         :destination {:city/id "london", :company/id "posped"},
         :cargo #:cargo{:id "med_equip"},
         :expires-in-mins 8,
         :distance 127,
         :urgency 1},
   105553124414960
   #:job{:id 105553124414960,
         :origin {:city/id "iasi", :company/id "kaarfor"},
         :destination {:city/id "bucuresti", :company/id "lognstick"},
         :cargo #:cargo{:id "empty_palet"},
         :expires-in-mins 121,
         :distance 377,
         :urgency 0},
   105553124416208
   #:job{:id 105553124416208,
         :origin {:city/id "calais", :company/id "lkwlog"},
         :destination {:city/id "paris", :company/id "tradeaux"},
         :cargo #:cargo{:id "cheese"},
         :expires-in-mins 148,
         :distance 267,
         :urgency 0},
   105553124416624
   #:job{:id 105553124416624,
         :origin {:city/id "calais", :company/id "stokes"},
         :destination {:city/id "southampton", :company/id "transinet"},
         :cargo #:cargo{:id "chem_sorb_c"},
         :expires-in-mins 65,
         :distance 238,
         :urgency 2},
   105553124415376
   #:job{:id 105553124415376,
         :origin {:city/id "calais", :company/id "lkwlog"},
         :destination {:city/id "amsterdam", :company/id "lkwlog"},
         :cargo #:cargo{:id "milk"},
         :expires-in-mins 270,
         :distance 451,
         :urgency 2},
   105553124414544
   #:job{:id 105553124414544,
         :origin {:city/id "iasi", :company/id "kaarfor"},
         :destination {:city/id "kosice", :company/id "lkwlog"},
         :cargo #:cargo{:id "used_plast"},
         :expires-in-mins 25,
         :distance 838,
         :urgency 0},
   105553124418288
   #:job{:id 105553124418288,
         :origin {:city/id "calais", :company/id "nbfc"},
         :destination {:city/id "lehavre", :company/id "cont_port_fr"},
         :cargo #:cargo{:id "sodhydro"},
         :expires-in-mins 89,
         :distance 301,
         :urgency 2},
   105553124415584
   #:job{:id 105553124415584,
         :origin {:city/id "calais", :company/id "lkwlog"},
         :destination {:city/id "rennes", :company/id "lisette_log"},
         :cargo #:cargo{:id "mtl_coil"},
         :expires-in-mins 271,
         :distance 594,
         :urgency 2},
   105553124417872
   #:job{:id 105553124417872,
         :origin {:city/id "targu_mures", :company/id "rock_eater"},
         :destination {:city/id "krakow", :company/id "sanbuilders"},
         :cargo #:cargo{:id "marb_blck2"},
         :expires-in-mins 616,
         :distance 912,
         :urgency 0},
   105553124414336
   #:job{:id 105553124414336,
         :origin {:city/id "iasi", :company/id "kaarfor"},
         :destination {:city/id "hunedoara", :company/id "lognstick"},
         :cargo #:cargo{:id "used_plast_c"},
         :expires-in-mins 141,
         :distance 475,
         :urgency 0},
   105553124414128
   #:job{:id 105553124414128,
         :origin {:city/id "iasi", :company/id "imp_otel"},
         :destination {:city/id "bucuresti", :company/id "domdepo"},
         :cargo #:cargo{:id "wallpanels"},
         :expires-in-mins 24,
         :distance 413,
         :urgency 2},
   105553124416832
   #:job{:id 105553124416832,
         :origin {:city/id "calais", :company/id "stokes"},
         :destination {:city/id "olbia", :company/id "c_navale"},
         :cargo #:cargo{:id "oil"},
         :expires-in-mins 313,
         :distance 1416,
         :urgency 0},
   105553124417456
   #:job{:id 105553124417456,
         :origin {:city/id "targu_mures", :company/id "rock_eater"},
         :destination {:city/id "hunedoara", :company/id "lognstick"},
         :cargo #:cargo{:id "driller"},
         :expires-in-mins 148,
         :distance 234,
         :urgency 1}})

#_(def economy-bits-sample
  {:economy/game-time 762021
   :economy/time-zone 120
   :economy/time-zone-name "@@tz_cest@@"})

(def ^:private connect-parser? false)

(def economy-parsing-env
  (-> (pci/register [;; Profiles
                     latest-save profile profile-root profiles
                     ;; Economy
                     economy])
      #_(pci/register ets2/pathom-index)
      (assoc ::economy-cache (atom {}))
      (cond-> #_env connect-parser?
        (p.connector/connect-env {::pvc/parser-id `economy-parsing-env}))))

(def job-search-env
  (-> (pci/register [;; Sim Time
                     cest-time local-time time-zone-name
                     ;; Jobs
                     (pbir/env-table-resolver
                       :economy/job-offers :job/id
                       job-shape)
                     all-jobs])
      (pci/register ets2/pathom-index)
      (cond-> #_env connect-parser?
        (p.connector/connect-env {::pvc/parser-id `job-search-env}))))

(defn parse-economy [game profile-id]
  (p.eql/process
    economy-parsing-env
    {:game game
     :profile/id profile-id}
    [:economy/game-time
     :economy/time-zone
     :economy/time-zone-name
     :economy/job-offers]))

(defn job-search [game profile-id query]
  (p.eql/process
    (merge job-search-env (parse-economy game profile-id))
    query))

(comment
  (-> (job-search :ets2 "426973686F7032"
                  [:jobs/along-the-black-sea :jobs/captain :jobs/cattle-drive
                   :jobs/check-in-check-out :jobs/concrete-jungle
                   :jobs/exclave-transit :jobs/go-nuclear
                   :jobs/iberian-pilgrimage :jobs/industry-standard
                   :jobs/fleet-builder :jobs/lets-get-shipping
                   :jobs/like-a-farmer :jobs/michaelangelo :jobs/miner
                   :jobs/orient-express :jobs/sailor :jobs/taste-the-sun
                   :jobs/turkish-delight :jobs/whatever-floats-your-boat])))
