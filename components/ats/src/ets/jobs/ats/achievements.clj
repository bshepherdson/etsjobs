(ns ets.jobs.ats.achievements
  (:require
   [com.wsscode.pathom3.connect.operation :as-alias pco]
   [com.wsscode.pathom3.connect.indexes :as-alias pci]
   [datahike.api :as d]))

;; Trawling the achievements for their needs. frequencies maps? sets?
;; Some (eg. perfect deliveries, like Leave No Branch Behind!) need different
;; conditions for job searches and past deliveries.

;; I think the best plan is a set of multimethods, and derive for common logic.

;; Sets of sources, targets, cargo, or special routes:
;; Sea Dog, Cheers!, Gold Fever, Sky Harbor, "Heavy... China Shop",
;; I Thought This Should Be Heavy?!, Sky Delivery, Lumberjack,
;; Cabbage to Cabbage, Go Big or Go Home, Get (to) the Chopper!
;; One Two Three Breathe!, Big in America!, Your Dumper Has Arrived!,
;; Home Sweet Home, Steel Wings, Keep Sailing, Terminal Terminus, Logged In,
;; Leave No Branch Behind, This One Is Mine, Some Like It Salty,
;; Along the Snake River, Energy From Above**, Big Boy, Shoreside Delivery,
;; Farm Away, Vehicle Dealer, Agriculture Expert

;; Straight count:
;; Pump it up, Grown in Idaho, Gold Rush, Up and Away, Buffalo Bill

;; Frequencies:
;; - Big Wheels Keep On Turning: 3 Big Tyres from *both* tyre factories
;; - Air Capital of the World: Wing + Engine to/from every depot in Wichita

;; Consecutive total: 
;; - Bigger Cargo, Bigger Profit: $100k deliveries 5 in a row

;; Notes:
;; - Energy From Above: engine and nacelle to each of the sites.
;;     - Power On! is the same way

;; Special cases:
;; - Zero Waste: 10 deliveries to certain places, plus at least one garbage truck to or from
;; - Major Miner: Different directions and cargoes, but a simple set
;; - Cotton Bloom: 10 deliveries of a set of cargoes, *and* at least one of each.
;; - School Bus Capital: 5 Bus Hoods to + 5 School Buses from the factory in Tulsa
;; - Grain of Salt: 6 deliveries from Hutchinson Salt Mine; at least 2 to a Food Factory

(def ^:private job-pull
  (let [endpoint [{:location/city    [:city/ident :city/name {:city/state [:db/ident]}]}
                  {:location/company [:company/ident :company/name]}]]
    [{:job/source endpoint}
     {:job/target endpoint}
     {:job/cargo  [:cargo/ident :cargo/name {:cargo/adr [:db/ident]}]}
     :*]))

