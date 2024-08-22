(ns ets.jobs.search.core
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]
   [datahike.api :as d]
   [ets.jobs.ats.interface :as ats]
   [ets.jobs.decrypt.interface :as decrypt]
   #_[ets.jobs.ets2.interface :as ets2]
   [ets.jobs.files.interface :as files]
   [ets.jobs.sii.interface :as sii])
  (:import
   [java.io File]))

;; :game is :ets2 or :ats

(defn- profile-root [game]
  (files/profile-root game))

(defn- profile-info [dir]
  (let [basics  (->> (File. dir "profile.sii")
                     decrypt/decode
                     sii/parse-profile-basics)]
    (assoc basics :profile/dir dir :profile/id (.getName dir))))

(defn all-profiles
  "Return the basics of all available profiles."
  [game]
  (into []
        (comp (filter #(.isDirectory %))
              (filter #(not= (first (.getName %)) \.))
              (map profile-info))
        (.listFiles (profile-root game))))

(defn- profile-by-id [game profile-id]
  (->> (all-profiles game)
       (filter #(= (:profile/id %) profile-id))
       first))

(defn- latest-save [{:profile/keys [dir]}]
  (-> dir io/file files/latest-save))

;; Game time =================================================================
;; The game tracks sim time as minutes from the epoch: Week 1, Monday, 00:00Z.
;; This breaks the time down into a handier map:
;; {:week 12, :day "Wednesday", :hour 0, :min 12}
(def ^:private day-length  (* 60 24))
(def ^:private week-length (* day-length 7))

(def ^:private days ["Mon" "Tue" "Wed" "Thu" "Fri" "Sat" "Sun"])

(defn- time-breakdown [epoch-mins]
  (let [in-week (mod epoch-mins week-length)
        in-day  (mod epoch-mins day-length)]
    {:game epoch-mins
     :week (inc (quot epoch-mins week-length))
     :day  (nth days (quot in-week day-length))
     :hour (quot in-day 60)
     :mins (mod epoch-mins 60)}))

(defn- economy-block [blocks]
  (->> blocks
       (filter (comp #{"economy"} :name :sii/struct))
       first))

(defn parse-latest-save
  "Reads and parses the most recent savegame for the given profile."
  [game profile-id]
  (let [profile (profile-by-id game profile-id)
        blocks  (-> profile latest-save decrypt/decode sii/parse-sii-raw)
        economy (economy-block blocks)]
    {:db      (case game
                #_#_:ets2 (ets2/ingest-sii blocks)
                :ats  (d/db (ats/ingest-sii blocks)))
     :game    game
     :profile profile
     :time    (let [{:keys [game-time time-zone time-zone-name]} economy]
                {:zone-name (->> time-zone-name
                                 (re-matches #"@@tz_(\w+)@@")
                                 second
                                 str/upper-case)
                 :time-zone time-zone
                 :cest      (time-breakdown game-time)
                 :local     (time-breakdown (+ game-time time-zone -120))})}))

(comment
  ;; TODO: Delivery order matters! Some achievements are about consecutive jobs.
  (def conn (-> (profile-by-id :ats "42726164656E")
                latest-save
                decrypt/decode
                sii/parse-sii-raw
                ats/ingest-sii))

  ;; ATS achievements - Track ================================================
  ;; Sea Dog - Deliver cargo to a port in Oakland and a port in SF - Track
  ;; Cheers! - Deliver cargo from all 3 vineyards in California - Track
  ;; Company Collector - Perform deliveries for at least 15 different companies - Track
  ;; High Five - Perfect delivery (no damage, no fines, in-time) 600+mi - Track
  ;; I Think I Like It - Finish 50 deliveries - Track
  ;; Gold Fever - Deliver to both quarries in Nevada - Track
  ;; Heavy, But Not a Bull in a China Shop! - Perfect Heavy Cargo delivery at least 1000mi - Track
  ;; Sky Harbor - Deliver to Phoenix airport - Track
  ;; Bigger Cargo, Bigger Profit - $100k on 5 consecutive Heavy Cargo jobs - Track
  ;; I Thought This Should Be Heavy?! - Deliver all heavy cargoes - Track
  ;; Sky Delivery - Deliver cargo to An-124 depot - Track
  ;; Lumberjack - Deliver from all timber harvest sites in Oregon - Track
  ;; Cabbage to Cabbage - Deliver vegatables over Cabbage Hill - Track
  ;; Size Matters - Special Transport delivery no damage and on time - Track
  ;; Go Big or Go Home - Complete deliveries on all oversize routes in current map - Track
  ;; Get (to) the Chopper! - Deliver a helicopter Special Transport no damage and on time - Track
  ;; One, Two, Three - Breathe! - Perfect Air Condition ST delivery - Track
  ;; Big in America! - Deliver all ST cargoes - Track
  ;; Your Dumper Has Arrived! - Deliver Haul Truck Hull, Chassis and Huge Tyres - Track
  ;; Home Sweet Home - Perfect delivery of Turnkey House ST - Track
  ;; Steel Wings - Deliver to an aerospace company in WA - Track
  ;; Keep Sailing - Deliver a boat to a marina in WA - Track
  ;; Terminal Terminus - Deliver a cargo to both port terminals in WA - Track
  ;; Over the Top - Drive through the forest road to timber harvest in Bellingham
  ;;   - Specific location, E of Bellingham WA
  ;;   - Track
  ;; Logged-in - Deliver all Forest Machinery cargoes - Track
  ;; Leave No Branch Behind! - Perfect deliveries of at least 3 FM jobs - Track
  ;; This One is Mine! - Visit all mines and quarries in Utah - Track
  ;; Some Like it Salty - Take a job from each branch of each company in SLC - Track
  ;; Pump It Up - Deliver 5 frac tanks to any oil drilling site in Utah - Track
  ;; Grown in Idaho - 5 deliveries of potatoes from Idaho farms - Track
  ;; Along the Snake River - Perfect deliveries (any order, direction) b/w:
  ;;    - Kennewick - Lewiston
  ;;    - Boise - Twin Falls
  ;;    - Twin Falls - Pocatello
  ;;    - Pocatello - Idaho Falls
  ;;    - **Track**
  ;; Energy From Above - Deliver a Tower and Nacelle to both Vitas Power sites in CO - Track
  ;; Gold Rush - 10 deliveries to or from NAMIQ's gold mine in CO - Track
  ;; Up and Away - 10 deliveries to Denver Airport - Track
  ;; Big Boy - Deliver Train Parts, Tamping Machine and Rails to or from the rail yard in Cheyenne - Track
  ;; Buffalo Bill - 10 perfect deliveries of cattle to livestock auctions in WY - Track
  ;; Zero Waste - 10 deliveries of Dumpster Bins and Paper Waste to Waste Transfer Stations in Montana, and at least 1 Garbage Truck cargo to/from anywhere in MT - Track
  ;; Power On! - Deliver Circuit Breakers cargo and Utility Poles cargo to the electric substations in Butte, Glasgow and Havre - Track
  ;; Major Miner - Perfect deliveries to:
  ;;    - Bull Mountains coal mine
  ;;    - Silane from the silane gas factory
  ;;    - Talc Power from the talc factory
  ;; Shoreside Delivery - Deliver cargo to or from all shipyards and cargo ports - Track
  ;; Farm Away - Delivery to or from all farms and livestock auctions - Track
  ;; Cotton Bloom - 10 deliveries of at least one Cotton Lint, Cotton Seed, Cotton Gin Harvester in TX - Track
  ;; School Bus Capital - Deliver 5 Bus Hood cargoes to and 5 School Bus cargoes from the bus factory in Tulsa - Track
  ;; Big Wheels Keep on Turning - 3 deliveries of Big Tyres from both tyre factories in Lawton and Ardmore - Track
  ;; Air Capital of the World - Deliver an Aircraft Wing and Jet Engine Inlets to or from every aviation depot in Wichita - Track
  ;; Grain of Salt - 6 deliveries from Hutchinson Salt Mine in KS. At least 2 to a Food Factory - Track
  ;; Vehicle Dealer - 3 deliveries from Utility Vehicle Factory in Lincoln to each RV dealer in Columbus, Norfolk, Omaha - Track
  ;; Agricultural Expert - Deliver corn, grain, potatoes, and soybeans from NE - Track

  
  ;; ATS achievements - Low priority
  ;; Warming Up - Drive 10k miles on deliveries - Low priority
  ;; Cha-Ching - Earn $100k on deliveries - Low priority
  ;; Not a Problem - Park a trailer - Low priority
  ;; Like a Boss - Park at a hard delivery point - Low priority
  ;; Pimp My Truck - Buy and apply a custom paintjob - Low priority
  ;; Powell's Trail - Visit the Colorado River sights - Low priority?
  ;; Time for Big Hauling - Deliver a Heavy Cargo Pack job - Low priority
  ;; How Heavy Am I? - Gross weight at least 175,000lbs - Low priority
  ;; Ferry Tales - Use a ferry to cross the water - Low priority
  ;; The Director - Sightseeing spots in Idaho - Low priority
  
  
  ;; ATS achievements - Out of scope
  ;; California Dreamin' - Discover every city in California - Out of scope
  ;; Rig Master - Buy your own truck - Out of scope
  ;; Final Makeover - Fully upgrade a garage - Out of scope
  ;; What's Your BMI? - Use a weigh station - Out of scope
  ;; Gas Guzzler - Use a gas station - Out of scope
  ;; Silver State - Discover every city in Nevada - Out of scope
  ;; Parking Challenge - 20 hard delivery options - Out of scope
  ;; Copper State - Discover every city in Arizona - Out of scope
  ;; Start Your Engine! - Get on the start of the Truck Racing circuit - Out of scope
  ;; The Land of Enchantment - Discover every city in New Mexico - Out of scope
  ;; Truck Stop Tour - Visit all large truck stops and rest stops in New Mexico - Out of scope?
  ;; Forest Shortcut - Discover shortcut through the forest - Out of scope
  ;; 1881 - Drive by the Billy The Kid Museum - Out of scope
  ;; The Beaver State - Discover every city in Oregon - Out of scope
  ;; Uplifting - Travel across the New Youngs Bay Bridge - Out of scope
  ;; Travel Oregon - Landmarks: Crater Lake, Crooked River Gorge, Thor's Well, and Yaquina Head Lighthouse - Out of scope
  ;; Evergreen State - Discover every city in Washington - Out of scope
  ;; Seattle Mole - Drive through both Seattle tunnels - Out of scope
  ;; Travel Washington - Visit Mt St. Helens, Grand Coulee Dam, Mount Rainier - Out of scope
  ;; Beehive State - Discover every city in Utah - Out of scope
  ;; It's Something - Find a sign that says "nothing" - Out of scope
  ;; Gem State - Discover every city in Utah - Out of scope
  ;; 45th Parallel - Visit 45th Parallel in Idaho - Out of scope
  ;; The Centennial State - Discover every city in Colorado - Out of scope 
  ;; Million Dollar Highway - Drive the famous bit of U.S. 550 in Colorado - Out of scope
  ;; Cruising High Below - Drive through the Eisenhower-Johnson Memorial Tunnel in both directions - Out of scope
  ;; Four Corners - Visit the Four Corners monument (AZ, CO, NM, UT) - Out of scope
  ;; The Equality State - Discover every city in Wyoming - Out of scope
  ;; Over Plains and Mountains - View all cutscenes in WY - Out of scope
  ;; Population 1 - Discover Buford WY - Out of scope
  ;; The Last Best Place - Discover every city in Montana - Out of scope
  ;; Big Sky Country - View cutscenes in MT - Out of scope
  ;; The Lone Star State - Discover every city in TX - Out of scope
  ;; Big Country Views - At least 25 cutscenes in Texas - Out of scope
  ;; Avid Historian - At least 30 historical markers - Out of scope
  ;; The Sooner State - Discover every city in Oklahoma - Out of scope
  ;; Can Do! - View cutscenes in OK - Out of scope
  ;; The Sunflower State - Discover every city in Kansas - Out of scope
  ;; Wheat State Explorer - View cutscenes in KS - Out of scope
  ;; Can You Keep a Secret? - Discover 3 unmarked roads in KS - Out of scope
  ;; The Cornhusker State - Discover every city in Nebraska - Out of scope
  ;; Nebraska Explorer - Cutscenes in NE - Out of scope
  
  
  


  ;; ETS2 achievements
  ;; I Am A GPS / Pathfinder - 60% and 100% of the map - unknown?
  ;; Choo-Choo - Use the train to cross the channel - trivial
  ;; Sardine - Use a boat - trivial
  ;; Head Hunter - Discover all recruitment agencies - track?
  ;; Successfully Docked - Use all ports in the game (sea and train ports) - track!
  ;; From the Comfort of Your Home - Buy a truck online - track?
  ;; Test Drive Limited - Drive 999km with a truck from each mfer
  ;;   - Owned trucks only
  ;;   - MAN, DAF, Mercedes-Benz, Renault, Iveco, Scania, Volvo
  ;;   - Track for sure
  ;; Just in Time! - Urgent=3 delivery at least 550km, complete with < 30m left - track
  ;; Long Hauler - Complete a delivery > 2000km - track
  ;; Reliable Contractor - Perform jobs for 15+ different companies - track!
  ;; Profit Hunter - > E130k and minimum 2200km
  ;;   - If I can figure out the value of a job, definitely show possibles!
  ;; Experience Beats All - Complete deliveries with all trailer types
  ;;   - Machinery, ADR cargo, Container, Refrigerated, Liquid cargo,
  ;;     Fragile cargo, Construction, Bulk cargo
  ;;   - Track and display
  ;; Job is Only Worth It If It's Done Well! - Perfect delivery 1000km+ - offers
  ;; Careerist - 5 jobs in a row, in time, no cargo damage, no autoparking
  ;;   - There are streak counters for these I think.
  ;; Friends Are Always Here to Help You - Use auto-parking - Track completion
  ;; All is Possible - Complete jobs with at least 30 different cargo - track!
  ;; Minimaxer - 20k XP for several consecutive jobs with the total distance below 10,000km
  ;;   - This one is tricky, but it is possible to track.
  ;;   - Look at the most recent jobs for those that average high enough to be
  ;;     plausible.
  ;;   - Then look ahead for offers that have high enough to be above the line.
  ;; National Company - Own a garage in every city in your headquarters country.
  ;;   - Possible, but low priority. The interface answers this one.
  ;; Property Magnate - Own a garage in every city - Low priority
  ;; Working with the Elite - Out of scope
  ;; Swimming in Success - Reach an average daily profit of E450k - Out of scope
  ;; Performance Optimizer - Achieve at least 75% of average garage productivity
  ;;     for 10 large garages in your company.
  ;;   - Out of scope
  ;; Aspects of Professionalism - 10 female + 10 male employees of max level
  ;;   - Out of scope
  ;; Zzzzz - Use a rest stop - Out of scope
  ;; Diesel, No Petrol! - Use a filling station - Out of scope
  ;; Parking Challenge - Complete 20 deliveries choosing the trailer delivery
  ;;     option which requires reversing. - Low priority
  ;; Volvo Trucks Lover - Deliver truck cargo from the Volvo factory to a dealer
  ;;   - Track
  ;; Scania Trucks Lover - Deliver truck cargo from the Scania factory to a dealer
  ;;   - Track
  ;; Sailor - Deliver yachts to all Scandinavian marinas - Track
  ;; Whatever Floats Your Boat - Deliver cargo to all container ports - Track
  ;; Cattle Drive - Complete a liverstock delivery to Scandinavia - Track
  ;; Sightseer - Discover all Scandinavian cities - Out of scope
  ;; Miner - Complete deliveries to all quarries in Scandinavia - Track
  ;; Aquaphobia - Drive between Kobenhavn and Malmo - Out of scope
  ;; Bon Voyage! - Discover all French cities - Out of scope
  ;; Landmark Tour - Discover landmarks of France - Low priority
  ;; Go Nuclear! - Deliver cargo to five nuclear plants in France - Track
  ;; Check-in, Check-out - Deliver cargo to all cargo airport terminals in France - Track
  ;; Gas Must Flow
  
  

  ;; Details of completed deliveries.
  ["280215" #_completion-game-time
   "company.volatile.aml_rail_str.cheyenne" #_source-location
   "company.volatile.st_met_wrk.alamosa" #_target-location
   "cargo.train_part" #_cargo
   "837" #_xp
   "8992" #_profit
   "638" #_distance-km
   "0.000" #_pct-cargo-damage          ;; eg. 0.017 for 1.7% damage
   "400"   #_time-left-to-expiry-guess ;; Can be negative, I have a few lates.
   "0"     #_urgency                   ;; 0, 1 or 2
   "0"     #_auto-parking?-guess       ;; 0 or 1, 1 meaning auto-park?
   "2"     #_parking-option-guess      ;; 1 -> Easy, 2 -> Normal, 3 -> Tough
   "0"     #_company-truck?-guess      ;; ie. 0 is own truck, 1 is quick job?
   "8992"  #_advertised-profit-guess   ;; after ferries are paid
   "200"   #_fines-guess               ;; dollars?
   "279099" #_start-time
   "vehicle.peterbilt.389" #_vehicle
   "637"        #_planned-distance-km  ;; <= the actual distance
   "compn"      #_job-type  ;; "quick" "compn" "freerm" "on_compn" "spec_oversize"
   ""           #_special-transport-route-name ;; Blank for others
   ""           #_mystery1                     ;; Always blank?
   "0"          #_double-trailer?-guess        ;; 125 0s, 104 1s... unclear in ATS
   "12496.500"  #_cargo-mass                   ;; Probably in kg?
   "1"          #_cargo-units                  ;; Matches the job, but meaning not clear
   ])
