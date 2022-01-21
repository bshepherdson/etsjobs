(ns ets.jobs.core
  (:require
    [clojure.java.io :as io]
    [clojure.string :as str]
    [ets.jobs.decrypt :as decrypt]
    [ets.jobs.sii-file :as sf]
    [ets.jobs.sii-file-text :as sft]
    [ets.jobs.util :as util])
  (:import
    [java.io File]))

(defn decode [file]
  (sf/parse-sii (decrypt/decode file)))

(defn sii-struct [s name]
  (first (filter #(= name (:name %)) (vals (:structures s)))))

(defn by-type [s typ]
  (for [[k d] (:data s)
        :when (= typ (:type d))]
    d))

(defn by-id [s id]
  (get-in s [:data id]))

(defn economy [s]
  (let [typ (sii-struct s "economy")]
    (first (by-type s (:id typ)))))

(defn current-time [s]
  (get (economy s) "game_time"))

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

(defn time-zone-names [raw]
  (let [[_ tz] (re-matches #"@@tz_(\w+)@@" raw)]
    (.toUpperCase tz)))

(defn local-time
  "Returns a more subjective time in the user's current local time zone.
  0 is week 1, Monday, 00:00Z.
  Returns {:zulu {:week 12 :day 'Wednesday' :hour 0 :min 12}
           :local {...}
           :tz    'CEST'}."
  [s]
  (let [now      (current-time s) ; Zulu minutes from game epoch.
        eco      (economy s)
        tz-delta (get eco "time_zone")]
    {:zulu  (time-breakdown now)
     :local (time-breakdown (+ now tz-delta))
     :tz    (time-zone-names (get eco "time_zone_name"))}))

(comment
  (let [now        (current-time p)
        companies  (get (economy p) "companies")
        c1         (by-id p (first companies))
        job-ids    (get c1 "job_offer")
        jobs       (map #(by-id p %) job-ids)
        ]
    jobs
    )

  (let [])
  (economy p)
  (all-jobs p)
  (local-time p)
  )

(defn all-jobs [s]
  (let [now (current-time s)]
    (for [[_ _ sender-company sender-city :as cid] (get (economy s) "companies")
          :let [c (by-id s cid)]
          oid (get c "job_offer")
          :let [o    (by-id s oid)
                exp  (get o "expiration_time")]
          :when (and (pos? exp)
                     (> exp now))]
      (let [[target-company target-city] (str/split (get o "target") #"\.")]
        {:origin          sender-city
         :sender          sender-company
         :recipient       target-company
         :destination     target-city
         :cargo           (get-in o ["cargo" 1])
         :expires-in-mins (- (get o "expiration_time") now)
         :distance        (get o "shortest_distance_km")
         :urgency         (get o "urgency")}))))


(defn concrete-jungle [{:keys [sender]}]
  (= "radus" sender))

(defn industry-standard
  "2 deliveries to every paper mill, loco factory, and furniture factory in
  the Baltic states.
  Those are LVR, Renat, Viljo Paperitehdas Oy, Estonian Paper AS, and VPF
  (lvr, renat, viljo_paper, ee_paper, viln_paper)."
  [{:keys [receiver]}]
  (#{"lvr" "renat" "viljo_paper" "ee_paper" "viln_paper"} receiver))

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

#_(defn taste-the-sun
  "Deliver ADR cargo to all solar power plants in Iberia (Engeron)."
  [{:keys [cargo recipient]}]
  (and (= "engeron" recipient)
       (adr-cargo? cargo)))


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

; TODO Test this one - no jobs found.
(defn sailor
  "Deliver yachts to all Scandinavian marinas (marina)."
  [{:keys [cargo recipient]}]
  (and (= "marina" recipient)
       (= "yacht" cargo)))

(def scandinavia-cities
  #{; Denmark
    "aalborg" "esbjerg" "frederikshavn" "gedser" "hirtshals" "kobenhavn" "odense"
    ; Norway
    "bergen" "kristiansand" "oslo" "stavanger"
    ; Sweden
    "goteborg" "helsingborg" "jonkoping" "kalmar" "kapellskar" "karlskrona"
    "linkoping" "malmo" "nynashamn" "orebro" "stockholm" "sodertalje"
    "trelleborg" "uppsala" "vasteraas" "vaxjo"})

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

; TODO Implement this one once I can see a "gas must flow" job.
(defn gas-must-flow
  "Deliver petrol/gasoline, diesel, or LPG to all truck stops in France."
  [_]
  false)


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


(def achievements
  {:concrete-jungle
   {:name   "Concrete Jungle"
    :region "Baltic"
    :pred   concrete-jungle}

   :industry-standard
   {:name   "Industry Standard"
    :region "Baltic"
    :pred   industry-standard}

   :exclave-transit
   {:name   "Exclave Transit"
    :region "Baltic"
    :pred   exclave-transit}


   :like-a-farmer
   {:name   "Like a Farmer"
    :region "Baltic"
    :pred   like-a-farmer}

   :turkish-delight
   {:name   "Turkish Delight"
    :region "Black Sea"
    :pred   turkish-delight}

   :along-the-black-sea
   {:name   "Along the Black Sea"
    :region "Black Sea"
    :pred   along-the-black-sea}

   :orient-express
   {:name   "Orient Express"
    :region "Black Sea"
    :pred   orient-express}

   :lets-get-shipping
   {:name   "Let's Get Shipping"
    :region "Iberia"
    :pred   lets-get-shipping}

   :fleet-builder
   {:name   "Fleet Builder"
    :region "Iberia"
    :pred   fleet-builder}

   :iberian-pilgrimage
   {:name   "Iberian Pilgrimage"
    :region "Iberia"
    :pred   iberian-pilgrimage}


   :volvo-trucks-lover
   {:name   "Volvo Trucks Lover"
    :region "Scandinavia"
    :pred   volvo-trucks-lover}

   :scania-trucks-lover
   {:name   "Scania Trucks Lover"
    :region "Scandinavia"
    :pred   scania-trucks-lover}

   :sailor
   {:name   "Sailor"
    :region "Scandinavia"
    :pred   sailor}

   :cattle-drive
   {:name   "Cattle Drive"
    :region "Scandinavia"
    :pred   cattle-drive}

   :whatever-floats-your-boat
   {:name   "Whatever Floats your Boat"
    :region "Scandinavia"
    :pred   whatever-floats-your-boat}

   :miner
   {:name   "Miner"
    :region "Scandinavia"
    :pred   miner}

   :go-nuclear
   {:name   "Go Nuclear"
    :region "France"
    :pred   go-nuclear}

   :check-in-check-out
   {:name   "Check In, Check Out"
    :region "France"
    :pred   check-in-check-out}

   :gas-must-flow
   {:name   "Gas Must Flow"
    :region "France"
    :pred   gas-must-flow}

   :captain
   {:name   "Captain"
    :region "Italy"
    :pred   captain}

   :michaelangelo
   {:name   "Michaelangelo"
    :region "Italy"
    :pred   michaelangelo}})


(def open-achievements
  #{:whatever-floats-your-boat
    :gas-must-flow
    :go-nuclear
    :along-the-black-sea
    :captain
    :turkish-delight
    :orient-express
    :check-in-check-out
    :concrete-jungle
    :like-a-farmer
    :sailor
    :lets-get-shipping
    :michaelangelo
    :scania-trucks-lover
    :industry-standard
    :exclave-transit
    :miner
    :fleet-builder
    :volvo-trucks-lover
    :iberian-pilgrimage})


(defn jobs-for [jobs {:keys [pred]}]
  (->> jobs
       (filter pred)
       (into #{})
       seq))

(defn achievable-jobs [s]
  (let [jobs (all-jobs s)]
    (into {}
          (for [[ak ach] achievements
                :when (open-achievements ak)]
            [ak (jobs-for jobs ach)]))))

(defn profile-info [dir]
  (let [basics  (->> (File. dir "profile.sii")
                     decrypt/decode
                     sft/parse-profile-basics)]
    (assoc basics :dir dir)))

(defn profiles [dir]
  (for [p (.listFiles dir)]
    (profile-info p)))

(defn parse-latest [profile]
  (->> profile
       (io/file)
       util/latest-save
       (#(do (prn %) %)) ; Dumps the filename.
       decrypt/decode
       sf/parse-sii))

(comment

  (def prof (->> "samples/426973686F70/profile.sii"
                 decrypt/decode
                 sft/parse-profile-basics
                 ))
  (def profile-dir (->> (profiles (clojure.java.io/file "samples"))
                        (filter #(= "Bishop2" (:name %)))
                        first
                        :dir))

  (def p (->> (File. (File. (File. profile-dir "save") "1") "game.sii")
              decrypt/decode
              sf/parse-sii))

  (identity p)
  (take 4 (:data p))

  (:structures p)
  (select-keys (economy p) ["game_time" "time_zone" "time_zone_name"])

  (get-in p [:structures 14])
  (sii-struct p "job_offer")
  (by-id p (-> (by-type p 19)
               first
               (get "data")
               first))
  ;now: 16425752
  (all-jobs p)
  (by-type p 1)
  (by-id p ["company" "volatile" "ibp" "helsinki"])
  ; job offers [2265125696736 2265125696320 2265125698816 2265125699856]
  (by-id p 2265125696736 )
  (by-id p 2265125696320 )
  (by-id p 2265125698816 )
  (by-id p 2265125699856 )

  (defn by-id [id]
    (get-in p [:data id]))

  (def cargos (into #{} (map :cargo (all-jobs p))))
  (count (all-jobs p))

  (def companies (clojure.set/union (into #{} (map :sender    (all-jobs p)))
                                    (into #{} (map :recipient (all-jobs p)))))
  (def cities (clojure.set/union (into #{} (map :origin (all-jobs p)))
                                 (into #{} (map :destination (all-jobs p)))))

  (identity cities)

  ; Predicate for "Concrete Jungle": sender is Radus.
  (filter  (all-jobs p))
  (current-time p)

  (get-in p [:structures 20])

  )

