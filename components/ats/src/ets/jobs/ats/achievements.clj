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

(def ^:private loc-pull
  [:db/id
   {:location/city    [:city/name]}
   {:location/company [:company/name]}])

(def ^:private cargo-pull
  '[:db/id :cargo/name :cargo/ident])

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

(defmulti achievement-info
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
  (fn [_db achievement] achievement))

;; ===========================================================================
;; |                                                                         |
;; |                          Heavy Cargo Pack                               |
;; |                                                                         |
;; ===========================================================================

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

(defmethod achievement-info :i-thought-this-should-be-heavy
  [db _cheevo]
  (let [heavies (heavy-cargoes db) 
        done    (d/q '[:find [?cargo ...]
                       :in $ % :where
                       [?cargo :cargo/heavy? true]
                       [?job   :job/cargo    ?cargo]
                       (delivery? ?job)]
                     db rules)
        needed  (remove (set done) heavies)]
    {:progress {:type      :set/cargo
                :completed (d/pull-many db cargo-pull done)
                :needed    (d/pull-many db cargo-pull needed)}
     :jobs     (d/q '[:find [(pull ?job spec) ...]
                      :in $ % spec [?needed ...] :where
                      [?job :job/cargo ?needed]
                      (offer? ?job)]
                    db rules job-pull needed)}))

;; Heavy, but not a Bull in a China Shop =====================================
(def ^:private ach-heavy-but-not-a-bull-in-a-china-shop
  {:id      :heavy-but-not-a-bull-in-a-china-shop
   :name    "Heavy, but not a Bull in a China Shop"
   :group   :group/heavy-cargo
   :desc    "Complete a PERFECT delivery of a heavy cargo which is at least 1000 miles long."})

(def ^:private thousand-miles-in-km
  "1000 mi = 1609.34 km"
  1609)

(defmethod achievement-info :heavy-but-not-a-bull-in-a-china-shop
  [db _cheevo]
  (let [cnt (d/q '[:find (count ?job) .
                   :in $ % ?thousand-miles :where
                   [?cargo :cargo/heavy? true]
                   [?job   :job/cargo    ?cargo]
                   (perfect? ?job)
                   [?job   :job/distance-km ?distance]
                   [(> ?distance ?thousand-miles)]] ;; 1000 mi = 1609.34 km
                 db rules thousand-miles-in-km)
        needed (if (zero? cnt) 1 0)]
    {:progress {:type      :count
                :completed cnt
                :total     1}
     :jobs     (when (pos? needed)
                 (d/q '[:find [(pull ?job pattern) ...]
                        :in $ % pattern ?thousand-miles :where
                        [?cargo :cargo/heavy? true]
                        [?job   :job/cargo    ?cargo]
                        (offer? ?job)
                        [?job   :job/distance-km ?distance]
                        [(> ?distance ?thousand-miles)]]
                      db rules job-pull thousand-miles-in-km))}))

;; Bigger Cargo, Bigger Profit ===============================================
;; TODO: Implement this one. It's a pain to check its status.
#_(def ^:private ach-bigger-cargo-bigger-profit
  {:id      :bigger-cargo-bigger-profit
   :name    "Bigger Cargo, Bigger Profit"
   :group   :group/heavy-cargo
   :desc    "Earn $100,000 on 5 consecutive heavy cargo deliveries."})

#_(defmethod achievement-progress :bigger-cargo-bigger-profit
  [db _cheevo]
  (let [last-5 (->> (d/q '[:find (max 5 ?end-time) . :where
                           [?job :delivery/end-time ?end-time]]
                         db)
                    (d/pull-many job-pull)
                    reverse)]
    last-5))

;; ===========================================================================
;; |                                                                         |
;; |                              California                                 |
;; |                                                                         |
;; ===========================================================================

;; Sea Dog ===================================================================
(def ^:private ach-sea-dog
  {:id      :sea-dog
   :name    "Sea Dog"
   :group   :state/ca
   :desc    "Deliver cargo to a port in Oakland and a port in San Francisco."})

