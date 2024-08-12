(ns ets.jobs.ats.achievements
  (:require
   [com.wsscode.pathom3.connect.built-in.resolvers :as pbir]
   [com.wsscode.pathom3.connect.operation :as pco]
   [com.wsscode.pathom3.connect.indexes :as pci]
   [ets.jobs.util.interface :as util :refer [defachievement]]))

;; California
(defachievement sea-dog
  "Deliver cargo to a port in Oakland and port in San Francisco."
  {::pco/input [{:job/destination [:company/id]}]}
  (fn [_env {{recipient :company/id} :job/destination}]
   {:job.cheevo/sea-dog (boolean (#{"oak_port" "sf_port"} recipient))}))

(defachievement cheers
  "Deliver cargo from all 3 vineyards in California."
  {::pco/input [{:job/origin [:company/id]}]}
  (fn [_env {{sender :company/id} :job/origin}]
    {:job.cheevo/cheers (= sender "darchelle_uzau")}))

;; Nevada
(defachievement gold-fever
  "Deliver cargo to both quarries in Nevada."
  {::pco/input [{:job/destination [:city/id :company/id]}]}
  (fn [_env {{destination :city/id, recipient :company/id} :job/destination}]
    {:job.cheevo/gold-fever (and (#{"elko" "carson_city"} destination)
                                 (#{"cm_min_qry" "cm_min_qryp"} recipient))}))

;; Arizona
(defachievement sky-harbor
  "Deliver cargo to the Phoenix Airport."
  {::pco/input [{:job/destination [:company/id]}]}
  (fn [_env {{recipient :company/id} :job/destination}]
    {:job.cheevo/sky-harbor (= recipient "aport_phx")}))

;; New Mexico
(defachievement sky-delivery
  "Deliver cargo to An-124 depot."
  {::pco/input [{:job/destination [:company/id]}]}
  (fn [_env {{recipient :company/id} :job/destination}]
    {:job.cheevo/sky-delivery (= recipient "aport_ult")}))

;; Oregon
(defachievement lumberjack
  "Deliver cargo from all timber harvest sites in Oregon."
  {::pco/input [{:job/origin [:company/id :country/id]}]}
  (fn [_env {{sender :company/id, state :country/id} :job/origin}]
    {:job.cheevo/lumberjack (and (= state  "OR")
                                 (= sender "dg_wd_hrv"))}))

(defachievement cabbage-to-cabbage
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
(defachievement steel-wings
  "Deliver to an aerospace company in Washington."
  {::pco/input [{:job/destination [:company/id]}]}
  (fn [_env {{recipient :company/id} :job/destination}]
    {:job.cheevo/steel-wings (= recipient "dw_air_pln")}))

(defachievement keep-sailing
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

(defachievement terminal-terminus
  "Deliver to both port terminals in Washington."
  {::pco/input [{:job/destination [:company/id]}]}
  (fn [_env {{recipient :company/id} :job/destination}]
    {:job.cheevo/terminal-terminus (boolean (#{"port_sea" "port_tac"} recipient))}))

(defachievement over-the-top
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
(defachievement this-one-is-mine
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

(defachievement some-like-it-salty
  "Take a job from each branch of each company located in Salt Lake City."
  {::pco/input [{:job/origin [:city/id]}]}
  (fn [_env {{origin :city/id} :job/origin}]
    {:job.cheevo/some-like-it-salty (= origin "salt_lake")}))

(defachievement pump-it-up
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
(defachievement grown-in-idaho
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

(defachievement along-the-snake-river
  "Complete PERFECT deliveries between Kennewick-Lewiston, Boise-Twin Falls, Twin Falls-Pocatello, Pocatello-Idaho Falls; any order or direction."
  {::pco/input [{:job/origin      [:city/id]}
                {:job/destination [:city/id]}]}
  (fn [_env {{origin      :city/id} :job/origin
             {destination :city/id} :job/destination}]
    {:job.cheevo/along-the-snake-river
     (boolean (snake-river-pairs [origin destination]))}))

;; Colorado
(defachievement energy-from-above
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

(defachievement gold-rush
  "Deliver 10 loads to or from the NAMIQ company at the gold mine in Colorado."
  {::pco/input [{:job/origin      [:company/id :country/id]}
                {:job/destination [:company/id :country/id]}]}
  (fn [_env {:job/keys [origin destination]}]
    {:job.cheevo/gold-rush (or (co-mining origin)
                               (co-mining destination))}))

(defachievement up-and-away
  "Complete 10 delivery to Denver airport."
  {::pco/input [{:job/destination [:company/id]}]}
  (fn [_env {{recipient :company/id} :job/destination}]
    {:job.cheevo/up-and-away (= "aport_den" recipient)}))

;; Wyoming
(defn- big-boy-site [{state   :country/id
                      company :company/id}]
  (and (= "WY" state)
       (= "aml_rail_str" company)))

(defachievement big-boy
  "Deliver train parts, tamping machine and rails to or from the rail yard in Cheyenne."
  {::pco/input [{:job/origin      [:company/id :country/id]}
                {:job/destination [:company/id :country/id]}
                {:job/cargo       [:cargo/id]}]}
  (fn [_env {:job/keys [cargo origin destination]}]
    {:job.cheevo/big-boy
     (boolean (and (#{"train_part" "tamp_machine" "rails"} (:cargo/id cargo))
                   (big-boy-site origin)
                   (big-boy-site destination)))}))

(defachievement buffalo-bill
  "Complete 10 PERFECT cattle deliveries to livestock auctions in Wyoming."
  {::pco/input [{:job/destination [:company/id :country/id]}
                {:job/cargo       [:cargo/id]}]}
  (fn [_env {{cargo       :cargo/id}   :job/cargo
             {destination :country/id
              recipient   :company/id} :job/destination}]
    (and (= "cattle" cargo)
         (= "WY" destination)
         (= "bn_live_auc" recipient))))

(def ^:private achievements-list
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

(def ^:private all-achievements
  (pbir/constantly-resolver
    :achievements/all
    (mapv #(select-keys % [:cheevo/id]) achievements-list)))

(def ^:private achievements-by-id
  (util/table-indexed-by achievements-list :cheevo/id))

(def achievement-job-flags
  (->> achievements-list
       (map :cheevo/flag)
       sort
       vec))

(def ^:private regions
  (->> achievements-list
       (map :cheevo/region)
       set
       vec
       (pbir/constantly-resolver :regions/all)))

(def ^:private achievements-by-region
  (group-by (comp :region/id :cheevo/region) achievements-list))

(def ^:private region-table
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

(def index
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
