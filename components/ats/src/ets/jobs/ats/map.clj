(ns ets.jobs.ats.map
  (:require
   [ets.jobs.files.interface :as files]
   [ets.jobs.scs.interface :as scs]))

#_(def ^:private state-dlc-codes
  ["al" "ar" "ar" #_"ca" "co" "ct" "de" "fl" "ga" "hi" "id" "il" "in" "ia" "ks"
   "ky" "la" "me" "md"   "ma" "mi" "mn" "ms" "mo" "mt" "ne" "nh" "nj" "nm" "ny"
   "nc" "nd" "oh" "ok"   "or" "pa" "ri" "sc" "sd" "tn" "tx" "ut" "vt" "va" "wa"
   "wv" "wi" "wy" "arizona" "nevada"])

(def ^:private sii-countries-to-states
  {"alabama"        :state/al
   "alaska"         :state/ar
   "arizona"        :state/az
   "arkansas"       :state/ar
   "california"     :state/ca
   "colorado"       :state/co
   "connecticut"    :state/ct
   "delaware"       :state/de
   "florida"        :state/fl
   "georgia"        :state/ga
   "hawaii"         :state/hi ;; Lol seems unlikely.
   "idaho"          :state/id
   "illinois"       :state/il
   "indiana"        :state/in
   "iowa"           :state/ia
   "kansas"         :state/ks
   "kentucky"       :state/ky
   "louisiana"      :state/la
   "maine"          :state/me
   "maryland"       :state/md
   "massachusetts"  :state/ma
   "michigan"       :state/mi
   "minnesota"      :state/mn
   "mississippi"    :state/ms
   "missouri"       :state/mo
   "montana"        :state/mt
   "nebraska"       :state/ne
   "nevada"         :state/nv
   "new_hampshire"  :state/nh
   "new_jersey"     :state/nj
   "new_mexico"     :state/nm
   "new_york"       :state/ny
   "north_carolina" :state/nc
   "north_dakota"   :state/nd
   "ohio"           :state/oh
   "oklahoma"       :state/ok
   "oregon"         :state/or
   "pennsylvania"   :state/pa
   "rhode_island"   :state/ri
   "south_carolina" :state/sc
   "south_dakota"   :state/sd
   "tennessee"      :state/tn
   "texas"          :state/tx
   "utah"           :state/ut
   "vermont"        :state/vt
   "virginia"       :state/va
   "washington"     :state/wa
   "west_virginia"  :state/wv
   "wisconsin"      :state/wi
   "wyoming"        :state/wy})

(def ^:private tx-states
  [{:db/ident :state/al, :state/name "Alabama"}
   {:db/ident :state/ak, :state/name "Alaska"}
   {:db/ident :state/az, :state/name "Arizona"}
   {:db/ident :state/ar, :state/name "Arkansas"}
   {:db/ident :state/ca, :state/name "California"}
   {:db/ident :state/co, :state/name "Colorado"}
   {:db/ident :state/ct, :state/name "Connecticut"}
   {:db/ident :state/de, :state/name "Delaware"}
   {:db/ident :state/fl, :state/name "Florida"}
   {:db/ident :state/ga, :state/name "Georgia"}
   {:db/ident :state/hi, :state/name "Hawaii"} ;; Lol seems unlikely.
   {:db/ident :state/id, :state/name "Idaho"}
   {:db/ident :state/il, :state/name "Illinois"}
   {:db/ident :state/in, :state/name "Indiana"}
   {:db/ident :state/ia, :state/name "Iowa"}
   {:db/ident :state/ks, :state/name "Kansas"}
   {:db/ident :state/ky, :state/name "Kentucky"}
   {:db/ident :state/la, :state/name "Louisiana"}
   {:db/ident :state/me, :state/name "Maine"}
   {:db/ident :state/md, :state/name "Maryland"}
   {:db/ident :state/ma, :state/name "Massachusetts"}
   {:db/ident :state/mi, :state/name "Michigan"}
   {:db/ident :state/mn, :state/name "Minnesota"}
   {:db/ident :state/ms, :state/name "Mississippi"}
   {:db/ident :state/mo, :state/name "Missouri"}
   {:db/ident :state/mt, :state/name "Montana"}
   {:db/ident :state/ne, :state/name "Nebraska"}
   {:db/ident :state/nv, :state/name "Nevada"}
   {:db/ident :state/nh, :state/name "New Hampshire"}
   {:db/ident :state/nj, :state/name "New Jersey"}
   {:db/ident :state/nm, :state/name "New Mexico"}
   {:db/ident :state/ny, :state/name "New York"}
   {:db/ident :state/nc, :state/name "North Carolina"}
   {:db/ident :state/nd, :state/name "North Dakota"}
   {:db/ident :state/oh, :state/name "Ohio"}
   {:db/ident :state/ok, :state/name "Oklahoma"}
   {:db/ident :state/or, :state/name "Oregon"}
   {:db/ident :state/pa, :state/name "Pennsylvania"}
   {:db/ident :state/ri, :state/name "Rhode Island"}
   {:db/ident :state/sc, :state/name "South Carolina"}
   {:db/ident :state/sd, :state/name "South Dakota"}
   {:db/ident :state/tn, :state/name "Tennessee"}
   {:db/ident :state/tx, :state/name "Texas"}
   {:db/ident :state/ut, :state/name "Utah"}
   {:db/ident :state/vt, :state/name "Vermont"}
   {:db/ident :state/va, :state/name "Virginia"}
   {:db/ident :state/wa, :state/name "Washington"}
   {:db/ident :state/wv, :state/name "West Virginia"}
   {:db/ident :state/wi, :state/name "Wisconsin"}
   {:db/ident :state/wy, :state/name "Wyoming"}])