;; TODO: Replace this with calls to the more flexible `deliver-to-all`
(defn- deliver-to-each-company [db company-slugs]
  (let [ports     (d/q '[:find [?loc ...]
                         :in $ [?port ...] :where
                         [?company :company/ident    ?port]
                         [?loc     :location/company ?company]]
                       db company-slugs)
        delivered (d/q '[:find [?loc ...]
                         :in $ % [?loc ...] :where
                         [?job :job/target ?loc]
                         (delivery? ?job)]
                       db rules ports)
        needed    (remove (set delivered) ports)]
    {:progress {:type      :set/company
                :completed (d/pull-many db loc-pull (set delivered))
                :needed    (d/pull-many db loc-pull needed)}
     :jobs     (d/q '[:find [(pull ?job spec) ...]
                      :in $ % spec [?needed ...] :where
                      [?job :job/target ?needed]
                      (offer? ?job)]
                    db rules job-pull needed)}))

(defmethod achievement-info :sea-dog [db _cheevo]
  (deliver-to-each-company db ["oak_port" "sf_port"]))

;; Cheers! ===================================================================
(def ^:private ach-cheers
  {:id    :cheers
   :name  "Cheers!"
   :group  :state/ca
   :desc   "Deliver cargo from all 3 vineyards in California."})

(defn- visit-all
  "Helper for achievements that visit a set of locations (cities, etc.) 

  Unless you have a complex `job-rule` rule (eg. specific cargo), you probably
  want to call the simpler `deliver-from-all` and `deliver-to-all`.
  
  - `loc-rule` is a rule `(location ?loc)` that finds the set of locations.
  - `job-rule` is a rule `(match ?job ?loc)` that (given one of the above `?loc`s)
    returns the valid jobs (offers or deliveries are filtered outside the rule!)"
  [db progress-type loc-rule job-rule]
  (let [va-rules  (conj rules loc-rule job-rule)
        delivered (d/q '[:find [?loc ...]
                         :in $ % :where
                         (location ?loc)
                         (match ?job ?loc)
                         (delivery? ?job)]
                       db va-rules)
        all-locs  (d/q '[:find [?loc ...] :in $ % :where (location ?loc)]
                       db va-rules)
        needed    (remove (set delivered) all-locs)]
    {:progress {:type      progress-type
                :completed (d/pull-many db loc-pull delivered)
                :needed    (d/pull-many db loc-pull needed)}
     :jobs     (d/q '[:find [(pull ?job pattern) ...]
                      :in $ % pattern [?needed ...] :where
                      (match ?job ?needed)
                      (offer? ?job)]
                    db va-rules job-pull needed)}))

(defn- deliver-to-all [db progress-type loc-rule]
  (visit-all db progress-type loc-rule '[(match ?job ?loc)
                                         [?job :job/target ?loc]]))

(defn- deliver-from-all [db progress-type loc-rule]
  (visit-all db progress-type loc-rule '[(match ?job ?loc)
                                         [?job :job/source ?loc]]))

(defn- deliver-to-or-from-all [db progress-type loc-rule]
  (visit-all db progress-type loc-rule '[(match ?job ?loc)
                                         (or [?job :job/source ?loc]
                                             [?job :job/target ?loc])]))

(defmethod achievement-info :cheers [db _cheevo]
  (visit-all db :set/city
             '[(location ?loc)
               [?company :company/ident    "du_farm"]
               [?loc     :location/company ?company]
               [?loc     :location/city    ?city]
               [?city    :city/state       :state/ca]]
             '[(match ?job ?loc)
               [?job :job/source ?loc]]))


;; ===========================================================================
;; |                                                                         |
;; |                                Nevada                                   |
;; |                                                                         |
;; ===========================================================================

;; Gold Fever ================================================================
(def ^:private ach-gold-fever
  {:id    :gold-fever
   :name  "Gold Fever"
   :group  :state/nv
   :desc   "Deliver cargo to both quarries in Nevada."})

