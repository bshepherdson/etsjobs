(ns ets.jobs.ets2.achievements
  (:require
   [com.wsscode.pathom3.connect.built-in.resolvers :as pbir]
   [com.wsscode.pathom3.connect.operation :as pco]
   [com.wsscode.pathom3.connect.indexes :as pci]
   [ets.jobs.ets2.map :as map]))

(pco/defresolver baltic-country [{c :country/id}]
  {:country/baltic? (boolean (#{"EST" "FI" "LT" "LV" "RU"} c))})

(defn- achievement [label spec f]
  (let [flag   (keyword "job.cheevo" (name label))
        output (keyword "jobs" (name label))] ; eg. :jobs/cattle-drive
    [(pco/resolver label (merge {::pco/output [flag]} spec) f)
     (pco/resolver (symbol (namespace label) (str (name label) "-jobs"))
                   {::pco/input   [{:jobs/all-available [:job/id flag]}]
                    ::pco/output  [{output              [:job/id]}]}
                   (fn [_env {jobs :jobs/all-available}]
                     {output (filter flag jobs)}))]))

(defmacro ^:private defachievement
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

(comment
  concrete-jungle)

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

#_(pco/defresolver turkish-delight
  [{{origin :city/id} :job/origin
    distance           :job/distance}]
  {::pco/input  [{:job/origin [:city/id]}
                 :job/distance]
   ::pco/output [:job.cheevo/turkish-delight]}
  {:job.cheevo/turkish-delight (and (>= distance 2500)
                                    (= origin "istanbul"))})

#_(pco/defresolver turkish-delight-jobs
  [{jobs :jobs/all-available}]
  {::pco/input  [{:jobs/all-available   [:job/id :job.cheevo/turkish-delight]}]
   ::pco/output [{:jobs/turkish-delight [:job/id]}]}
  {:jobs/turkish-delight (for [job jobs
                               :when (:job.cheevo/turkish-delight job)]
                           (select-keys job [:job/id]))})

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
#_(pco/defresolver volvo-trucks-lover
  "Deliver trucks from the Volvo factory to a dealer."
  [{:keys [cargo sender]}]
  (and (= "volvo_fac" sender)
       (= "trucks" cargo)))

; TODO Test this one - no jobs found.
#_(defn scania-trucks-lover
  "Deliver trucks from Scania factory to a dealer."
  [{:keys [cargo sender]}]
  (and (= "scania_fac" sender)
       (= "trucks" cargo)))

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

(comment
  (map (comp ::pco/input :config) sailor)
  (map (comp ::pco/output :config) sailor))

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
#_(pco/defresolver gas-must-flow
  "Deliver petrol/gasoline, diesel, or LPG to all truck stops in France."
  [{{cargo     :cargo/id}   :job/cargo
    {recipient :company/id
     country   :country/id} :job/destination}]
  {::pco/input [{:job/destination [:company/id :country/id]}
                {:job/cargo       [:cargo/id]}]}
  {:job.cheevo/gas-must-flow (and (contains? #{"diesel" "lpg" "petrol"} cargo)
                                  (= country "F")
                                  (= recipient "something"))})

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

(def ^:private achievements
  (pbir/static-table-resolver
    :cheevo/id
    {:whatever-floats-your-boat
     {:cheevo/name   "Whatever Floats Your Boat"
      :cheevo/region :scandinavia
      :cheevo/desc   "Deliver to all container ports in Scandinavia (Container Port)."
      :cheevo/flag   :job.cheevo/whatever-floats-your-boat}

     :sailor
     {:cheevo/name   "Sailor"
      :cheevo/region :scandinavia
      :cheevo/desc   "Deliver yachts to all Scandinavian marinas (boat symbol)."
      :cheevo/flag   :job.cheevo/sailor}

     :volvo-trucks-lover
     {:cheevo/name   "Volvo Trucks Lover"
      :cheevo/region :scandinavia
      :cheevo/desc   "Deliver trucks from the Volvo factory."
      :cheevo/flag   :job.cheevo/volvo-trucks-lover}
     :scania-trucks-lover
     {:cheevo/name   "Scania Trucks Lover"
      :cheevo/region :scandinavia
      :cheevo/desc   "Deliver trucks from the Scania factory."
      :cheevo/flag   :job.cheevo/scania-trucks-lover}

     :cattle-drive
     {:cheevo/name   "Cattle Drive"
      :cheevo/region :scandinavia
      :cheevo/desc   "Complete a livestock delivery to Scandinavia."
      :cheevo/flag   :job.cheevo/cattle-drive}

     :miner
     {:cheevo/name   "Miner"
      :cheevo/region :scandinavia
      :cheevo/desc   "Deliver to all quarries in Scandinavia (Nordic Stenbrott, MS Stein)."
      :cheevo/flag   :job.cheevo/miner}

    ;; Baltic
    :concrete-jungle
    {:cheevo/name   "Concrete Jungle"
     :cheevo/region :baltic
     :cheevo/desc   "Complete 10 deliveries from concrete plants (Radus, Радус)"
     :cheevo/flag   :job.cheevo/concrete-jungle}

    :industry-standard
    {:cheevo/name   "Industry Standard"
     :cheevo/region :baltic
     :cheevo/desc   (str "Complete 2 deliveries to every paper mill, loco factory, and"
                         "furniture maker in the Baltic region (LVR, Renat, Viljo "
                         "Paperitehdas Oy, Estonian Paper AS, VPF).")
     :cheevo/flag   :job.cheevo/industry-standard}

    :exclave-transit
    {:cheevo/name   "Exclave Transit"
     :cheevo/region :baltic
     :cheevo/desc   "Complete 5 deliveries from Kaliningrad to any other Russian city."
     :cheevo/flag   :job.cheevo/exclave-transit}

    :like-a-farmer
    {:cheevo/name   "Like a Farmer"
     :cheevo/region :baltic
     :cheevo/desc   "Deliver to each farm in the Baltic. (Agrominta UAB, Eviksi, Maatila Egres, Onnelik Talu, Zelenye Polja)"
     :cheevo/flag   :job.cheevo/like-a-farmer}

    ;; Beyond the Black Sea
    :turkish-delight
    {:cheevo/name   "Turkish Delight"
     :cheevo/region :black-sea
     :cheevo/desc   "Complete 3 deliveries from Istanbul which are at least 2500km long."
     :cheevo/flag   :job.cheevo/turkish-delight}

    :along-the-black-sea
    {:cheevo/name   "Along the Black Sea"
     :cheevo/region :black-sea
     :cheevo/desc   "Complete perfect deliveries in any order or direction between these coastal cities."
     :cheevo/flag   :job.cheevo/along-the-black-sea}

    :orient-express
    {:cheevo/name   "Orient Express"
     :cheevo/region :black-sea
     :cheevo/desc   "Complete deliveries between the following cities, in order: Paris, Strasbourg, Munich, Vienna, Budapest, Bucharest, Istanbul. (Requires Going East as well!)"
     :cheevo/flag   :job.cheevo/orient-express}

    ;; Italia
    :captain
    {:cheevo/name   "Captain"
     :cheevo/region :italia
     :cheevo/desc   "Deliver to all Italian shipyards. (Cantiare Navale)"
     :cheevo/flag   :job.cheevo/captain}

    :michaelangelo
    {:cheevo/name   "Michaelangelo"
     :cheevo/region :italia
     :cheevo/desc   "Deliver from the Carrara quarry (Marmo SpA in Livorno)."
     :cheevo/flag   :job.cheevo/michaelangelo}

    ;; Vive la France
    :go-nuclear
    {:cheevo/name   "Go Nuclear"
     :cheevo/region :vive-la-france
     :cheevo/desc   "Deliver to five nuclear power plants in France. (Nucleon)"
     :cheevo/flag   :job.cheevo/go-nuclear}

    :check-in-check-out
    {:cheevo/name   "Check in, Check out"
     :cheevo/region :vive-la-france
     :cheevo/desc   "Deliver to all cargo airport terminals in France (FLE)."
     :cheevo/flag   :job.cheevo/check-in-check-out}

    :gas-must-flow
    {:cheevo/name   "Gas Must Flow"
     :cheevo/region :vive-la-france
     :cheevo/desc   "Deliver diesel, LPG or gasoline/petrol to all truck stops in France. (Eco)"
     :cheevo/flag   :job.cheevo/gas-must-flow}

    ;; Iberia
    :lets-get-shipping
    {:cheevo/name   "Let's Get Shipping"
     :cheevo/region :iberia
     :cheevo/desc   "Deliver to all container ports in Iberia (TS Atlas)."
     :cheevo/flag   :job.cheevo/lets-get-shipping}

    :fleet-builder
    {:cheevo/name   "Fleet Builder"
     :cheevo/region :iberia
     :cheevo/desc   "Deliver to all shipyards in Iberia (Ocean Solution Group)."
     :cheevo/flag   :job.cheevo/fleet-builder}

    :taste-the-sun
    {:cheevo/name   "Taste the Sun"
     :cheevo/region :iberia
     :cheevo/desc   "Deliver ADR cargo to all solar power plants in Iberia (Engeron)."
     :cheevo/flag   :job.cheevo/taste-the-sun}

    :iberian-pilgrimage
    {:cheevo/name   "Iberian Pilgrimage"
     :cheevo/region :iberia
     :cheevo/desc   "Deliver to A Coruña from Lisbon, Seville and Pamplona."
     :cheevo/flag   :job.cheevo/iberian-pilgrimage}}))

(def achievement-job-flags
  [:job.cheevo/along-the-black-sea
   :job.cheevo/captain
   :job.cheevo/cattle-drive
   :job.cheevo/check-in-check-out
   :job.cheevo/concrete-jungle
   :job.cheevo/exclave-transit
   :job.cheevo/fleet-builder
   #_:job.cheevo/gas-must-flow
   :job.cheevo/go-nuclear
   :job.cheevo/iberian-pilgrimage
   :job.cheevo/industry-standard
   :job.cheevo/lets-get-shipping
   :job.cheevo/like-a-farmer
   :job.cheevo/michaelangelo
   :job.cheevo/miner
   :job.cheevo/orient-express
   :job.cheevo/sailor
   #_:job.cheevo/scania-trucks-lover
   :job.cheevo/taste-the-sun
   :job.cheevo/turkish-delight
   #_:job.cheevo/volvo-trucks-lover
   :job.cheevo/whatever-floats-your-boat])

(def ^:private regions
  (pbir/static-table-resolver
    :region/id
    {:scandinavia    {:region/name "Scandinavia"}
     :baltic         {:region/name "Beyond the Baltic Sea"}
     :black-sea      {:region/name "Road to the Black Sea"}
     :italia         {:region/name "Italia"}
     :vive-la-france {:region/name "Vive la France"}
     :iberia         {:region/name "Iberia"}}))

(def index
  (pci/register
    [;; Top level
     achievements
     regions
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
     taste-the-sun
     turkish-delight
     whatever-floats-your-boat]))
