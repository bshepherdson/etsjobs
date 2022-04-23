(ns ets.jobs.ets2.achievements
  (:require
    [ets.jobs.ets2.map :as map]))

(defn baltic? [city-slug]
  (#{"EST" "FI" "LT" "LV" "RU"} (:c (map/cities city-slug))))

(defn concrete-jungle [{:keys [sender origin]}]
  (and (baltic? origin)
       (= "radus" sender)))

(defn industry-standard
  "2 deliveries to every paper mill, loco factory, and furniture factory in
  the Baltic states.
  Those are LVR, Renat, Viljo Paperitehdas Oy, Estonian Paper AS, and VPF
  (lvr, renat, viljo_paper, ee_paper, viln_paper)."
  [{:keys [recipient]}]
  (#{"lvr" "renat" "viljo_paper" "ee_paper" "viln_paper"} recipient))

(def russian-main-cities
  #{"luga" "pskov" "petersburg" "sosnovy_bor" "vyborg"})

(defn exclave-transit
  "Delivery from Kaliningrad to other Russian cities."
  [{:keys [origin destination]}]
  (and (= "kaliningrad" origin)
       (russian-main-cities destination)))

(defn like-a-farmer
  "Deliver to each farm in the Baltic region.
  Agrominta UAB (agrominta, agrominta_a; both near Utena LT)
  Eviksi (eviksi, eviksi_a; (double near Liepaja LV))
  Maatila Egres (egres)
  Onnelik talu (onnelik, onnelik_a; double near Parna EST)
  Zelenye Polja (zelenye, zelenye_a)"
  [{:keys [recipient]}]
  (#{"agrominta" "agrominta_a"
     "eviksi" "eviksi_a"
     "egres"
     "onnelik" "onnelik_a"
     "zelenye" "zelenye_a"}
    recipient))

(defn turkish-delight
  [{:keys [origin distance]}]
  (and (>= distance 2500)
       (= "istanbul" origin)))

(defn along-the-black-sea
  "Perfect deliveries either direction between these pairs:
  Istanbul-Burgas
  Burgas-Varna
  Varna-Mangalia
  Mangalia-Constanta"
  [{:keys [origin destination]}]
  (or (and (= origin      "istanbul")
           (= destination "burgas"))
      (and (= origin      "burgas")
           (= destination "istanbul"))
      (and (= origin      "burgas")
           (= destination "varna"))
      (and (= origin      "varna")
           (= destination "burgas"))
      (and (= origin      "varna")
           (= destination "mangalia"))
      (and (= origin      "mangalia")
           (= destination "varna"))
      (and (= origin      "mangalia")
           (= destination "constanta"))
      (and (= origin      "constanta")
           (= destination "mangalia"))))

(defn orient-express
  [{:keys [origin destination]}]
  (or (and (= origin      "paris")
           (= destination "strasbourg"))
      (and (= origin      "strasbourg")
           (= destination "munchen"))
      (and (= origin      "munchen")
           (= destination "wien"))
      (and (= origin      "wien")
           (= destination "budapest"))
      (and (= origin      "budapest")
           (= destination "bucuresti"))
      (and (= origin      "bucuresti")
           (= destination "istanbul"))))

(defn lets-get-shipping
  "Deliver to all container ports in Iberia (TS Atlas)."
  [{:keys [recipient]}]
  (= "ts_atlas" recipient))

(defn fleet-builder
  "Deliver to all shipyards in Iberia (Ocean Solution Group, ocean_sol)."
  [{:keys [recipient]}]
  (= "ocean_sol" recipient))

(defn iberian-pilgrimage
  "Deliver from Lisbon, Seville and Pamplona to A Coruna."
  [{:keys [origin destination]}]
  (and (= destination "a_coruna")
       (#{"lisboa" "sevilla" "pamplona"} origin)))

(defn taste-the-sun
  "Deliver ADR cargo to all solar power plants in Iberia (Engeron)."
  [{:keys [cargo recipient]}]
  (and (= "engeron" recipient)
       (:adr (map/cargos cargo))))


; TODO Test this one - no jobs found.
(defn volvo-trucks-lover
  "Deliver trucks from the Volvo factory to a dealer."
  [{:keys [cargo sender]}]
  (and (= "volvo_fac" sender)
       (= "trucks" cargo)))

; TODO Test this one - no jobs found.
(defn scania-trucks-lover
  "Deliver trucks from Scania factory to a dealer."
  [{:keys [cargo sender]}]
  (and (= "scania_fac" sender)
       (= "trucks" cargo)))

(def scandinavia-cities
  #{; Denmark
    "aalborg" "esbjerg" "frederikshavn" "gedser" "hirtshals" "kobenhavn" "odense"
    ; Norway
    "bergen" "kristiansand" "oslo" "stavanger"
    ; Sweden
    "goteborg" "helsingborg" "jonkoping" "kalmar" "kapellskar" "karlskrona"
    "linkoping" "malmo" "nynashamn" "orebro" "stockholm" "sodertalje"
    "trelleborg" "uppsala" "vasteraas" "vaxjo"})

(defn sailor
  "Deliver yachts to all Scandinavian marinas (marina)."
  [{:keys [cargo destination recipient]}]
  (and (scandinavia-cities destination)
       (= "marina" recipient)
       (= "yacht" cargo)))

(defn cattle-drive
  "Complete a livestock delivery to Scandinavia."
  [{:keys [destination cargo]}]
  (and (= "livestock" cargo)
       (scandinavia-cities destination)))

(defn whatever-floats-your-boat
  "Deliver to all container ports in Scandinavia (cont_port)."
  [{:keys [destination recipient]}]
  (and (= "cont_port" recipient)
       (scandinavia-cities destination)))

(defn miner
  "Deliver to all quarries in Scandinavia (nord_sten, ms_stein)."
  [{:keys [destination recipient]}]
  (and (#{"nord_sten" "ms_stein"} recipient)
       (scandinavia-cities destination)))

; France
(def french-reactors
  #{"civaux" "golfech" "paluel" "alban" "laurent"})

(defn go-nuclear
  "Deliver to 5 nuclear plants in France (nucleon)."
  [{:keys [destination recipient]}]
  (and (= "nucleon" recipient)
       (french-reactors destination)))

(def french-airports
  #{"bastia" "brest" "calvi" "clermont" "montpellier"
    "nantes" "paris" "toulouse"})

(defn check-in-check-out
  "Deliver to all cargo airport terminals in France (fle, in France.)"
  [{:keys [destination recipient]}]
  (and (= "fle" recipient)
       (french-airports destination)))

(defn gas-must-flow
  "Deliver petrol/gasoline, diesel, or LPG to all truck stops in France."
  [{:keys [cargo destination recipient]}]
  (and (= "eco" recipient)
       (#{"petrol" "gasoline" "diesel" "lpg"} cargo)
       (= "F" (-> (get map/cities destination) :c))))


; Italy
(defn captain
  "Deliver to all Italian shipyards (c_navale)."
  [{:keys [recipient]}]
  (= "c_navale" recipient))

(defn michaelangelo
  "Deliver from the Carrara quarry (marmo in Livorno)."
  [{:keys [origin sender]}]
  (and (= "marmo" sender)
       (= "livorno" origin)))


(def ^:private ets-regions
  {:scandinavia
   {:name "Scandinavia"
    :achievements
    [{:key  :whatever-floats-your-boat
      :name "Whatever Floats Your Boat"
      :desc "Deliver to all container ports in Scandinavia (Container Port)."
      :pred whatever-floats-your-boat}

     {:key  :sailor
      :name "Sailor"
      :desc "Deliver yachts to all Scandinavian marinas (boat symbol)."
      :pred sailor}

     {:key  :volvo-trucks-lover
      :name "Volvo Trucks Lover"
      :desc "Deliver trucks from the Volvo factory."
      :pred volvo-trucks-lover}
     {:key  :scania-trucks-lover
      :name "Scania Trucks Lover"
      :desc "Deliver trucks from the Scania factory."
      :pred scania-trucks-lover}

     {:key  :cattle-drive
      :name "Cattle Drive"
      :desc "Complete a livestock delivery to Scandinavia."
      :pred cattle-drive}

     {:key  :miner
      :name "Miner"
      :desc "Deliver to all quarries in Scandinavia (Nordic Stenbrott, MS Stein)."
      :pred miner}]}

   :baltic
   {:name "Beyond the Baltic Sea"
    :achievements
    [{:key  :concrete-jungle
      :name "Concrete Jungle"
      :desc "Complete 10 deliveries from concrete plants (Radus, Радус)"
      :pred concrete-jungle}

     {:key  :industry-standard
      :name "Industry Standard"
      :desc (str "Complete 2 deliveries to every paper mill, loco factory, and"
                 "furniture maker in the Baltic region (LVR, Renat, Viljo "
                 "Paperitehdas Oy, Estonian Paper AS, VPF).")
      :pred industry-standard}

     {:key  :exclave-transit
      :name "Exclave Transit"
      :desc "Complete 5 deliveries from Kaliningrad to any other Russian city."
      :pred exclave-transit}

     {:key  :like-a-farmer
      :name "Like a Farmer"
      :desc "Deliver to each farm in the Baltic. (Agrominta UAB, Eviksi, Maatila Egres, Onnelik Talu, Zelenye Polja)"
      :pred like-a-farmer}]}

   :black-sea
   {:name "Road to the Black Sea"
    :achievements
    [{:key  :turkish-delight
      :name "Turkish Delight"
      :desc "Complete 3 deliveries from Istanbul which are at least 2500km long."
      :pred turkish-delight}

     {:key  :along-the-black-sea
      :name "Along the Black Sea"
      :desc "Complete perfect deliveries in any order or direction between these coastal cities."
      :pred along-the-black-sea}

     {:key  :orient-express
      :name "Orient Express"
      :desc "Complete deliveries between the following cities, in order: Paris, Strasbourg, Munich, Vienna, Budapest, Bucharest, Istanbul. (Requires Going East as well!)"
      :pred orient-express}]}

   :italia
   {:name "Italia"
    :achievements
    [{:key  :captain
      :name "Captain"
      :desc "Deliver to all Italian shipyards. (Cantiare Navale)"
      :pred captain}

     {:key  :michaelangelo
      :name "Michaelangelo"
      :desc "Deliver from the Carrara quarry (Marmo SpA in Livorno)."
      :pred michaelangelo}]}

   :vive-la-france
   {:name "Vive la France"
    :achievements
    [{:key  :go-nuclear
      :name "Go Nuclear"
      :desc "Deliver to five nuclear power plants in France. (Nucleon)"
      :pred go-nuclear}

     {:key  :check-in-check-out
      :name "Check in, Check out"
      :desc "Deliver to all cargo airport terminals in France (FLE)."
      :pred check-in-check-out}

     {:key  :gas-must-flow
      :name "Gas Must Flow"
      :desc "Deliver diesel, LPG or gasoline/petrol to all truck stops in France. (Eco)"
      :pred gas-must-flow}]}

   :iberia
   {:name "Iberia"
    :achievements
    [{:key  :lets-get-shipping
      :name "Let's Get Shipping"
      :desc "Deliver to all container ports in Iberia (TS Atlas)."
      :pred lets-get-shipping}

     {:key  :fleet-builder
      :name "Fleet Builder"
      :desc "Deliver to all shipyards in Iberia (Ocean Solution Group)."
      :pred fleet-builder}

     {:key  :taste-the-sun
      :name "Taste the Sun"
      :desc "Deliver ADR cargo to all solar power plants in Iberia (Engeron)."
      :pred taste-the-sun}

     {:key  :iberian-pilgrimage
      :name "Iberian Pilgrimage"
      :desc "Deliver to A Coruña from Lisbon, Seville and Pamplona."
      :pred iberian-pilgrimage}]}})


(def ^:private ets-open-achievements
  #{:along-the-black-sea
    :captain
    :check-in-check-out
    :concrete-jungle
    :exclave-transit
    :fleet-builder
    :gas-must-flow
    :go-nuclear
    :iberian-pilgrimage
    :industry-standard
    :lets-get-shipping
    :like-a-farmer
    :michaelangelo
    :miner
    :orient-express
    :sailor
    :scania-trucks-lover
    :taste-the-sun
    :turkish-delight
    :volvo-trucks-lover
    :whatever-floats-your-boat})

(def ets-meta
  {:regions ets-regions
   :open    ets-open-achievements})