(defmethod achievement-info :gold-fever [db _cheevo]
  (deliver-to-all db :set/city
                  '[(location ?loc)
                    [(ground ["elko" "carson_city"]) [?city-slug ...]]
                    [?city :city/ident       ?city-slug]
                    [?loc  :location/city    ?city]
                    [?loc  :location/company [:company/ident "cm_min_qry"]]]))


;; ===========================================================================
;; |                                                                         |
;; |                                Arizona                                  |
;; |                                                                         |
;; ===========================================================================

(defn- counted-deliveries
  [db total rule]
  (let [sd-rules (conj rules rule)
        matches  (or (d/q '[:find (count ?job) .
                             :in $ % :where
                             (match ?job)
                             (delivery? ?job)]
                           db sd-rules)
                      0)]
     {:progress {:type      :count
                 :total     total
                 :completed (min total matches)}
      ;; TODO: Watch this one - I haven't tested it out.
      :jobs     (when (< matches total)
                  (d/q '[:find [(pull ?job pattern) ...]
                         :in $ % pattern :where
                         (match ?job)
                         (offer? ?job)]
                       db sd-rules job-pull))}))

(defn- single-delivery [db rule]
  (counted-deliveries db 1 rule))

(defn- single-delivery-to
  [db company-slug]
  (let [base-rule '[(match ?job)
                    [?company :company/ident    "dummy"]
                    [?loc     :location/company ?company]
                    [?job     :job/target       ?loc]]]
    (single-delivery db (assoc-in base-rule [1 2] company-slug))))

;; Sky Harbor ================================================================
(def ^:private ach-sky-harbor
  {:id    :sky-harbor
   :name  "Sky Harbor"
   :group :state/az
   :desc  "Deliver cargo to the Phoenix Airport."})

(defmethod achievement-info :sky-harbor [db _cheevo]
  (single-delivery-to db "aport_phx"))

;; ===========================================================================
;; |                                                                         |
;; |                              New Mexico                                 |
;; |                                                                         |
;; ===========================================================================

;; Sky Delivery ==============================================================
(def ^:private ach-sky-delivery
  {:id    :sky-delivery
   :name  "Sky Delivery"
   :group :state/nm
   :desc  "Deliver cargo to An-124 depot."})

(defmethod achievement-info :sky-delivery [db _cheevo]
  (single-delivery-to db "aport_ult"))

;; ===========================================================================
;; |                                                                         |
;; |                                Oregon                                   |
;; |                                                                         |
;; ===========================================================================

;; Lumberjack ================================================================
(def ^:private ach-lumberjack
  {:id    :lumberjack
   :name  "Lumberjack"
   :group :state/or
   :desc  "Deliver cargo from all timber harvest sites in Oregon."})

(defmethod achievement-info :lumberjack [db _cheevo]
  (deliver-from-all
    db :set/city
    '[(location ?loc)
      [(ground ["astoria" "bend" "medford" "newport" "salem"]) [?city-slug ...]]
      [?city :city/ident       ?city-slug]
      [?loc  :location/city    ?city]
      [?loc  :location/company [:company/ident "dg_wd_hrv"]]]))

;; Cabbage to Cabbage ========================================================
(def ^:private ach-cabbage-to-cabbage
  {:id    :cabbage-to-cabbage
   :name  "Cabbage to Cabbage"
   :group :state/or
   :desc  (str "Carry cabbage (vegetables) over Cabbage Hill. "
               "(Cabbage Hill is on I-84 east of Pendleton; these jobs are the "
               "right cargo and either endpoint anywhere in Oregon.)")})

(defmethod achievement-info :cabbage-to-cabbage [db _cheevo]
  (let [cabbage-rules (conj rules
                            '[(cabbage ?job)
                              (or [?cargo :cargo/ident "frozen_veget"]
                                  [?cargo :cargo/ident "vegetable"])
                              [?job :job/cargo ?cargo]
                              (or [?job :job/source ?loc]
                                  [?job :job/target ?loc])
                              [?loc :location/city ?city]
                              [?city :city/state :state/or]])
        done           (or (d/q '[:find (count ?job) .
                                  :in $ % :where
                                  (cabbage ?job)
                                  (delivery? ?job)]
                                db cabbage-rules)
                           0)]
    {:progress {:type      :count
                :total     1
                :completed (min 1 done)}
     :jobs     (when (zero? done)
                 (d/q '[:find [(pull ?job pattern) ...]
                        :in $ % pattern :where
                        (cabbage ?job)
                        (offer? ?job)]
                      db cabbage-rules job-pull))}))

