(ns ets.jobs.ets2.achievements
  (:require
   [com.wsscode.pathom3.connect.built-in.resolvers :as pbir]
   [com.wsscode.pathom3.connect.operation :as pco]
   [com.wsscode.pathom3.connect.indexes :as pci]
   [ets.jobs.ets2.map :as map]
   [ets.jobs.util.interface :as util :refer [defachievement]]))

(pco/defresolver baltic-country [{c :country/id}]
  {:country/baltic? (boolean (#{"EST" "FI" "LT" "LV" "RU"} c))})

#_(defn- achievement [label spec f]
  (let [flag   (keyword "job.cheevo" (name label))
        output (keyword "jobs" (name label))] ; eg. :jobs/cattle-drive
    [(pco/resolver label (merge {::pco/output [flag]} spec) f)
     (pco/resolver (symbol (namespace label) (str (name label) "-jobs"))
                   {::pco/input   [{:jobs/all-available [:job/id flag]}]
                    ::pco/output  [{output              [:job/id]}]}
                   (fn [_env {jobs :jobs/all-available}]
                     {output (filter flag jobs)}))]))

#_(defmacro ^:private defachievement
  ([sym spec f] `(defachievement ~sym "(no docs)" ~spec ~f))
  ([sym docstring spec f]
   `(def ~(symbol sym) ~docstring
      (achievement '~(symbol (str *ns*) (name sym)) ~spec ~f))))

(defachievement concrete-jungle
  {::pco/input [{:job/origin [:country/baltic? :company/id]}]}
  (fn [_env
       {{baltic? :country/baltic?
         sender  :company/id}    :job/origin}]
    {:job.cheevo/concrete-jungle (and baltic?
                                      (= "radus" sender))}))

(defachievement industry-standard
  "2 deliveries to every paper mill, loco factory, and furniture factory in
  the Baltic states.
  Those are LVR, Renat, Viljo Paperitehdas Oy, Estonian Paper AS, and VPF
  (lvr, renat, viljo_paper, ee_paper, viln_paper)."
  {}
  (fn [_env {{recipient :company/id} :job/destination}]
    {:job.cheevo/industry-standard
     (boolean (#{"lvr" "renat" "viljo_paper" "ee_paper" "viln_paper"} recipient))}))

(defachievement exclave-transit
  "Delivery from Kaliningrad to other Russian cities."
  {::pco/input [{:job/origin      [:city/id]}
                {:job/destination [:city/id :country/id]}]}
  (fn [_env
       {{origin       :city/id}    :job/origin
        {dest         :city/id
         dest-country :country/id} :job/destination}]
    {:job.cheevo/exclave-transit (and (= "kaliningrad" origin)
                                      (not= "kaliningrad" dest)
                                      (= "RU" dest-country))}))

(defachievement like-a-farmer
  "Deliver to each farm in the Baltic region.
  Agrominta UAB (agrominta, agrominta_a; both near Utena LT)
  Eviksi (eviksi, eviksi_a; (double near Liepaja LV))
  Maatila Egres (egres)
  Onnelik talu (onnelik, onnelik_a; double near Parna EST)
  Zelenye Polja (zelenye, zelenye_a)"
  {}
  (fn [_env {{recipient :company/id} :job/destination}]
    {:job.cheevo/like-a-farmer (boolean (#{"agrominta" "agrominta_a"
                                           "eviksi" "eviksi_a"
                                           "egres"
                                           "onnelik" "onnelik_a"
                                           "zelenye" "zelenye_a"}
                                          recipient))}))

(defachievement turkish-delight
  "Deliveries from Istanbul at least 2500km long."
  {::pco/input [{:job/origin [:city/id]}
                :job/distance]}
  (fn [_env
       {{origin :city/id} :job/origin
        :keys [job/distance]}]
    {:job.cheevo/turkish-delight (and (>= distance 2500)
                                      (= origin "istanbul"))}))

(defachievement along-the-black-sea
  "Perfect deliveries either direction between these pairs:
  Istanbul-Burgas
  Burgas-Varna
  Varna-Mangalia
  Mangalia-Constanta"
  {::pco/input [{:job/origin      [:city/id]
                 :job/destination [:city/id]}]}
  (fn [_env
       {{origin      :city/id} :job/origin
        {destination :city/id} :job/destination}]
    {:job.cheevo/along-the-black-sea
     (some #(= (into #{} [origin destination]) %)
           [#{"istanbul" "burgas"}
            #{"burgas" "varna"}
            #{"varna" "mangalia"}
            #{"mangalia" "constanta"}])}))

(defachievement orient-express
  {::pco/input [{:job/origin      [:city/id]
                 :job/destination [:city/id]}]}
  (fn [_env
       {{origin      :city/id} :job/origin
        {destination :city/id} :job/destination}]
    {:job.cheevo/orient-express
     (some #(= [origin destination] %)
           [["paris" "strasbourg"]
            ["strasbourg" "munchen"]
            ["munchen" "wien"]
            ["wien" "budapest"]
            ["budapest" "bucuresti"]
            ["bucuresti" "istanbul"]])}))

(defachievement lets-get-shipping
  "Deliver to all container ports in Iberia (TS Atlas)."
  {::pco/input [{:job/destination [:company/id]}]}
  (fn [_env {{recipient :company/id} :job/destination}]
    {:job.cheevo/lets-get-shipping (= "ts_atlas" recipient)}))

(defachievement fleet-builder
  "Deliver to all shipyards in Iberia (Ocean Solution Group, ocean_sol)."
  {::pco/input [{:job/destination [:company/id]}]}
  (fn [_env {{recipient :company/id} :job/destination}]
    {:job.cheevo/fleet-builder (= "ocean_sol" recipient)}))

(defachievement iberian-pilgrimage
  "Deliver from Lisbon, Seville and Pamplona to A Coruna."
  {::pco/input [{:job/origin      [:city/id]}
                {:job/destination [:city/id]}]}
  (fn [_env
       {{origin      :city/id} :job/origin
        {destination :city/id} :job/destination}]
    {:job.cheevo/iberian-pilgrimage
     (boolean (and (= destination "a_coruna")
                   (#{"lisboa" "sevilla" "pamplona"} origin)))}))

(defachievement taste-the-sun
  "Deliver ADR cargo to all solar power plants in Iberia (engeron)."
  {::pco/input [{:job/cargo       [(pco/? :cargo/adr)]}
                {:job/destination [:company/id]}]}
  (fn [_env
       {{adr       :cargo/adr}  :job/cargo
        {recipient :company/id} :job/destination}]
    {:job.cheevo/taste-the-sun (boolean (and (= "engeron" recipient)
                                             (not-empty adr)))}))

;; Scandinavia ===============================================================
(pco/defresolver scandinavian-country [{c :country/id}]
  {:country/scandinavian? (boolean (#{"DK" "N" "S"} c))})

; TODO Test this one - no jobs found.
(defachievement volvo-trucks-lover
  "Deliver trucks from the Volvo factory to a dealer."
  {::pco/input [{:job/cargo  [:cargo/id]}
                {:job/origin [:company/id]}]}
  (fn [_env {{cargo  :cargo/id}   :job/cargo
             {sender :company/id} :job/origin}]
    {:job.cheevo/volvo-trucks-lover (and (= "volvo_fac" sender)
                                         (= "trucks" cargo))}))

; TODO Test this one - no jobs found.
(defachievement scania-trucks-lover
  "Deliver trucks from Scania factory to a dealer."
  {::pco/input [{:job/cargo  [:cargo/id]}
                {:job/origin [:company/id]}]}
  (fn [_env {{cargo  :cargo/id}   :job/cargo
             {sender :company/id} :job/origin}]
    {:job.cheevo/scania-trucks-lover (and (= "scania_fac" sender)
                                          (= "trucks" cargo))}))

(defachievement sailor
  "Deliver yachts to all Scandinavian marinas (marina)."
  {::pco/input [{:job/cargo       [:cargo/id]}
                {:job/destination [:company/id :country/scandinavian?]}]}
  (fn [_env
       {{cargo     :cargo/id}              :job/cargo
        {recipient :company/id
         scandi?   :country/scandinavian?} :job/destination}]
    {:job.cheevo/sailor (and scandi?
                             (= "marina" recipient)
                             (= "yacht" cargo))}))

(defachievement cattle-drive
  "Complete a livestock delivery to Scandinavia."
  {::pco/input [{:job/cargo       [:cargo/id]}
                {:job/destination [:country/scandinavian?]}]}
  (fn [_env
       {{cargo     :cargo/id}              :job/cargo
        {scandi?   :country/scandinavian?} :job/destination}]
    {:job.cheevo/cattle-drive (and scandi? (= "livestock" cargo))}))

(defachievement whatever-floats-your-boat
  "Deliver to all container ports in Scandinavia (cont_port)."
  {::pco/input [{:job/destination [:company/id :country/scandinavian?]}]}
  (fn [_env
       {{recipient :company/id
         scandi?   :country/scandinavian?} :job/destination}]
    {:job.cheevo/whatever-floats-your-boat (and scandi?
                                                (= "cont_port" recipient))}))

(defachievement miner
  "Deliver to all quarries in Scandinavia (nord_sten, ms_stein)."
  {::pco/input [{:job/destination [:company/id :country/scandinavian?]}]}
  (fn [_env
       {{recipient :company/id
         scandi?   :country/scandinavian?} :job/destination}]
    {:job.cheevo/miner (boolean (and scandi?
                                     (#{"nord_sten" "ms_stein"} recipient)))}))

;; France ====================================================================
(defachievement go-nuclear
  "Deliver to 5 nuclear plants in France (nucleon)."
  {::pco/input [{:job/destination [:company/id :country/id]}]}
  (fn [_env
       {{recipient :company/id
         country   :country/id} :job/destination}]
    {:job.cheevo/go-nuclear (and (= "nucleon" recipient)
                                 (= "F" country))}))

(defachievement check-in-check-out
  "Deliver to all cargo airport terminals in France (fle, in France.)"
  {::pco/input [{:job/destination [:company/id :country/id]}]}
  (fn [_env
       {{recipient :company/id
         country   :country/id} :job/destination}]
    {:job.cheevo/check-in-check-out (and (= "fle" recipient)
                                         (= "F" country))}))

;; TODO: Implement this one once I can see a "gas must flow" job.
(defachievement gas-must-flow
  "Deliver petrol/gasoline, diesel, or LPG to all truck stops in France."
  {::pco/input [{:job/destination [:company/id :country/id]}
                {:job/cargo       [:cargo/id]}]}
  (fn [_env {{cargo     :cargo/id}   :job/cargo
             {recipient :company/id
              country   :country/id} :job/destination}]
    {:job.cheevo/gas-must-flow (and (contains? #{"diesel" "lpg" "petrol"} cargo)
                                    (= country "F")
                                    (= recipient "something"))}))

;; Italy =====================================================================
(defachievement captain
  "Deliver to all Italian shipyards (c_navale)."
  {::pco/input [{:job/destination [:company/id]}]}
  (fn [_env {{recipient :company/id} :job/destination}]
    {:job.cheevo/captain (= "c_navale" recipient)}))

(defachievement michaelangelo
  "Deliver from the Carrara quarry (marmo in Livorno)."
  {::pco/input [{:job/origin [:company/id :city/id]}]}
  (fn [_env
       {{sender :company/id
         origin :city/id}   :job/origin}]
    {:job.cheevo/michaelangelo (and (= "marmo" sender)
                                    (= "livorno" origin))}))

;; Schema for jobs:
;; {:job/origin      {:city/id    "origin"
;;                    :company/id "sender"}
;;  :job/destination {:city/id    "destination"
;;                    :company/id "recipient"}
;;  :job/cargo       {:cargo/id   "things"}
;;  :job/distance    1234 #_km}

(def ^:private achievements-list
  [{:cheevo/id     :whatever-floats-your-boat
    :cheevo/name   "Whatever Floats Your Boat"
    :cheevo/region {:region/id :scandinavia}
    :cheevo/desc   "Deliver to all container ports in Scandinavia (Container Port)."
    :cheevo/flag   :job.cheevo/whatever-floats-your-boat}

   {:cheevo/id     :sailor
    :cheevo/name   "Sailor"
    :cheevo/region {:region/id :scandinavia}
    :cheevo/desc   "Deliver yachts to all Scandinavian marinas (boat symbol)."
    :cheevo/flag   :job.cheevo/sailor}

   {:cheevo/id     :volvo-trucks-lover
    :cheevo/name   "Volvo Trucks Lover"
    :cheevo/region {:region/id :scandinavia}
    :cheevo/desc   "Deliver trucks from the Volvo factory."
    :cheevo/flag   :job.cheevo/volvo-trucks-lover}

   {:cheevo/id     :scania-trucks-lover
    :cheevo/name   "Scania Trucks Lover"
    :cheevo/region {:region/id :scandinavia}
    :cheevo/desc   "Deliver trucks from the Scania factory."
    :cheevo/flag   :job.cheevo/scania-trucks-lover}

   {:cheevo/id     :cattle-drive
    :cheevo/name   "Cattle Drive"
    :cheevo/region {:region/id :scandinavia}
    :cheevo/desc   "Complete a livestock delivery to Scandinavia."
    :cheevo/flag   :job.cheevo/cattle-drive}

   {:cheevo/id     :miner
    :cheevo/name   "Miner"
    :cheevo/region {:region/id :scandinavia}
    :cheevo/desc   "Deliver to all quarries in Scandinavia (Nordic Stenbrott, MS Stein)."
    :cheevo/flag   :job.cheevo/miner}

   ;; Baltic
   {:cheevo/id     :concrete-jungle
    :cheevo/name   "Concrete Jungle"
    :cheevo/region {:region/id :baltic}
    :cheevo/desc   "Complete 10 deliveries from concrete plants (Radus, Радус)"
    :cheevo/flag   :job.cheevo/concrete-jungle}

   {:cheevo/id     :industry-standard
    :cheevo/name   "Industry Standard"
    :cheevo/region {:region/id :baltic}
    :cheevo/desc   (str "Complete 2 deliveries to every paper mill, loco factory, and"
                        "furniture maker in the Baltic region (LVR, Renat, Viljo "
                        "Paperitehdas Oy, Estonian Paper AS, VPF).")
    :cheevo/flag   :job.cheevo/industry-standard}

   {:cheevo/id     :exclave-transit
    :cheevo/name   "Exclave Transit"
    :cheevo/region {:region/id :baltic}
    :cheevo/desc   "Complete 5 deliveries from Kaliningrad to any other Russian city."
    :cheevo/flag   :job.cheevo/exclave-transit}

   {:cheevo/id     :like-a-farmer
    :cheevo/name   "Like a Farmer"
    :cheevo/region {:region/id :baltic}
    :cheevo/desc   "Deliver to each farm in the Baltic. (Agrominta UAB, Eviksi, Maatila Egres, Onnelik Talu, Zelenye Polja)"
    :cheevo/flag   :job.cheevo/like-a-farmer}

   ;; Beyond the Black Sea
   {:cheevo/id     :turkish-delight
    :cheevo/name   "Turkish Delight"
    :cheevo/region {:region/id :black-sea}
    :cheevo/desc   "Complete 3 deliveries from Istanbul which are at least 2500km long."
    :cheevo/flag   :job.cheevo/turkish-delight}

   {:cheevo/id     :along-the-black-sea
    :cheevo/name   "Along the Black Sea"
    :cheevo/region {:region/id :black-sea}
    :cheevo/desc   "Complete perfect deliveries in any order or direction between these coastal cities."
    :cheevo/flag   :job.cheevo/along-the-black-sea}

   {:cheevo/id     :orient-express
    :cheevo/name   "Orient Express"
    :cheevo/region {:region/id :black-sea}
    :cheevo/desc   "Complete deliveries between the following cities, in order: Paris, Strasbourg, Munich, Vienna, Budapest, Bucharest, Istanbul. (Requires Going East as well!)"
    :cheevo/flag   :job.cheevo/orient-express}

   ;; Italia
   {:cheevo/id     :captain
    :cheevo/name   "Captain"
    :cheevo/region {:region/id :italia}
    :cheevo/desc   "Deliver to all Italian shipyards. (Cantiare Navale)"
    :cheevo/flag   :job.cheevo/captain}

   {:cheevo/id     :michaelangelo
    :cheevo/name   "Michaelangelo"
    :cheevo/region {:region/id :italia}
    :cheevo/desc   "Deliver from the Carrara quarry (Marmo SpA in Livorno)."
    :cheevo/flag   :job.cheevo/michaelangelo}

   ;; Vive la France
   {:cheevo/id     :go-nuclear
    :cheevo/name   "Go Nuclear"
    :cheevo/region {:region/id :vive-la-france}
    :cheevo/desc   "Deliver to five nuclear power plants in France. (Nucleon)"
    :cheevo/flag   :job.cheevo/go-nuclear}

   {:cheevo/id     :check-in-check-out
    :cheevo/name   "Check in, Check out"
    :cheevo/region {:region/id :vive-la-france}
    :cheevo/desc   "Deliver to all cargo airport terminals in France (FLE)."
    :cheevo/flag   :job.cheevo/check-in-check-out}

   {:cheevo/id     :gas-must-flow
    :cheevo/name   "Gas Must Flow"
    :cheevo/region {:region/id :vive-la-france}
    :cheevo/desc   "Deliver diesel, LPG or gasoline/petrol to all truck stops in France. (Eco)"
    :cheevo/flag   :job.cheevo/gas-must-flow}

   ;; Iberia
   {:cheevo/id     :lets-get-shipping
    :cheevo/name   "Let's Get Shipping"
    :cheevo/region {:region/id :iberia}
    :cheevo/desc   "Deliver to all container ports in Iberia (TS Atlas)."
    :cheevo/flag   :job.cheevo/lets-get-shipping}

   {:cheevo/id     :fleet-builder
    :cheevo/name   "Fleet Builder"
    :cheevo/region {:region/id :iberia}
    :cheevo/desc   "Deliver to all shipyards in Iberia (Ocean Solution Group)."
    :cheevo/flag   :job.cheevo/fleet-builder}

   {:cheevo/id     :taste-the-sun
    :cheevo/name   "Taste the Sun"
    :cheevo/region {:region/id :iberia}
    :cheevo/desc   "Deliver ADR cargo to all solar power plants in Iberia (Engeron)."
    :cheevo/flag   :job.cheevo/taste-the-sun}

   {:cheevo/id     :iberian-pilgrimage
    :cheevo/name   "Iberian Pilgrimage"
    :cheevo/region {:region/id :iberia}
    :cheevo/desc   "Deliver to A Coruña from Lisbon, Seville and Pamplona."
    :cheevo/flag   :job.cheevo/iberian-pilgrimage}])

(def ^:private all-achievements
  (pbir/constantly-resolver
    :achievements/all
    (mapv #(select-keys % [:cheevo/id]) achievements-list)))

(def ^:private achievements-by-id
  (util/table-indexed-by achievements-list :cheevo/id))

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

(def ^:private achievements-by-region
  (util/table-grouped-by
    achievements-list :region/id
    (comp :region/id :cheevo/region)
    #(assoc {} :region/achievements %)))

(def achievement-job-flags
  (->> achievements-list
       (map :cheevo/flag)
       (remove #{:job.cheevo/gas-must-flow
                 :job.cheevo/scania-trucks-lover
                 :job.cheevo/volvo-trucks-lover})
       sort
       vec))

(def ^:private regions
  (pbir/constantly-resolver
    :regions/all
    [{:region/id :scandinavia}
     {:region/id :baltic}
     {:region/id :black-sea}
     {:region/id :italia}
     {:region/id :vive-la-france}
     {:region/id :iberia}]))

(def ^:private achievements-by-region
  (group-by (comp :region/id :cheevo/region) achievements-list))

(def ^:private region-table
  (->> (for [[region contents] {:scandinavia    {:region/name "Scandinavia"}
                                :baltic         {:region/name "Beyond the Baltic Sea"}
                                :black-sea      {:region/name "Road to the Black Sea"}
                                :italia         {:region/name "Italia"}
                                :vive-la-france {:region/name "Vive la France"}
                                :iberia         {:region/name "Iberia"}}]

         (do
           (prn "region-table inner" region (achievements-by-region region))
           [region (assoc contents :region/achievements (achievements-by-region region))]))
       (into {})
       (pbir/static-table-resolver `region-table :region/id)))

(def index
  (pci/register
    [;; Top level
     all-achievements
     achievements-by-id
     achievements-by-region
     regions region-table
     ;; Misc
     baltic-country
     scandinavian-country
     ;; Cheevo predicates
     along-the-black-sea
     captain
     cattle-drive
     check-in-check-out
     concrete-jungle
     exclave-transit
     gas-must-flow
     go-nuclear
     iberian-pilgrimage
     industry-standard
     fleet-builder
     lets-get-shipping
     like-a-farmer
     michaelangelo
     miner
     orient-express
     sailor
     scania-trucks-lover
     taste-the-sun
     turkish-delight
     volvo-trucks-lover
     whatever-floats-your-boat]))