(defn- dlc-files [dlc-name]
  (let [dlc-name (files/strip-extension dlc-name)]
    {:scs     (str dlc-name ".scs")
     :company (str "def/company." dlc-name ".sii")
     :city    (str "def/city."    dlc-name ".sii")
     :cargo   (str "def/cargo."   dlc-name ".sii")}))

(defn- tx-city [{:keys [city_name]
                 [state]      :country
                 [_city slug] :sii/block-id}]
  {:city/ident slug
   :city/name  city_name
   :city/state (sii-countries-to-states state)})

(defn- tx-company [{:keys [name]
                    [_company _permanent slug] :sii/block-id}]
  {:company/ident slug
   :company/name  name})

(def ^:private adr-classes
  {"1" :cargo.adr/explosive
   "2" :cargo.adr/gases
   "3" :cargo.adr/flammable-liquids
   "4" :cargo.adr/flammable-solids
   "6" :cargo.adr/poison
   "8" :cargo.adr/corrosive})

(comment

 (def f (scs/scs-file :ats "def.scs"))
 (scs/directory-listing f "def/cargo")
 (scs/scs->text-sii f "def/cargo_mapping.sii")
 #_(scs/scs->text-sii []))

(defn- tx-cargo [{:keys [adr_class name]
                  [_cargo slug] :sii/block-id}]
 )

(defn- read-game-file [{:keys [cargo city company scs]}]
  (when-let [file (scs/scs-file :ats scs)]
    {:cities    (into [] (comp (filter #(= (:type %) "city_data"))
                               (map tx-city))
                      (scs/scs->text-sii file city))
     :companies (into [] (comp (filter #(= (:type %) "company_permanent"))
                               (map tx-company))
                      (scs/scs->text-sii file company))
     #_#_:cargo     (into [] (scs/scs->text-sii file cargo))}))

(def ^:private core-files
  {:scs     "def.scs"
   :cargo   "def/cargo.sii"
   :city    "def/city.sii"
   :company "def/company.sii"})

(defn- read-game-files []
  (->> (files/scs-files :ats)
       (remove #{"def.scs"})
       (map dlc-files)
       (cons core-files)
       (mapv read-game-file)))

(comment
  (def specs (read-game-files))
  (def cargoes (into [] (mapcat :cargo) (read-game-files)))

  (->> cargoes
       (take 5)
       )

  (filter #(= (:sii/block-id %) ["cargo" "cable"]) cargoes))

(def ^:private tx-game-files
  (delay
    (let [m (reduce (partial merge-with concat) {} (read-game-files))]
      (into [] cat [(:cities m) (:companies m) (:cargo m)]))))

(def ^:private cargoes
  "List of cargo data for ATS."
  [{:cargo/ident "aircraft_eng"  :cargo/name "Aircraft Engine"}
   {:cargo/ident "air_eng2"      :cargo/name "Aircraft Engine?"}
   {:cargo/ident "aircon"        :cargo/name "Air Conditioner?? FIXME"}
   {:cargo/ident "aircondition"  :cargo/name "Air Conditioning Complex?"}
   {:cargo/ident "almond_box"    :cargo/name "Boxed Almonds?? FIXME"}
   {:cargo/ident "ammonia"       :cargo/name "Ammonia?"
    :cargo/adr #{:cargo.adr/gases}}
   {:cargo/ident "aromatics"     :cargo/name "Aromatics"
    :cargo/adr #{:cargo.adr/flammable-liquids}}
   {:cargo/ident "asph_miller"   :cargo/name "Milling Machine"
    :cargo/heavy? true}
   {:cargo/ident "beverages"     :cargo/name "Beverages"}
   {:cargo/ident "beet_harvest"  :cargo/name "Beet Harvester?? FIXME"}
   {:cargo/ident "big_tyres"     :cargo/name "Big Tyres"}
   {:cargo/ident "boat"          :cargo/name "Boat"}
   {:cargo/ident "boom_lift"     :cargo/name "Boom Lift? (1)"}
   {:cargo/ident "boom_lift2"    :cargo/name "Boom Lift? (2)"}
   {:cargo/ident "bottles"       :cargo/name "Empty Bottles"}
   {:cargo/ident "bulldozer"     :cargo/name "Bulldozer"}
   {:cargo/ident "bus_hood"      :cargo/name "Bus Hood?"}
   {:cargo/ident "butter"        :cargo/name "Butter"}
   {:cargo/ident "buttermilk"    :cargo/name "Buttermilk?"}
   {:cargo/ident "cable"         :cargo/name "Cable Reel"
    :cargo/heavy? true}
   {:cargo/ident "cans"          :cargo/name "Cans??"}
   {:cargo/ident "carcomp"       :cargo/name "carcomp???"}
   {:cargo/ident "cars_big"      :cargo/name "Cars"}
   {:cargo/ident "cars_big2"     :cargo/name "Cars"}
   {:cargo/ident "cars_mix"      :cargo/name "Cars"}
   {:cargo/ident "cars_pickup"   :cargo/name "Cars"}
   {:cargo/ident "cars_pick_tt"  :cargo/name "Cars"}
   {:cargo/ident "cars_small"    :cargo/name "Cars"}
   {:cargo/ident "cars_rv"       :cargo/name "RVs??"}
   {:cargo/ident "cattle"        :cargo/name "Live Cattle"}
   {:cargo/ident "cement"        :cargo/name "Cement"}
   {:cargo/ident "cheese"        :cargo/name "Cheese"}
   {:cargo/ident "circuit_brk"   :cargo/name "Circuit Breakers??"}
   {:cargo/ident "clothes"       :cargo/name "Clothes"}
   {:cargo/ident "chlorine_t"    :cargo/name "Chlorine"
    :cargo/adr #{:cargo.adr/gases}}
   {:cargo/ident "coil"          :cargo/name "Metal Coil?"}
   {:cargo/ident "colors"        :cargo/name "colors???"}
   {:cargo/ident "computers"     :cargo/name "Computers"}
   {:cargo/ident "conc_barr"     :cargo/name "Concrete Barriers??"}
   {:cargo/ident "const_house"   :cargo/name "Construction Houses"}
   {:cargo/ident "corn_b"        :cargo/name "Corn B?? FIXME"}
   {:cargo/ident "cott_harvest"  :cargo/name "Cotton Harvester??"}
   {:cargo/ident "cott_lint"     :cargo/name "Cotton Lint?? FIXME"}
   {:cargo/ident "cott_seed"     :cargo/name "Cotton Seeds??"}
   {:cargo/ident "crude_oil"     :cargo/name "Crude Oil"
    :cargo/adr #{:cargo.adr/flammable-liquids}}
   {:cargo/ident "ctubes"        :cargo/name "Concrete Tubes (small?)"}
   {:cargo/ident "ctubes_b"      :cargo/name "Concrete Tubes (large?)"}
   {:cargo/ident "curtains"      :cargo/name "Curtains"}
   {:cargo/ident "diesel"        :cargo/name "Diesel"
    :cargo/adr #{:cargo.adr/flammable-liquids}}
   {:cargo/ident "digger1000"    :cargo/name "Wheel Loader"}
   {:cargo/ident "digger500"     :cargo/name "Backhoe Loader"}
   {:cargo/ident "dozer"         :cargo/name "Heavy Bulldozer"
    :cargo/heavy? true}
   {:cargo/ident "dry_fruit"     :cargo/name "Dry Fruits"}
   {:cargo/ident "dry_milk"      :cargo/name "Dry Milk"}
   {:cargo/ident "dumper"        :cargo/name "Dumper Chassis?? FIXME"}
   {:cargo/ident "dumper_hull"   :cargo/name "Dumper Hull?? FIXME"}
   {:cargo/ident "dumper_tire"   :cargo/name "Dumper Tire?? FIXME"}
   {:cargo/ident "dumpster"      :cargo/name "Dumpster??"}
   {:cargo/ident "dynamite"      :cargo/name "Dynamite"
    :cargo/adr #{:cargo.adr/explosive}}
   {:cargo/ident "electro_comp"  :cargo/name "Electronic Components"}
   {:cargo/ident "empty_palet"   :cargo/name "Empty Pallets"}
   {:cargo/ident "emptytank"     :cargo/name "Reservoir Tank?"}
   {:cargo/ident "ethane"        :cargo/name "Ethane"
    :cargo/adr #{:cargo.adr/flammable-liquids}}
   {:cargo/ident "excavator"     :cargo/name "Excavator"}
   {:cargo/ident "fert_tank"     :cargo/name "Fertilizer Tank??"}
   {:cargo/ident "fertilizer"    :cargo/name "Fertilizer"}
   {:cargo/ident "fireworks"     :cargo/name "Fireworks"
    :cargo/adr #{:cargo.adr/explosive}}
   {:cargo/ident "flour"         :cargo/name "Flour"}
   {:cargo/ident "food"          :cargo/name "Packaged Food?"}
   {:cargo/ident "forklifts"     :cargo/name "Forklifts"}
   {:cargo/ident "forwarder"     :cargo/name "Forwarder"}
   {:cargo/ident "frac_tank"     :cargo/name "Frac Tank"}
   {:cargo/ident "frozen_food"   :cargo/name "Frozen Food"}
   {:cargo/ident "frozen_fruit"  :cargo/name "Frozen Fruits"}
   {:cargo/ident "frozen_veget"  :cargo/name "Frozen Vegetables"}
   {:cargo/ident "fruit_juic_t"  :cargo/name "Fruit Juice?"}
   {:cargo/ident "fruits"        :cargo/name "Fruits"}
   {:cargo/ident "furniture"     :cargo/name "Furniture"}
   {:cargo/ident "garbage_trck"  :cargo/name "Garbage Truck?"}
   {:cargo/ident "generator_c"   :cargo/name "Power Generator"}
   {:cargo/ident "gen_set"       :cargo/name "Generator Set"}
   {:cargo/ident "glass"         :cargo/name "Glass"}
   {:cargo/ident "glass_rack"    :cargo/name "Glass Rack??"}
   {:cargo/ident "grain"         :cargo/name "Grain"}
   {:cargo/ident "grain_b"       :cargo/name "Grain B?? FIXME"}
   {:cargo/ident "gravel"        :cargo/name "Gravel"}
   {:cargo/ident "guard_rails"   :cargo/name "Guard Rails"}
   {:cargo/ident "gypsum"        :cargo/name "Gypsum"}
   {:cargo/ident "harvester"     :cargo/name "Harvester"}
   {:cargo/ident "hay"           :cargo/name "Hay"}
   {:cargo/ident "helicopter"    :cargo/name "Transport Helicopter"}
   {:cargo/ident "hipresstank"   :cargo/name "Pressure Tank"}
   {:cargo/ident "home_acc"      :cargo/name "Home Accessories"}
   {:cargo/ident "house"         :cargo/name "House?? FIXME"}
   {:cargo/ident "house_pref"    :cargo/name "House Prefabs"}
   {:cargo/ident "househd_appl"  :cargo/name "Household Appliances"}
   {:cargo/ident "hydrochlor"    :cargo/name "Hydrochloric Acid"
    :cargo/adr #{:cargo.adr/corrosive}}
   {:cargo/ident "ibc_cont"      :cargo/name "IBC Containers"}
   {:cargo/ident "icecream"      :cargo/name "Ice Cream"}
   {:cargo/ident "ind_palet"     :cargo/name "Industrial Pallet?? FIXME"}
   {:cargo/ident "ins_panel"     :cargo/name "Insulation Panels"}
   {:cargo/ident "jet_wing"      :cargo/name "Aircraft Wing"}
   {:cargo/ident "kb_loader"     :cargo/name "Knuckleboom Loader"}
   {:cargo/ident "kw_t680"       :cargo/name "Kenworth Trucks"}
   {:cargo/ident "largetubes"    :cargo/name "Large Tubes"}
   {:cargo/ident "lattice"       :cargo/name "lattice??? FIXME"}
   {:cargo/ident "lemonade"      :cargo/name "Lemonade??"}
   {:cargo/ident "lift_truck"    :cargo/name "Lift Truck"
    :cargo/heavy? true}
   {:cargo/ident "lift_truck_s"  :cargo/name "Lift Truck Chassis"
    :cargo/heavy? true}
   {:cargo/ident "limestone"     :cargo/name "Limestone"}
   {:cargo/ident "log_harvest"   :cargo/name "Log Harvester"}
   {:cargo/ident "log_loader"    :cargo/name "Log Loader"}
   {:cargo/ident "log_stacker"   :cargo/name "Log Stacker"}
   {:cargo/ident "logs"          :cargo/name "Logs"}
   {:cargo/ident "lpg_t"         :cargo/name "LPG??"}
   {:cargo/ident "lumber"        :cargo/name "Lumber (beams?)"}
   {:cargo/ident "lumber_b"      :cargo/name "Lumber (boards?)"}
   {:cargo/ident "lye"           :cargo/name "Lye"
    :cargo/adr #{:cargo.adr/poison}}
   {:cargo/ident "machine_pts"   :cargo/name "Machine Parts"}
   {:cargo/ident "mbt"           :cargo/name "Mobile Barrier?"}
   {:cargo/ident "meat"          :cargo/name "Meat"}
   {:cargo/ident "mercuric"      :cargo/name "Mercuric Chloride"
    :cargo/adr #{:cargo.adr/poison}}
   {:cargo/ident "milk"          :cargo/name "Milk"}
   {:cargo/ident "milk_t"        :cargo/name "Raw Milk?"}
   {:cargo/ident "mixtank"       :cargo/name "Mix Tank"}
   {:cargo/ident "mobile_crane"  :cargo/name "All Terrain Crane"
    :cargo/heavy? true}
   {:cargo/ident "moor_buoy"     :cargo/name "Mooring Buoy"}
   {:cargo/ident "mortar"        :cargo/name "Mortar"}
   {:cargo/ident "mtl_coil"      :cargo/name "Metal Coil?"}
   {:cargo/ident "mulcher"       :cargo/name "Mulcher"}
   {:cargo/ident "mystery_box"   :cargo/name "Mystery Box??? FIXME"}
   {:cargo/ident "nitrocel"      :cargo/name "Nitrocellulose"
    :cargo/adr #{:cargo.adr/flammable-solids}} ; TODO Check ADR?
   {:cargo/ident "nuts"          :cargo/name "Nuts"}
   {:cargo/ident "office_suppl"  :cargo/name "Office Supplies"}
   {:cargo/ident "oil_pipes"     :cargo/name "Oil Pipes??"}
   {:cargo/ident "paper"         :cargo/name "Paper?"}
   {:cargo/ident "pellet_afood"  :cargo/name "Pelleted Animal Food"}
   {:cargo/ident "pellet_afd_b"  :cargo/name "Pelleted Animal Food??"}
   {:cargo/ident "pesticide"     :cargo/name "Pesticides"
    :cargo/adr #{:cargo.adr/poison}}
   {:cargo/ident "petrol"        :cargo/name "Petrol/Gasoline"
    :cargo/adr #{:cargo.adr/flammable-liquids}}
   {:cargo/ident "pipes"         :cargo/name "Iron Pipes"}
   {:cargo/ident "plows"         :cargo/name "Plows"}
   {:cargo/ident "potassium"     :cargo/name "Potassium"
    :cargo/adr #{:cargo.adr/flammable-solids}}
   {:cargo/ident "potatoes"      :cargo/name "Potatoes?"}
   {:cargo/ident "potatoes_b"    :cargo/name "Potatoes??"}
   {:cargo/ident "propane"       :cargo/name "Propane"
    :cargo/adr #{:cargo.adr/flammable-liquids}}
   {:cargo/ident "pt_579"        :cargo/name "pt_579?? Patrol Boat?? FIXME"}
   {:cargo/ident "pumpjack"      :cargo/name "Pumpjack"}
   {:cargo/ident "railcar"       :cargo/name "Railcar??"}
   {:cargo/ident "rails"         :cargo/name "Rails?"}
   {:cargo/ident "rawmilk"       :cargo/name "Raw Milk"}
   {:cargo/ident "rice"          :cargo/name "Rice"}
   {:cargo/ident "roadroller"    :cargo/name "Roadroller"}
   {:cargo/ident "roof_tiles"    :cargo/name "Roof Tiles"}
   {:cargo/ident "salt"          :cargo/name "Salt"}
   {:cargo/ident "sand"          :cargo/name "Sand"}
   {:cargo/ident "sawpanels"     :cargo/name "Sawdust Panels"}
   {:cargo/ident "school_bus"    :cargo/name "School Bus"}
   {:cargo/ident "scrap_cars"    :cargo/name "Scrapped Cars"}
   {:cargo/ident "scrap_metals"  :cargo/name "Scrap Metals"}
   {:cargo/ident "scraper"       :cargo/name "Scraper"
    :cargo/heavy? true}
   {:cargo/ident "silane"        :cargo/name "Silane"
    :cargo/adr #{:cargo.adr/gases}}
   {:cargo/ident "silica"        :cargo/name "Silica"}
   {:cargo/ident "silo"          :cargo/name "Giant Silo"}
   {:cargo/ident "soda_ash"      :cargo/name "Soda Ash?"}
   {:cargo/ident "soil"          :cargo/name "Excavated Soil"}
   {:cargo/ident "solvents"      :cargo/name "Solvents"
    :cargo/adr #{:cargo.adr/flammable-liquids}}
   {:cargo/ident "soybean_b"     :cargo/name "Soybean"}
   {:cargo/ident "space_cont"    :cargo/name "Space Container?? FIXME"}
   {:cargo/ident "sq_tub"        :cargo/name "Square Tubing"}
   {:cargo/ident "stones"        :cargo/name "Stones"}
   {:cargo/ident "stumper"       :cargo/name "Stumper"}
   {:cargo/ident "sugar"         :cargo/name "Sugar"}
   {:cargo/ident "sugar_beet_b"  :cargo/name "Sugar Beets??"}
   {:cargo/ident "sulfuric"      :cargo/name "Sulphuric Acid"
    :cargo/adr #{:cargo.adr/corrosive}}
   {:cargo/ident "tableware"     :cargo/name "Tableware"}
   {:cargo/ident "talc_pwdr"     :cargo/name "Talc Powder"}
   {:cargo/ident "tamp_machine"  :cargo/name "Tamping Machine"}
   {:cargo/ident "tank"          :cargo/name "Large Reservoir Tank??"}
   {:cargo/ident "telehandler"   :cargo/name "Telescopic Handler"}
   {:cargo/ident "toys"          :cargo/name "Toys"}
   {:cargo/ident "tractor_c"     :cargo/name "Crawler Tractor"
    :cargo/heavy? true}
   {:cargo/ident "tractors"      :cargo/name "Tractors"}
   {:cargo/ident "train_part"    :cargo/name "Train Axles?"}
   {:cargo/ident "transformer"   :cargo/name "Transformer"
    :cargo/heavy? true}
   {:cargo/ident "transform2"    :cargo/name "Transformer 2??"}
   {:cargo/ident "trees"         :cargo/name "Trees"}
   {:cargo/ident "tub_grinder"   :cargo/name "Tub Grinder"}
   {:cargo/ident "tvs"           :cargo/name "TVs"}
   {:cargo/ident "tyres"         :cargo/name "Tires??"}
   {:cargo/ident "used_pack"     :cargo/name "Used Packaging"}
   {:cargo/ident "util_pole"     :cargo/name "Utility Poles??"}
   {:cargo/ident "vegetable"     :cargo/name "Vegetables"}
   {:cargo/ident "ventilation"   :cargo/name "Ventilation Shaft"}
   {:cargo/ident "waste_paper"   :cargo/name "Waste Paper"}
   {:cargo/ident "windblade"     :cargo/name "Wind Turbine Blade??"}
   {:cargo/ident "windml_eng"    :cargo/name "Wind Turbine Nacelle?"}
   {:cargo/ident "windml_tube"   :cargo/name "Wind Turbine Tower?"}
   {:cargo/ident "ws_49x"        :cargo/name "Some kind of truck?"}
   {:cargo/ident "wshavings"     :cargo/name "Wood Shavings"}
   {:cargo/ident "yard_truck"    :cargo/name "Yard Truck"}
   {:cargo/ident "yogurt"        :cargo/name "Yoghurt"}])

(def initial-data
  "Initial data for the ATS map, companies, etc."
  (future (into [] cat  [cargoes tx-states @tx-game-files])))

(comment
 (->> @initial-data
      (filter :db/ident)
      )
 )

(def ^:private city-renames
  {"san_rafael" "oakland"
   "oakdale"    "modesto"
   "hornbrook"  "hilt"})

(defn canonical-city-name [city-ident]
  (get city-renames city-ident city-ident))
