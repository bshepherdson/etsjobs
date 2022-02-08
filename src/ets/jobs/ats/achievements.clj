(ns ets.jobs.ats.achievements
  (:require
    [ets.jobs.ats.map :as map]))

(def ^:private snake-river-pairs
  (let [fwd [["kennewick" "lewiston"]
             ["boise" "twin_falls"]
             ["twin_falls" "pocatello"]
             ["pocatello"  "idaho_falls"]]
        bwd (map reverse fwd)]

    (into #{} (map vec (concat fwd bwd)))))

(def ^:private ats-regions
  {:ca
   {:name "California"
    :achievements
    [{:key  :sea-dog
      :name "Sea Dog"
      :desc "Deliver cargo to a port in Oakland and a port in San Francisco."
      :pred (fn [{:keys [recipient]}]
              (#{"oak_port" "sf_port"} recipient))}

     {:key  :cheers
      :name "Cheers!"
      :desc "Deliver cargo from all 3 vineyards in California."
      ; TODO This isn't in my test profile, so I don't have the spelling.
      :pred (fn [{:keys [sender]}]
              (= sender "darchelle_uzau"))}]}

   :nv
   {:name "Nevada"
    :achievements
    [{:key  :gold-fever
      :name "Gold Fever"
      :desc "Deliver cargo to both quarries in Nevada."
      :pred (fn [{:keys [destination recipient]}]
              (and (#{"elko" "carson_city"} destination)
                   (#{"cm_min_qry" "cm_min_qryp"} recipient)))}]}

   :az
   {:name "Arizona"
    :achievements
    [{:key  :sky-harbor
      :name "Sky Harbor"
      :desc "Deliver cargo to the Phoenix Airport."
      :pred (fn [{:keys [recipient]}]
              (= recipient "aport_phx"))}]}

   :nm
   {:name "New Mexico"
    :achievements
    [{:key  :sky-delivery
      :name "Sky Delivery"
      :desc "Deliver cargo to An-124 depot."
      :pred (fn [{:keys [recipient]}]
              (= recipient "aport_ult"))}]}

   :or
   {:name "Oregon"
    :achievements
    [{:key  :lumberjack
      :name "Lumberjack"
      :desc "Deliver cargo from all timber harvest sites in Oregon."
      :pred (fn [{:keys [origin sender]}]
              (let [{:keys [country]} (map/human-name origin)]
                (and (= country "OR")
                     (= sender  "dg_wd_hrv"))))}

     {:key  :cabbage-to-cabbage
      :name "Cabbage to Cabbage"
      :desc "Carry cabbage (vegetables) over Cabbage Hill."
      :pred (fn [{:keys [origin destination cargo]}]
              (and (#{"frozen_veget" "vegetable"} cargo)
                   (= "OR" (:country (map/human-name origin)))
                   (= "OR" (:country (map/human-name destination)))))}]}

   :wa
   {:name "Washington"
    :achievements
    [{:key  :steel-wings
      :name "Steel Wings"
      :desc "Deliver to an aerospace company in Washinton."
      :pred (fn [{:keys [recipient]}]
              (= recipient "dw_air_pln"))}

     {:key  :keep-sailing
      :name "Keep Sailing"
      :desc "Deliver a boat to a marina in Washington."
      :pred (fn [{:keys [cargo destination recipient]}]
              (and (= "boat" cargo)
                   (= "sh_shp_mar" recipient)
                   (= "WA" (:country (map/human-name destination)))))}

     {:key  :terminal-terminus
      :name "Terminal Terminus"
      :desc "Deliver to both port terminals in Washington."
      :pred (fn [{:keys [recipient]}]
              (#{"port_sea" "port_tac"} recipient))}

     {:key  :over-the-top
      :name "Over the Top"
      :desc "Drive through the forest road to timber harvest in Bellingham."
      :pred (fn [{:keys [origin destination sender recipient]}]
              (and (or (= origin      "bellingham")
                       (= destination "bellingham"))
                   (or (= sender      "dg_wd_hrv")
                       (= recipient   "dg_wd_hrv"))))}]}

   :ut
   {:name "Utah"
    :achievements
    [{:key  :this-one-is-mine
      :name "This One is Mine"
      :desc "Visit all mines and quarries in Utah."
      :pred (fn [{:keys [origin destination sender recipient]}]
              (let [sites #{"salt_lake" "cedar_city" "vernal"}
                    locs  #{"cm_min_qry" "cm_min_qryp"}]
                (and (or (sites origin) (sites destination))
                     (or (locs sender)  (locs recipient)))))}

     {:key  :some-like-it-salty
      :name "Some Like It Salty"
      :desc "Take a job from each branch of each company located in Salt Lake City."
      :pred (fn [{:keys [origin]}]
              (= origin "salt_lake"))}

     {:key  :pump-it-up
      :name "Pump It Up"
      :desc "Deliver 5 frac tank trailers to any oil drilling site in Utah."
      :pred (fn [{:keys [cargo destination recipient]}]
              (and (= cargo       "frac_tank" )
                   (= recipient   "gal_oil_sit")
                   (= "UT" (:country (map/human-name destination)))))}]}


   :id
   {:name "Idaho"
    :achievements
    [{:key  :grown-in-idaho
      :name "Grown in Idaho"
      :desc "Complete 5 deliveries of potatoes from Idaho farms."
      :pred (fn [{:keys [cargo origin sender]}]
              (and (= cargo  "potatoes")
                   (= sender "sc_frm")
                   (= "ID"   (:country (map/human-name origin)))))}

     {:key  :along-the-snake-river
      :name "Along the Snake River"
      :desc "Complete PERFECT deliveries between Kennewick-Lewiston, Boise-Twin Falls, Twin Falls-Pocatello, Pocatello-Idaho Falls; any order or direction."
      :pred (fn [{:keys [origin destination]}]
              (snake-river-pairs [origin destination]))}]}

   :co
   {:name "Colorado"
    :achievements
    [{:key  :energy-from-above
      :name "Energy From Above"
      :desc "Deliver a tower and nacelle to both Vitas Power wind turbine construction sites in Colorado."
      :pred (fn [{:keys [cargo destination recipient]}]
              (and (#{"windml_eng" "windml_tube"} cargo)
                   (= "CO" (:country (map/human-name destination)))
                   (= recipient "vp_epw_sit")))}

     {:key  :gold-rush
      :name "Gold Rush"
      :desc "Deliver 10 loads to or from the NAMIQ company at the gold mine in Colorado."
      :pred (fn [{:keys [origin destination sender recipient]}]
              (or (and (= "CO" (:country (map/human-name origin)))
                       (#{"nmq_min_qry" "nmq_min_qrys"} sender))
                  (and (= "CO" (:country (map/human-name destination)))
                       (#{"nmq_min_qry" "nmq_min_qrys"} recipient))))}

     {:key  :up-and-away
      :name "Up and Away"
      :desc "Complete 10 delivery to Denver airport."
      :pred (fn [{:keys [recipient]}]
              (= "aport_den" recipient))}]}

   :wy
   {:name "Wyoming"
    :achievements
    [{:key  :big-boy
      :name "Big Boy"
      :desc "Deliver train parts, tamping machine and rails to or from the rail yard in Cheyenne."
      :pred (fn [{:keys [cargo origin destination sender recipient]}]
              (and (#{"train_part" "tamp_machine" "rails"} cargo)
                   (or (= "WY" (:country (map/human-name origin)))
                       (= "WY" (:country (map/human-name destination))))
                   (or (= "aml_rail_str" sender)
                       (= "aml_rail_str" recipient))))}

     {:key  :buffalo-bill
      :name "Buffalo Bill"
      :desc "Complete 10 PERFECT cattle deliveries to livestock auctions in Wyoming."
      :pred (fn [{:keys [cargo destination recipient]}]
              (and (= "cattle" cargo)
                   (= "WY" (:country (map/human-name destination)))
                   (= "bn_live_auc" recipient)))}]}
   })

(defn- ats-open-achievements [_]
  true)

(def ats-meta
  {:regions ats-regions
   :open    ats-open-achievements})