(comment
  (let [db     (d/db ets.jobs.search.core/conn) 
        cheevo :cheers]
    (->> (d/q '[:find ?city-name :where
                [?city :city/state    :state/ut]
                [?city :city/name     ?city-name]
                [?loc  :location/city ?city]
                [?loc  :location/company ?cmp]
                [?cmp  :company/ident    "gal_oil_sit"]
                ]
              db)
         sort)
    )
  ; elko -> cm_min_qry
  )

;; ===========================================================================
;; |                                                                         |
;; |                              Washington                                 |
;; |                                                                         |
;; ===========================================================================

;; Steel Wings ===============================================================
(def ^:private ach-steel-wings
  {:id    :steel-wings
   :name  "Steel Wings"
   :group :state/wa
   :desc  "Deliver to an aerospace company in Washington."})

;; TODO: Test this one properly shows jobs - nothing in the current save.
(defmethod achievement-info :steel-wings [db _cheevo]
  (single-delivery-to db "dw_air_pln"))

;; Keep Sailing ==============================================================
(def ^:private ach-keep-sailing
  {:id    :keep-sailing
   :name  "Keep Sailing"
   :group :state/wa
   :desc  "Deliver a boat to a marina in Washington."})

;; TODO: Test this one properly; no jobs in the current save.
(defmethod achievement-info :keep-sailing [db _cheevo]
  (single-delivery db '[(match ?job)
                        [?cargo :cargo/ident      "boat"]
                        [?job   :job/cargo        ?cargo]
                        [?job   :job/target       ?loc]
                        [?loc   :location/company [:company/ident "sh_shp_mar"]]
                        [?loc   :location/city    ?city]
                        [?city  :city/state       :state/wa]]))

;; Terminal Terminus =========================================================
(def ^:private ach-terminal-terminus
  {:id    :terminal-terminus
   :name  "Terminal Terminus"
   :group :state/wa
   :desc  "Deliver to both port terminals in Washington."})

(defmethod achievement-info :terminal-terminus [db _cheevo]
  (deliver-to-each-company db ["port_sea" "port_tac"]))

;; Over the Top ==============================================================
(def ^:private ach-over-the-top
  {:id    :over-the-top
   :name  "Over the Top"
   :group :state/wa
   :desc  "Drive through the forest road to timber harvest in Bellingham."})

(defmethod achievement-info :over-the-top [db _cheevo]
  ;; Either direction to this specific spot.
  (single-delivery db '[(match ?job)
                        [?loc :location/city    [:city/ident "bellingham"]]
                        [?loc :location/company [:company/ident "dg_wd_hrv"]]
                        (or [?job :job/source ?loc]
                            [?job :job/target ?loc])]))

;; ===========================================================================
;; |                                                                         |
;; |                                 Utah                                    |
;; |                                                                         |
;; ===========================================================================

;; This One is Mine ==========================================================
(def ^:private ach-this-one-is-mine
  {:id    :this-one-is-mine
   :name  "This One is Mine"
   :group :state/ut
   :desc  "Visit all mines and quarries in Utah."})

(defmethod achievement-info :this-one-is-mine [db _cheevo]
  (deliver-to-or-from-all
    db :set/city
    '[(location ?loc)
      [(ground ["salt_lake" "cedar_city" "vernal"]) [?city-slug ...]]
      [?city :city/ident       ?city-slug]
      [?loc  :location/city    ?city]
      [?loc  :location/company ?comp]
      [(ground ["cm_min_qry" "cm_min_str"]) [?company-slug ...]]
      [?comp :company/ident    ?company-slug]]))