(def ^:private rules
  '[[(offer? ?job)
     [?job :offer/expiration-time _]]
    [(delivery? ?job)
     [?job :delivery/profit _]]
    ;; Implicitly a delivery, since it references :delivery/* things.
    [(perfect? ?job)
     [?job :delivery/fines 0]
     [?job :delivery/damage-ratio 0.0]
     [?job :delivery/remaining-time ?remaining]
     [(>= ?remaining 0)]]])

(defmulti relevant-jobs
  "Queries for relevant jobs - that is, job offers which are available and
  would contribute to the achievement."
  (fn [_db achievement] achievement))

(defmulti achievement-progress
  "Returns an achievement-specific data structure giving the progress.
  
  - Sets of sources (most common) get `{:completed #{...}, :needed #{...}}.
      - :needed is optional
  - Counts get `{:completed 5, :required 10}`
  - Frequencies get a map of `{key Count-structure}`
  - Special cases are special"
  (fn [_db achievement] achievement))

;; I Thought This Should Be Heavy ============================================
(def ^:private ach-should-be-heavy
  {:id      :i-thought-this-should-be-heavy
   :name    "I Thought This Should Be Heavy"
   :group   :group/heavy-cargo
   :desc    "Complete a delivery of all heavy cargoes in American Truck Simulator."})

(defn- heavy-cargoes [db]
  (set (d/q '[:find [?cargo ...] :where
              [?cargo :cargo/heavy? true]]
            db)))

(defmethod achievement-progress :i-thought-this-should-be-heavy
  [db _cheevo]
  (let [heavies (heavy-cargoes db) 
        done    (d/q '[:find [?cargo ...]
                       :in $ % :where
                       [?cargo :cargo/heavy? true]
                       [?job   :job/cargo    ?cargo]
                       (delivery? ?job)]
                     db rules)]
    {:type      :set/cargo
     :completed done
     :needed    (remove (set done) heavies)}))

(defmethod relevant-jobs :i-thought-this-should-be-heavy
  [db cheevo]
  (let [{:keys [needed]} (achievement-progress db cheevo)]
    (d/q '[:find [(pull ?job spec) ...]
           :in $ % spec [?needed ...] :where
           [?job :job/cargo ?needed]
           (offer? ?job)]
         db rules job-pull needed)))

;; Sea Dog ===================================================================
(def ^:private ach-sea-dog
  {:id      :sea-dog
   :name    "Sea Dog"
   :group   :state/ca
   :desc    "Deliver cargo to a port in Oakland and a port in San Francisco."})

(defmethod achievement-progress :sea-dog [db _cheevo]
  (let [ports     #{"oak_port" "sf_port"}
        delivered (d/q '[:find [?port ...]
                         :in $ % [?port ...] :where
                         [?company :company/ident    ?port]
                         [?loc     :location/company ?company]
                         [?job     :job/target       ?loc]
                         (delivery? ?job)]
                       db rules ports)]
    {:type      :set/cargo
     :completed delivered
     :needed    (remove (set delivered) ports)}))

(defmethod relevant-jobs :sea-dog [db cheevo]
  (let [{:keys [needed]} (achievement-progress db cheevo)]
    (d/q '[:find [(pull ?job spec) ...]
           :in $ % spec [?needed ...] :where
           [?company :company/ident    ?needed]
           [?loc     :location/company ?company]
           [?job     :job/target       ?loc]
           (offer? ?job)]
         db rules job-pull needed)))

;; Cheers! ===================================================================
;; START HERE - actually I should work out this model end to end before continuing further.
;; Pick some group - Montana? Wyoming? where I have some achievements completed and some
;; not. Implement them all, then follow that thread all the way to the UI.

(def ^:private achievements-ca
  [;; California
   {:name    "Sea Dog"
    :region  :state/ca
    :desc    "Deliver cargo to a port in Oakland and port in San Francisco."}
    
   {:id    :cheers
    :name  "Cheers"
    :region {:region/id "CA"}
    :desc   "Deliver cargo from all 3 vineyards in California."
    :flag   :job.cheevo/cheers}])

;; California
#_(defachievement cheers
  "Deliver cargo from all 3 vineyards in California."
  {::pco/input [{:job/origin [:company/id]}]}
  (fn [_env {{sender :company/id} :job/origin}]
    {:job.cheevo/cheers (= sender "darchelle_uzau")}))

;; Nevada
#_(defachievement gold-fever
  "Deliver cargo to both quarries in Nevada."
  {::pco/input [{:job/destination [:city/id :company/id]}]}
  (fn [_env {{destination :city/id, recipient :company/id} :job/destination}]
    {:job.cheevo/gold-fever (and (#{"elko" "carson_city"} destination)
                                 (#{"cm_min_qry" "cm_min_qryp"} recipient))}))

;; Arizona
#_(defachievement sky-harbor
  "Deliver cargo to the Phoenix Airport."
  {::pco/input [{:job/destination [:company/id]}]}
  (fn [_env {{recipient :company/id} :job/destination}]
    {:job.cheevo/sky-harbor (= recipient "aport_phx")}))

;; New Mexico
#_(defachievement sky-delivery
  "Deliver cargo to An-124 depot."
  {::pco/input [{:job/destination [:company/id]}]}
  (fn [_env {{recipient :company/id} :job/destination}]
    {:job.cheevo/sky-delivery (= recipient "aport_ult")}))

;; Oregon
#_(defachievement lumberjack
  "Deliver cargo from all timber harvest sites in Oregon."
  {::pco/input [{:job/origin [:company/id :country/id]}]}
  (fn [_env {{sender :company/id, state :country/id} :job/origin}]
    {:job.cheevo/lumberjack (and (= state  "OR")
                                 (= sender "dg_wd_hrv"))}))

#_(defachievement cabbage-to-cabbage
  "Carry cabbage (vegetables) over Cabbage Hill."
  {::pco/input [{:job/origin      [:country/id]}
                {:job/destination [:country/id]}
                {:job/cargo       [:cargo/id]}]}
  (fn [_env {{origin :country/id}      :job/origin
             {destination :country/id} :job/destination
             {cargo :cargo/id}         :job/cargo}]
    {:job.cheevo/cabbage-to-cabbage (and (#{"frozen_veget" "vegetable"} cargo)
                                         (= "OR" origin)
                                         (= "OR" destination))}))

;; Washington
#_(defachievement steel-wings
  "Deliver to an aerospace company in Washington."
  {::pco/input [{:job/destination [:company/id]}]}
  (fn [_env {{recipient :company/id} :job/destination}]
    {:job.cheevo/steel-wings (= recipient "dw_air_pln")}))

#_(defachievement keep-sailing
  "Deliver a boat to a marina in Washington."
  {::pco/input [{:job/origin      [:country/id]}
                {:job/destination [:country/id]}
                {:job/cargo       [:cargo/id]}]}
  (fn [_env {{cargo     :cargo/id}   :job/cargo
             {state     :country/id
              recipient :company/id} :job/destination}]
    {:job.cheevo/keep-sailing (and (= "boat" cargo)
                                   (= "sh_shp_mar" recipient)
                                   (= "WA" state))}))

#_(defachievement terminal-terminus
  "Deliver to both port terminals in Washington."
  {::pco/input [{:job/destination [:company/id]}]}
  (fn [_env {{recipient :company/id} :job/destination}]
    {:job.cheevo/terminal-terminus (boolean (#{"port_sea" "port_tac"} recipient))}))

#_(defachievement over-the-top
  "Drive through the forest road to timber harvest in Bellingham."
  {::pco/input [{:job/origin      [:city/id :company/id]}
                {:job/destination [:city/id :company/id]}]}
  (fn [{{origin      :city/id, sender    :company/id} :job/origin
        {destination :city/id, recipient :company/id} :job/destination}]
    {:job.cheevo/over-the-top (and (or (= origin      "bellingham")
                                       (= destination "bellingham"))
                                   (or (= sender      "dg_wd_hrv")
                                       (= recipient   "dg_wd_hrv")))}))

;; Utah
#_(defachievement this-one-is-mine
  "Visit all mines and quarries in Utah."
  {::pco/input [{:job/origin      [:city/id :company/id]}
                {:job/destination [:city/id :company/id]}]}
  (fn [{{origin      :city/id, sender    :company/id} :job/origin
        {destination :city/id, recipient :company/id} :job/destination}]
    (let [sites #{"salt_lake" "cedar_city" "vernal"}
          locs  #{"cm_min_qry" "cm_min_qryp"}]
      {:job.cheevo/this-one-is-mine
       (boolean (and (or (sites origin) (sites destination))
                     (or (locs sender)  (locs recipient))))})))

#_(defachievement some-like-it-salty
  "Take a job from each branch of each company located in Salt Lake City."
  {::pco/input [{:job/origin [:city/id]}]}
  (fn [_env {{origin :city/id} :job/origin}]
    {:job.cheevo/some-like-it-salty (= origin "salt_lake")}))

#_(defachievement pump-it-up
  "Deliver 5 frac tank trailers to any oil drilling site in Utah."
  {::pco/input [{:job/destination [:company/id :country/id]}
                {:job/cargo       [:cargo/id]}]}
  (fn [_env {{cargo :cargo/id} :job/cargo
             {destination :country/id
              recipient   :company/id} :job/destination}]
    {:job.cheevo/pump-it-up (and (= cargo       "frac_tank")
                                 (= recipient   "gal_oil_sit")
                                 (= destination "UT"))}))

;; Idaho
#_(defachievement grown-in-idaho
  "Complete 5 deliveries of potatoes from Idaho farms."
  {::pco/input [{:job/origin [:company/id :country/id]}
                {:job/cargo  [:cargo/id]}]}
  (fn [_env {{cargo :cargo/id}    :job/cargo
             {origin :country/id
              sender :company/id} :job/origin}]
    {:job.cheevo/grown-in-idaho (and (= cargo  "potatoes")
                                     (= sender "sc_frm")
                                     (= origin "ID"))}))

(def ^:private snake-river-pairs
  (let [fwd [["kennewick" "lewiston"]
             ["boise" "twin_falls"]
             ["twin_falls" "pocatello"]
             ["pocatello"  "idaho_falls"]]]
   (into (set fwd)
         (comp (map reverse)
               (map vec))
         fwd)))

#_(defachievement along-the-snake-river
  "Complete PERFECT deliveries between Kennewick-Lewiston, Boise-Twin Falls, Twin Falls-Pocatello, Pocatello-Idaho Falls; any order or direction."
  {::pco/input [{:job/origin      [:city/id]}
                {:job/destination [:city/id]}]}
  (fn [_env {{origin      :city/id} :job/origin
             {destination :city/id} :job/destination}]
    {:job.cheevo/along-the-snake-river
     (boolean (snake-river-pairs [origin destination]))}))

;; Colorado
#_(defachievement energy-from-above
  "Deliver a tower and nacelle to both Vitas Power wind turbine construction sites in Colorado."
  {::pco/input [{:job/cargo     [:cargo/id]}
                {:job/destination [:company/id :country/id]}]}
  (fn [_env {{cargo       :cargo/id}   :job/cargo
             {destination :country/id
              recipient   :company/id} :job/destination}]
    {:job/cheevo/energy-from-above
     (boolean (and (#{"windml_eng" "windml_tube"} cargo)
                   (= destination "CO")
                   (= recipient "vp_epw_sit")))}))

(defn- co-mining [{state   :country/id
                   company :company/id}]
  (boolean (and (#{"nmq_min_qry" "nmq_min_qrys"} company)
                (= "CO" state))))

#_(defachievement gold-rush
  "Deliver 10 loads to or from the NAMIQ company at the gold mine in Colorado."
  {::pco/input [{:job/origin      [:company/id :country/id]}
                {:job/destination [:company/id :country/id]}]}
  (fn [_env {:job/keys [origin destination]}]
    {:job.cheevo/gold-rush (or (co-mining origin)
                               (co-mining destination))}))

#_(defachievement up-and-away
  "Complete 10 delivery to Denver airport."
  {::pco/input [{:job/destination [:company/id]}]}
  (fn [_env {{recipient :company/id} :job/destination}]
    {:job.cheevo/up-and-away (= "aport_den" recipient)}))

;; Wyoming
(defn- big-boy-site [{state   :country/id
                      company :company/id}]
  (and (= "WY" state)
       (= "aml_rail_str" company)))

;; Big Boy ===================================================================
(def ^:private ach-big-boy
  {:id    :big-boy
   :name  "Big Boy"
   :group :state/wy
   :desc  "Deliver train parts, tamping machine and rails to or from the rail yard in Cheyenne."})

(defn- pull-cargoes [db ids]
  (d/pull-many db '[:db/id :cargo/name :cargo/ident] ids))

(defmethod achievement-progress :big-boy
  [db _cheevo]
  (let [slugs   ["train_part" "tamp_machine" "rails"]
        cargoes (d/q '[:find [?cargo ...]
                       :in $ [?slug ...] :where
                       [?cargo :cargo/ident ?slug]]
                     db slugs)
        done    (d/q '[:find [?cargo ...]
                       :in $ % [?slug ...] [?dir ...] :where
                       [?loc   :location/city    [:city/ident "cheyenne"]]
                       [?loc   :location/company [:company/ident "aml_rail_str"]]
                       [?job   ?dir              ?loc]
                       [?job   :job/cargo        ?cargo]
                       [?cargo :cargo/ident      ?slug]
                       (delivery? ?job)
                       [?cargo :cargo/name       ?cargo-name]]
                     db rules slugs [:job/source :job/target])]
    {:type      :set/cargo
     :completed (pull-cargoes db done)
     :needed    (pull-cargoes db (remove (set done) cargoes))}))

(defmethod relevant-jobs :big-boy
  [db cheevo]
  (let [needed (->> (achievement-progress db cheevo)
                    :needed
                    (map :db/id))]
    (d/q '[:find [(pull ?job pattern) ...]
           :in $ % pattern [?cargo ...] [?dir ...] :where
           [?job   :job/cargo ?cargo]
           (offer? ?job)
           [?job   ?dir       ?loc]
           [?loc   :location/city    [:city/ident "cheyenne"]]
           [?loc   :location/company [:company/ident "aml_rail_str"]]]
         db rules job-pull needed [:job/source :job/target])))

;; Buffalo Bill ==============================================================
(def ^:private ach-buffalo-bill
  {:id    :buffalo-bill
   :name  "Buffalo Bill"
   :group :state/wy
   :desc  (str "Complete 10 PERFECT cattle deliveries (no damage, no fines, "
               "in-time) to livestock auctions in Wyoming.")})

(def ^:private buffalo-bill-rules
  (concat rules
          '[[(buffalo-bill? ?job)
             [?job  :job/cargo        [:cargo/ident "cattle"]]
             [?job  :job/target       ?tgt]
             [?tgt  :location/company [:company/ident "bn_live_auc"]]
             [?tgt  :location/city    ?city]
             [?city :city/state       :state/wy]]]))

(defmethod achievement-progress :buffalo-bill
  [db _cheevo]
  {:type      :count
   :completed (d/q '[:find (count ?job) .
                     :in $ % :where
                     (buffalo-bill? ?job)
                     (perfect? ?job)]
                   db buffalo-bill-rules)
   :needed    10})

(defmethod relevant-jobs :buffalo-bill [db _cheevo]
  (d/q '[:find [(pull ?job pattern) ...]
         :in $ % pattern :where
         (buffalo-bill? ?job)
         (offer? ?job)]
       db buffalo-bill-rules job-pull))

(comment
  (let [db     (d/db ets.jobs.search.core/conn) 
        cheevo :buffalo-bill]
    #_(achievement-progress db cheevo)
    (relevant-jobs        db cheevo))
  )

(def achievement-groups
  [{:group   :state/wy
    :name    "Wyoming"
    :cheevos [ach-big-boy
              ach-buffalo-bill]}])

#_(def ^:private achievements-list
  [;; California
   {:cheevo/id    :sea-dog
    :cheevo/name  "Sea Dog"
    :cheevo/region {:region/id "CA"}
    :cheevo/desc   "Deliver cargo to a port in Oakland and port in San Francisco."
    :cheevo/flag   :job.cheevo/sea-dog}
   {:cheevo/id    :cheers
    :cheevo/name  "Cheers"
    :cheevo/region {:region/id "CA"}
    :cheevo/desc   "Deliver cargo from all 3 vineyards in California."
    :cheevo/flag   :job.cheevo/cheers}

   ;; Nevada
   {:cheevo/id    :gold-fever
    :cheevo/name  "Gold Fever"
    :cheevo/region {:region/id "NV"}
    :cheevo/desc   "Deliver cargo to both quarries in Nevada."
    :cheevo/flag   :job.cheevo/gold-fever}

   ;; Arizona
   {:cheevo/id    :sky-harbor
    :cheevo/name  "Sky Harbor"
    :cheevo/region {:region/id "AZ"}
    :cheevo/desc   "Deliver cargo to the Phoenix Airport."
    :cheevo/flag   :job.cheevo/sky-harbor}

   ;; New Mexico
   {:cheevo/id    :sky-delivery
    :cheevo/name  "Sky Delivery"
    :cheevo/region {:region/id "NM"}
    :cheevo/desc   "Deliver cargo to An-124 depot."
    :cheevo/flag   :job.cheevo/sky-delivery}
   
   ;; Oregon
   {:cheevo/id    :lumberjack
    :cheevo/name  "Lumberjack"
    :cheevo/region {:region/id "OR"}
    :cheevo/desc   "Deliver cargo from all timber harvest sites in Oregon."
    :cheevo/flag   :job.cheevo/lumberjack}
   {:cheevo/id    :cabbage-to-cabbage
    :cheevo/name  "Cabbage to Cabbage"
    :cheevo/region {:region/id "OR"}
    :cheevo/desc   "Carry cabbage (vegetables) over Cabbage Hill."
    :cheevo/flag   :job.cheevo/cabbage-to-cabbage}

   ;; Washington
   {:cheevo/id    :steel-wings
    :cheevo/name  "Steel Wings"
    :cheevo/region {:region/id "WA"}
    :cheevo/desc   "Deliver to an aerospace company in Washington."
    :cheevo/flag   :job.cheevo/steel-wings}
   {:cheevo/id    :keep-sailing
    :cheevo/name  "Keep Sailing"
    :cheevo/region {:region/id "WA"}
    :cheevo/desc   "Deliver a boat to a marina in Washington."
    :cheevo/flag   :job.cheevo/keep-sailing}
   {:cheevo/id    :terminal-terminus
    :cheevo/name  "Terminal Terminus"
    :cheevo/region {:region/id "WA"}
    :cheevo/desc   "Deliver to both port terminals in Washington."
    :cheevo/flag   :job.cheevo/terminal-terminus}
   {:cheevo/id    :over-the-top
    :cheevo/name  "Over the Top"
    :cheevo/region {:region/id "WA"}
    :cheevo/desc   "Drive through the forest road to timber harvest in Bellingham."
    :cheevo/flag   :job.cheevo/over-the-top}
   
   ;; Utah
   {:cheevo/id    :this-one-is-mine
    :cheevo/name  "This One is Mine"
    :cheevo/region {:region/id "UT"}
    :cheevo/desc   "Visit all mines and quarries in Utah."
    :cheevo/flag   :job.cheevo/this-one-is-mine}
   {:cheevo/id    :some-like-it-salty
    :cheevo/name  "Some Like it Salty"
    :cheevo/region {:region/id "UT"}
    :cheevo/desc   "Take a job from each branch of each company located in Salt Lake City."
    :cheevo/flag   :job.cheevo/some-like-it-salty}
   {:cheevo/id    :pump-it-up
    :cheevo/name  "Pump It Up"
    :cheevo/region {:region/id "UT"}
    :cheevo/desc   "Deliver 5 frac tank trailers to any oil drilling site in Utah."
    :cheevo/flag   :job.cheevo/pump-it-up}

   ;; Idaho
   {:cheevo/id    :grown-in-idaho
    :cheevo/name  "Grown in Idaho"
    :cheevo/region {:region/id "ID"}
    :cheevo/desc   "Complete 5 deliveries of potatoes from Idaho farms."
    :cheevo/flag   :job.cheevo/grown-in-idaho}
   {:cheevo/id    :along-the-snake-river
    :cheevo/name  "Along the Snake River"
    :cheevo/region {:region/id "ID"}
    :cheevo/desc   "Complete PERFECT deliveries between Kennewick-Lewiston, Boise-Twin Falls, Twin Falls-Pocatello, Pocatello-Idaho Falls; any order or direction."
    :cheevo/flag   :job.cheevo/along-the-snake-river}
   
   ;; Colorado
   {:cheevo/id    :energy-from-above
    :cheevo/name  "Energy From Above"
    :cheevo/region {:region/id "CO"}
    :cheevo/desc   "Deliver a tower and nacelle to both Vitas Power wind turbine construction sites in Colorado."
    :cheevo/flag   :job.cheevo/energy-from-above}
   {:cheevo/id    :gold-rush
    :cheevo/name  "Gold Rush"
    :cheevo/region {:region/id "CO"}
    :cheevo/desc   "Deliver 10 loads to or from the NAMIQ company at the gold mine in Colorado."
    :cheevo/flag   :job.cheevo/gold-rush}
   {:cheevo/id    :up-and-away
    :cheevo/name  "Up and Away"
    :cheevo/region {:region/id "CO"}
    :cheevo/desc   "Complete 10 delivery to Denver airport."
    :cheevo/flag   :job.cheevo/up-and-away}

   ;; Wyoming
   {:cheevo/id    :big-boy
    :cheevo/name  "Big Boy"
    :cheevo/region {:region/id "WY"}
    :cheevo/desc   "Deliver train parts, tamping machine and rails to or from the rail yard in Cheyenne."
    :cheevo/flag   :job.cheevo/big-boy}
   {:cheevo/id    :buffalo-bill
    :cheevo/name  "Buffalo Bill"
    :cheevo/region {:region/id "WY"}
    :cheevo/desc   "Complete 10 PERFECT cattle deliveries to livestock auctions in Wyoming."
    :cheevo/flag   :job.cheevo/buffalo-bill}])

#_(def ^:private all-achievements
  (pbir/constantly-resolver
    :achievements/all
    (mapv #(select-keys % [:cheevo/id]) achievements-list)))

#_(def ^:private achievements-by-id
  (util/table-indexed-by achievements-list :cheevo/id))

#_(def achievement-job-flags
  (->> achievements-list
       (map :cheevo/flag)
       sort
       vec))

#_(def ^:private regions
  (->> achievements-list
       (map :cheevo/region)
       set
       vec
       (pbir/constantly-resolver :regions/all)))

#_(def ^:private achievements-by-region
  (group-by (comp :region/id :cheevo/region) achievements-list))

#_(def ^:private region-table
  (->> (for [[state label] [["AZ" "Arizona"] 
                            ["CA" "California"]
                            ["CO" "Colorado"]
                            ["ID" "Idaho"] 
                            ["NM" "New Mexico"] 
                            ["NV" "Nevada"]
                            ["OR" "Oregon"] 
                            ["UT" "Utah"] 
                            ["WA" "Washington"] 
                            ["WY" "Wyoming"]]]
         [state {:region/name         label
                 :region/achievements (mapv :cheevo/id (achievements-by-region state))}])
       (into {})
       (pbir/static-table-resolver :region/id)))

#_(def index
  (pci/register
    [;; Top level
     all-achievements
     achievements-by-id
     regions region-table
     ;; Cheevo predicates
     cabbage-to-cabbage
     cheers
     gold-fever
     grown-in-idaho
     keep-sailing
     lumberjack
     over-the-top
     pump-it-up
     sea-dog
     sky-delivery
     sky-harbor
     some-like-it-salty
     steel-wings
     terminal-terminus
     this-one-is-mine]))