;; Some Like it Salty ========================================================
(def ^:private ach-some-like-it-salty
  {:id    :some-like-it-salty
   :name  "Some Like it Salty"
   :group :state/ut
   :desc  "Take a job from each branch of each company located in Salt Lake City."})

(defmethod achievement-info :some-like-it-salty [db _cheevo]
  (deliver-from-all
    db :set/company
    '[(location ?loc)
      [?loc  :location/city    [:city/ident "salt_lake"]]]))

;; Pump it Up ================================================================
(def ^:private ach-pump-it-up
  {:id    :pump-it-up
   :name  "Pump it Up"
   :group :state/ut
   :desc  "Deliver 5 frac tank trailers to any oil drilling site in Utah."})

;; TODO: Steam Achievements says 4/5 for me; this logic says 2/5.
;; Maybe Steam is counting across profiles? Multiple company slugs?
(defmethod achievement-info :pump-it-up [db _cheevo]
  (counted-deliveries
    db 5
    '[(match ?job)
      [?job  :job/cargo        [:cargo/ident "frac_tank"]]
      [?job  :job/target       ?loc]
      [?loc  :location/city    ?city]
      [?city :city/state       :state/ut]
      [?loc  :location/company [:company/ident "gal_oil_sit"]]]))

;; ===========================================================================
;; |                                                                         |
;; |                                Idaho                                    |
;; |                                                                         |
;; ===========================================================================

;; Grown in Idaho ============================================================
(def ^:private ach-grown-in-idaho
  {:id    :grown-in-idaho
   :name  "Grown in Idaho"
   :group :state/id
   :desc  "Complete 5 deliveries of potatoes from Idaho farms."})

(defmethod achievement-info :grown-in-idaho [db _cheevo]
  (counted-deliveries
    db 5
    '[(match ?job)
      [?job  :job/cargo        [:cargo/ident "potatoes"]]
      [?job  :job/source       ?loc]
      [?loc  :location/city    ?city]
      [?city :city/state       :state/id]
      [?loc  :location/company [:company/ident "sc_frm"]]]))

;; Along the Snake River =====================================================
(def ^:private ach-along-the-snake-river
  {:id    :along-the-snake-river
   :name  "Along the Snake River"
   :group :state/id
   :desc  "PERFECT deliveries (any order and direction) between Kennewick/Lewiston, Boise/Twin Falls, Twin Falls/Pocatello, Pocatello/Idaho Falls."})

(def ^:private snake-river-pairs
  [["kennewick" "lewiston"]
   ["boise" "twin_falls"]
   ["twin_falls" "pocatello"]
   ["pocatello"  "idaho_falls"]])

(defmethod achievement-info :along-the-snake-river [db _cheevo]
  (let [snake-rules (conj rules '[(snake-river ?slug1 ?slug2 ?job)
                                  [?city1     :city/ident ?slug1]
                                  [?loc1      :location/city ?city1]
                                  [?city2     :city/ident ?slug2]
                                  [?loc2      :location/city ?city2]
                                  (or (and [?job :job/source ?loc1]
                                           [?job :job/target ?loc2])
                                      (and [?job :job/source ?loc2]
                                           [?job :job/target ?loc1]))])
        deliveries  (d/q '[:find ?slug1 ?slug2
                           :in $ % [[?slug1 ?slug2]] :where
                           (snake-river ?slug1 ?slug2 ?job)
                           (perfect? ?job)]
                         db snake-rules snake-river-pairs)
        needed      (remove (set deliveries) snake-river-pairs)]
    {:progress {:type      :special
                :special   :along-the-snake-river
                :completed deliveries
                :needed    needed}
     :jobs     (d/q '[:find [(pull ?job pattern) ...]
                      :in $ % pattern [[?slug1 ?slug2]] :where
                      (snake-river ?slug1 ?slug2 ?job)
                      (offer? ?job)]
                    db snake-rules job-pull needed)}))

;; Colorado
#_(defachievement energy-from-above
  "Deliver a tower and nacelle to both Vitas Power wind turbine construction sites in Colorado."
  {::pco/input [{:job/cargo     [:cargo/id]}
                {:job/destination [:company/id :country/id]}]}
  (fn [_env {{cargo       :cargo/id}   :job/cargo
             {destination :country/id
              recipient   :company/id} :job/destination}]
    {:job.cheevo/energy-from-above
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

;; ===========================================================================
;; |                                                                         |
;; |                                Wyoming                                  |
;; |                                                                         |
;; ===========================================================================

;; Big Boy ===================================================================
(def ^:private ach-big-boy
  {:id    :big-boy
   :name  "Big Boy"
   :group :state/wy
   :desc  "Deliver train parts, tamping machine and rails to or from the rail yard in Cheyenne."})

(defmethod achievement-info :big-boy
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
                     db rules slugs [:job/source :job/target])
        needed  (remove (set done) cargoes)]
    {:progress {:type      :set/cargo
                :completed (d/pull-many db cargo-pull done)
                :needed    (d/pull-many db cargo-pull needed)}
     :jobs     (d/q '[:find [(pull ?job pattern) ...]
                      :in $ % pattern [?cargo ...] [?dir ...] :where
                      [?job   :job/cargo ?cargo]
                      (offer? ?job)
                      [?job   ?dir       ?loc]
                      [?loc   :location/city    [:city/ident "cheyenne"]]
                      [?loc   :location/company [:company/ident "aml_rail_str"]]]
                    db rules job-pull needed [:job/source :job/target])}))

;; Buffalo Bill ==============================================================
(def ^:private ach-buffalo-bill
  {:id    :buffalo-bill
   :name  "Buffalo Bill"
   :group :state/wy
   :desc  "Complete 10 PERFECT cattle deliveries to livestock auctions in Wyoming."})

(defmethod achievement-info :buffalo-bill
  [db _cheevo]
  (let [buffalo-bill-rules
        (into '[[(buffalo-bill? ?job)
                 [?job  :job/cargo        [:cargo/ident "cattle"]]
                 [?job  :job/target       ?tgt]
                 [?tgt  :location/company [:company/ident "bn_live_auc"]]
                 [?tgt  :location/city    ?city]
                 [?city :city/state       :state/wy]]]
              rules)]
    {:progress {:type      :count
                :completed (d/q '[:find (count ?job) .
                                  :in $ % :where
                                  (buffalo-bill? ?job)
                                  (perfect? ?job)]
                                db buffalo-bill-rules)
                :total     10}
     :jobs     (d/q '[:find [(pull ?job pattern) ...]
                      :in $ % pattern :where
                      (buffalo-bill? ?job)
                      (offer? ?job)]
                    db buffalo-bill-rules job-pull)}))

(def achievement-groups
  [{:group   :state/ca
    :name    "California"
    :cheevos [ach-sea-dog
              ach-cheers]}
   {:group   :state/nv
    :name    "Nevada"
    :cheevos [ach-gold-fever]}
   {:group   :state/az
    :name    "Arizona"
    :cheevos [ach-sky-harbor]}
   {:group   :state/nm
    :name    "New Mexico"
    :cheevos [ach-sky-delivery]}
   {:group   :state/or
    :name    "Oregon"
    :cheevos [ach-lumberjack
              ach-cabbage-to-cabbage]}
   {:group   :state/wa
    :name    "Washington"
    :cheevos [ach-steel-wings
              ach-keep-sailing
              ach-terminal-terminus
              ach-over-the-top]}
   {:group   :state/ut
    :name    "Utah"
    :cheevos [ach-this-one-is-mine
              ach-some-like-it-salty
              ach-pump-it-up]}
   {:group   :state/id
    :name    "Idaho"
    :cheevos [ach-grown-in-idaho
              ach-along-the-snake-river]}
   {:group   :state/wy
    :name    "Wyoming"
    :cheevos [ach-big-boy
              ach-buffalo-bill]}
   {:group   :group/heavy-cargo
    :name    "Heavy Cargo"
    :cheevos [ach-heavy-but-not-a-bull-in-a-china-shop
              ach-should-be-heavy]}])

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
