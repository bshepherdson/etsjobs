(ns ets.jobs.ats.map
  (:require
   [com.wsscode.pathom3.connect.built-in.resolvers :as pbir]
   [com.wsscode.pathom3.connect.indexes :as pci]
   [com.wsscode.pathom3.connect.operation :as pco]))

(def ^:private cities-ca
  "California (CA, 22 cities)"
  {"bakersfield"  "Bakersfield"
   "barstow"      "Barstow"
   "carlsbad"     "Carlsbad"
   "el_centro"    "El Centro"
   "eureka"       "Eureka"
   "fresno"       "Fresno"
   "hilt"         "Hilt"
   "huron"        "Huron"
   "los_angeles"  "Los Angeles"
   "oakdale"      "Oakdale"
   "oakland"      "Oakland"
   "oxnard"       "Oxnard"
   "redding"      "Redding"
   "sacramento"   "Sacramento"
   "san_diego"    "San Diego"
   "san_francisc" "San Francisc"
   "san_rafael"   "San Rafael"
   "santa_cruz"   "Santa Cruz"
   "santa_maria"  "Santa Maria"
   "stockton"     "Stockton"
   "truckee"      "Truckee"
   "ukiah"        "Ukiah"})

(def ^:private cities-az
  "Arizona (AZ, 16 cities)"
  {"camp_verde"   "Camp Verde"
   "clifton"      "Clifton"
   "ehrenberg"    "Ehrenberg"
   "flagstaff"    "Flagstaff"
   "g_canyon_vlg" "Grand Canyon Village"
   "holbrook"     "Holbrook"
   "kayenta"      "Kayenta"
   "kingman"      "Kingman"
   "nogales"      "Nogales"
   "page"         "Page"
   "phoenix"      "Phoenix"
   "san_simon"    "San Simon"
   "show_low"     "Show Low"
   "sierra_vista" "Sierra Vista"
   "tucson"       "Tucson"
   "yuma"         "Yuma"})

(def ^:private cities-co
  "Colorado (CO, 13 cities)"
  {"alamosa"       "Alamosa"
   "burlington"    "Burlington"
   "colorado_spr"  "Colorado Springs"
   "denver"        "Denver"
   "durango"       "Durango"
   "fort_collins"  "Fort Collins"
   "grand_juncti"  "Grand Junction"
   "lamar"         "Lamar"
   "montrose"      "Montrose"
   "pueblo"        "Pueblo"
   "rangely"       "Rangely"
   "steamboat_sp"  "Steamboat Springs"
   "sterling"      "Sterling"})

(def ^:private cities-id
  "Idaho (ID, 11 cities)"
  {"boise"         "Boise"
   "coeur_dalene"  "Coeur d'Alene"
   "grangeville"   "Grangeville"
   "idaho_falls"   "Idaho Falls"
   "ketchum"       "Ketchum"
   "lewiston"      "Lewiston"
   "nampa"         "Nampa"
   "pocatello"     "Pocatello"
   "salmon"        "Salmon"
   "sandpoint"     "Sandpoint"
   "twin_falls"    "Twin Falls"})

(def ^:private cities-nv
  "Nevada (NV, 10 cities)"
  {"carson_city"   "Carson City"
   "elko"          "Elko"
   "ely"           "Ely"
   "jackpot"       "Jackpot"
   "las_vegas"     "Las Vegas"
   "pioche"        "Pioche"
   "primm"         "Primm"
   "reno"          "Reno"
   "tonopah"       "Tonopah"
   "winnemucca"    "Winnemucca"})

(def ^:private cities-nm
  "New Mexico (NM, 14 cities)"
  {"alamogordo"    "Alamogordo"
   "albuquerque"   "Albuquerque"
   "artesia"       "Artesia"
   "carlsbad_nm"   "Carlsbad"
   "clovis"        "Clovis"
   "farmington"    "Farmington"
   "gallup"        "Gallup"
   "hobbs"         "Hobbs"
   "las_cruces"    "Las Cruces"
   "raton"         "Raton"
   "roswell"       "Roswell"
   "santa_fe"      "Santa Fe"
   "socorro"       "Socorro"
   "tucumcari"     "Tucumcari"})

(def ^:private cities-or
  "Oregon (OR, 14 cities)"
  {"astoria"       "Astoria"
   "bend"          "Bend"
   "burns"         "Burns"
   "coos_bay"      "Coos Bay"
   "eugene"        "Eugene"
   "klamath_f"     "Klamath Falls"
   "lakeview"      "Lakeview"
   "medford"       "Medford"
   "newport"       "Newport"
   "ontario"       "Ontario"
   "pendleton"     "Pendleton"
   "portland"      "Portland"
   "salem"         "Salem"
   "the_dalles"    "The Dalles"})

(def ^:private cities-ut
  "Utah (UT, 10 cities)"
  {"cedar_city"    "Cedar City"
   "logan"         "Logan"
   "moab"          "Moab"
   "ogden"         "Ogden"
   "price"         "Price"
   "provo"         "Provo"
   "salina"        "Salina"
   "salt_lake"     "Salt Lake City"
   "st_george"     "St. George"
   "vernal"        "Vernal"})

(def ^:private cities-wa
  "Washington (WA, 16 cities)"
  {"aberdeen_wa"   "Aberdeen"
   "bellingham"    "Bellingham"
   "colville"      "Colville"
   "everett"       "Everett"
   "grand_coulee"  "Grand Coulee"
   "kennewick"     "Kennewick"
   "longview"      "Longview"
   "olympia"       "Olympia"
   "omak"          "Omak"
   "port_angeles"  "Port Angeles"
   "seattle"       "Seattle"
   "spokane"       "Spokane"
   "tacoma"        "Tacoma"
   "vancouver"     "Vancouver"
   "wenatchee"     "Wenatchee"
   "yakima"        "Yakima"})

(def ^:private cities-wy
  "Wyoming (WY, 10 cities)"
  {"casper"        "Casper"
   "cheyenne"      "Cheyenne"
   "evanston"      "Evanston"
   "gillette"      "Gillette"
   "jackson"       "Jackson"
   "laramie"       "Laramie"
   "rawlins"       "Rawlins"
   "riverton"      "Riverton"
   "rock_springs"  "Rock Springs"
   "sheridan"      "Sheridan"})

(def ^:private states-data
  [["CA" "California" cities-ca]
   ["AZ" "Arizona"    cities-az]
   ["CO" "Colorado"   cities-co]
   ["ID" "Idaho"      cities-id]
   ["NV" "Nevada"     cities-nv]
   ["NM" "New Mexico" cities-nm]
   ["OR" "Oregon"     cities-or]
   ["UT" "Utah"       cities-ut]
   ["WA" "Washington" cities-wa]
   ["WY" "Wyoming"    cities-wy]])

(def ^:private states
  (->> (for [[state label _cities] states-data]
         [state {:country/name label}])
       (into {})
       (pbir/static-table-resolver :country/id)))

(def ^:private cities
  (->> (for [[state _label cities] states-data
             [city-id city-name]   cities]
         [city-id {:country/id  state
                   :city/name city-name}])
       (into {})
       (pbir/static-table-resolver :city/id)))

(def ^:private company-names
  (pbir/static-attribute-map-resolver
    :company/id :company/name
    {"42p_pck_pln"   "42 Print"
     "42p_print"     "42 Print"
     "aml_rail_str"  "American Lines"
     "aport_abq"     "ABQ Cargo Center"
     "aport_den"     "Denver Air Cargo"
     "aport_pcc"     "Portland Cargo Central"
     "aport_phx"     "Phoenix Freight"
     "aport_ult"     "Ultimus"
     "avs_met_scr"   "Avalanche Steel"
     "avs_met_sml"   "Avalanche Steel"
     "bit_rd_grg"    "Bitumen"
     "bit_rd_svc"    "Bitumen"
     "bit_rd_wrk"    "Bitumen"
     "bn_farm"       "Bushnell Farms"
     "bn_live_auc"   "Bushnell Farms"
     "cha_el_mkt"    "Charged"
     "cha_el_whs"    "Charged"
     "chm_che_plnt"  "Chemso"
     "chm_che_str"   "Chemso"
     "cm_min_plnt"   "Coastline Mining"
     "cm_min_qry"    "Coastline Mining"
     "cm_min_qryp"   "Coastline Mining"
     "cm_min_str"    "Coastline Mining"
     "cm_min_svc"    "Coastline Mining"
     "dc_car_dlr"    "Drake Car Dealer"
     "dg_wd_hrv"     "Deepgrove (saw)"
     "dg_wd_saw"     "Deepgrove (saw)"
     "dg_wd_saw1"    "Deepgrove (saw)"
     "dmc_car_dlr"   "DeMuro Cars"
     "du_farm"       "???"
     "dw_air_pln"    "Darwing"
     "ed_mkt"        "Eddy's"
     "fb_farm_mkt"   "Farmer's Barn"
     "fb_farm_pln"   "Farmer's Barn"
     "ftf_food_pln"  "???"
     "gal_oil_gst"   "Gallon Oil"
     "gal_oil_ref"   "Gallon Oil"
     "gal_oil_sit"   "Gallon Oil"
     "gal_oil_str"   "Gallon Oil"
     "gal_oil_str1"  "Gallon Oil"
     "gm_chs_plnt"   "Global Mills"
     "gm_food_plnt"  "Global Mills"
     "gol_trk_pln"   "Goliath"
     "hds_met_shp"   "Haddock Shipyard"
     "hf_wd_pln"     "Heartwood Furniture"
     "hms_con_svc"   "HMS Machinery"
     "hs_mkt"        "Home Store"
     "hs_whs"        "Home Store"
     "jns_rail_wrk"  "Johnson & Smith"
     "kw_trk_dlr"    "Kenworth Trucks???"
     "kw_trk_pln"    "Kenworth Trucks???"
     "nmq_min_plnt"  "NAMIQ"
     "nmq_min_qry"   "NAMIQ"
     "nmq_min_qrys"  "NAMIQ"
     "nmq_min_str"   "NAMIQ"
     "nmq_min_svc"   "NAMIQ"
     "oak_port"      "Oakland Shippers"
     "oh_con_hom"    "Olthon Homes"
     "pnp_wd_pln"    "Page & Price Paper"
     "pns_con_grg"   "Plaster & Sons"
     "pns_con_sit"   "Plaster & Sons"
     "pns_con_sit1"  "Plaster & Sons"
     "pns_con_sit2"  "Plaster & Sons"
     "pns_con_sit3"  "Plaster & Sons"
     "pns_con_whs"   "Plaster & Sons"
     "port_sea"      "Port of Seattle"
     "port_tac"      "Port of Tacoma"
     "re_train"      "Rail Export"
     "sc_frm"        "Sunshine Crops"
     "sc_frm_grg"    "Sunshine Crops"
     "sf_port"       "Port of San Francisco"
     "sg_whs"        "Sell Goods"
     "sh_shp_mar"    "Sea Horizon"
     "sh_shp_plnt"   "Sea Horizon"
     "st_met_whs"    "Steeler"
     "st_met_wrk"    "Steeler"
     "tid_mkt"       "Tidbit"
     "usb_food_pln"  "USBB"
     "vm_car_dlr"    "Voltison Motors"
     "vm_car_pln"    "Voltison Motors"
     "vm_car_whs"    "Voltison Motors"
     "vp_epw_pln"    "Vitas Power"
     "vp_epw_sit"    "Vitas Power"
     "wal_food_mkt"  "Wallbert"
     "wal_food_whs"  "Wallbert"
     "wal_mkt"       "Wallbert"
     "wal_whs"       "Wallbert"
     "ws_trk_dlr"    "???"
     "ws_trk_pln"    "???"}))

(def ^:private cargos
  (pbir/static-table-resolver
    :cargo/id
    {"aircraft_eng"  {:cargo/name "Aircraft Engine"}
     "aromatics"     {:cargo/name "Aromatics" :cargo/adr #{:adr/flammable-liquids}}
     "asph_miller"   {:cargo/name "Asphalt Miller"}
     "beverages"     {:cargo/name "Beverages"}
     "boat"          {:cargo/name "Boat"}
     "bottles"       {:cargo/name "Empty Bottles"}
     "bulldozer"     {:cargo/name "Bulldozer"}
     "butter"        {:cargo/name "Butter"}
     "cable"         {:cargo/name "Cable Reel"}
     "cans"          {:cargo/name "Cans??"}
     "carcomp"       {:cargo/name "carcomp???"}
     "cars_big"      {:cargo/name "Cars"}
     "cars_mix"      {:cargo/name "Cars"}
     "cars_pickup"   {:cargo/name "Cars"}
     "cars_small"    {:cargo/name "Cars"}
     "cattle"        {:cargo/name "Live Cattle"}
     "cement"        {:cargo/name "Cement"}
     "cheese"        {:cargo/name "Cheese"}
     "clothes"       {:cargo/name "Clothes"}
     "coil"          {:cargo/name "Metal Coil?"}
     "colors"        {:cargo/name "colors???"}
     "computers"     {:cargo/name "Computers"}
     "const_house"   {:cargo/name "Construction Houses"}
     "crude_oil"     {:cargo/name "Crude Oil" :cargo/adr #{:adr/flammable-liquids}}
     "ctubes"        {:cargo/name "Concrete Tubes (small?)"}
     "ctubes_b"      {:cargo/name "Concrete Tubes (large?)"}
     "curtains"      {:cargo/name "Curtains"}
     "diesel"        {:cargo/name "Diesel" :cargo/adr #{:adr/flammable-liquids}}
     "digger1000"    {:cargo/name "Wheel Loader"}
     "digger500"     {:cargo/name "Backhoe Loader"}
     "dozer"         {:cargo/name "Bulldozer"}
     "dry_fruit"     {:cargo/name "Dry Fruits"}
     "dry_milk"      {:cargo/name "Dry Milk"}
     "dynamite"      {:cargo/name "Dynamite" :cargo/adr #{:adr/explosive}}
     "electro_comp"  {:cargo/name "Electronic Components"}
     "empty_palet"   {:cargo/name "Empty Pallets"}
     "emptytank"     {:cargo/name "Reservoir Tank?"}
     "ethane"        {:cargo/name "Ethane" :cargo/adr #{:adr/flammable-liquids}}
     "excavator"     {:cargo/name "Excavator"}
     "fertilizer"    {:cargo/name "Fertilizer"}
     "fireworks"     {:cargo/name "Fireworks" :cargo/adr #{:adr/explosive}}
     "flour"         {:cargo/name "Flour"}
     "food"          {:cargo/name "Packaged Food?"}
     "forklifts"     {:cargo/name "Forklifts"}
     "forwarder"     {:cargo/name "Forwarder"}
     "frac_tank"     {:cargo/name "Frac Tank"}
     "frozen_food"   {:cargo/name "Frozen Food"}
     "frozen_fruit"  {:cargo/name "Frozen Fruits"}
     "frozen_veget"  {:cargo/name "Frozen Vegetables"}
     "fruit_juic_t"  {:cargo/name "Fruit Juice?"}
     "fruits"        {:cargo/name "Fruits"}
     "furniture"     {:cargo/name "Furniture"}
     "generator_c"   {:cargo/name "Power Generator"}
     "grain"         {:cargo/name "Grain"}
     "gravel"        {:cargo/name "Gravel"}
     "gypsum"        {:cargo/name "Gypsum"}
     "harvester"     {:cargo/name "Harvester"}
     "hay"           {:cargo/name "Hay"}
     "hipresstank"   {:cargo/name "Pressure Tank"}
     "home_acc"      {:cargo/name "Home Accessories"}
     "house_pref"    {:cargo/name "House Prefabs"}
     "househd_appl"  {:cargo/name "Household Appliances"}
     "hydrochlor"    {:cargo/name "Hydrochloric Acid" :cargo/adr #{:adr/corrosive}}
     "icecream"      {:cargo/name "Ice Cream"}
     "kb_loader"     {:cargo/name "Knuckleboom Loader"}
     "kw_t680"       {:cargo/name "Kenworth Trucks"}
     "largetubes"    {:cargo/name "Large Tubes"}
     "lift_truck"    {:cargo/name "Lift Truck?"}
     "lift_truck_s"  {:cargo/name "Lift Truck Chassis?"}
     "limestone"     {:cargo/name "Limestone"}
     "log_harvest"   {:cargo/name "Log Harvester"}
     "log_loader"    {:cargo/name "Log Loader"}
     "log_stacker"   {:cargo/name "Log Stacker"}
     "logs"          {:cargo/name "Logs"}
     "lumber"        {:cargo/name "Lumber (beams?)"}
     "lumber_b"      {:cargo/name "Lumber (boards?)"}
     "lye"           {:cargo/name "Lye" :cargo/adr #{:adr/poison}}
     "machine_pts"   {:cargo/name "Machine Parts"}
     "mbt"           {:cargo/name "Mobile Barrier?"}
     "meat"          {:cargo/name "Meat"}
     "milk"          {:cargo/name "Milk"}
     "milk_t"        {:cargo/name "Raw Milk?"}
     "mixtank"       {:cargo/name "mixtank???"}
     "mobile_crane"  {:cargo/name "All Terrain Crane"}
     "moor_buoy"     {:cargo/name "Mooring Buoy"}
     "mortar"        {:cargo/name "Mortar"}
     "mtl_coil"      {:cargo/name "Metal Coil?"}
     "mulcher"       {:cargo/name "Mulcher"}
     "nitrocel"      {:cargo/name "Nitrocellulose" :cargo/adr #{:adr/flammable-solids}} ; TODO Check ADR?
     "nuts"          {:cargo/name "Nuts"}
     "office_suppl"  {:cargo/name "Office Supplies"}
     "paper"         {:cargo/name "Paper?"}
     "pellet_afood"  {:cargo/name "Pelleted Animal Food"}
     "pesticide"     {:cargo/name "Pesticides" :cargo/adr #{:adr/poison}}
     "petrol"        {:cargo/name "Petrol/Gasoline" :cargo/adr #{:adr/flammable-liquids}}
     "pipes"         {:cargo/name "Iron Pipes"}
     "plows"         {:cargo/name "Plows"}
     "potassium"     {:cargo/name "Potassium" :cargo/adr #{:adr/flammable-solids}}
     "potatoes"      {:cargo/name "Potatoes?"}
     "propane"       {:cargo/name "Propane" :cargo/adr #{:adr/flammable-liquids}}
     "rails"         {:cargo/name "Rails?"}
     "rawmilk"       {:cargo/name "Raw Milk"}
     "rice"          {:cargo/name "Rice"}
     "roadroller"    {:cargo/name "Roadroller"}
     "salt"          {:cargo/name "Salt"}
     "sand"          {:cargo/name "Sand"}
     "sawpanels"     {:cargo/name "Sawdust Panels"}
     "scrap_cars"    {:cargo/name "Scrapped Cars"}
     "scrap_metals"  {:cargo/name "Scrap Metals"}
     "scraper"       {:cargo/name "Scraper"}
     "silica"        {:cargo/name "Silica"}
     "soda_ash"      {:cargo/name "Soda Ash?"}
     "soil"          {:cargo/name "Excavated Soil"}
     "solvents"      {:cargo/name "Solvents" :cargo/adr #{:adr/flammable-liquids}}
     "sq_tub"        {:cargo/name "Square Tubing"}
     "stones"        {:cargo/name "Stones"}
     "stumper"       {:cargo/name "Stumper"}
     "sugar"         {:cargo/name "Sugar"}
     "tableware"     {:cargo/name "Tableware"}
     "tamp_machine"  {:cargo/name "Tamping Machine"}
     "toys"          {:cargo/name "Toys"}
     "tractor_c"     {:cargo/name "Crawler Tractor"}
     "tractors"      {:cargo/name "Tractors"}
     "train_part"    {:cargo/name "Train Axles?"}
     "transformer"   {:cargo/name "Transformer"}
     "tub_grinder"   {:cargo/name "Tub Grinder"}
     "tvs"           {:cargo/name "TVs"}
     "tyres"         {:cargo/name "Tires??"}
     "used_pack"     {:cargo/name "Used Packaging"}
     "vegetable"     {:cargo/name "Vegetables"}
     "ventilation"   {:cargo/name "Ventilation Shaft"}
     "windml_eng"    {:cargo/name "Wind Turbine Nacelle?"}
     "windml_tube"   {:cargo/name "Wind Turbine Tower?"}
     "ws_49x"        {:cargo/name "Some kind of truck?"}
     "wshavings"     {:cargo/name "Wood Shavings"}
     "yogurt"        {:cargo/name "Yoghurt"}}))

(pco/defresolver city-human-name
  "Given the machine-readable slug (eg. 'laurent') returns the human-readable
  name with state abbreviation, like in the game (eg. 'Tacoma (WA)').
  If the slug is unknown (the list above is ad hoc and might have missed some)
  it returns 'slug (??)'.

  NOTE the field is called :country, not :state, for unity with ETS2.
  Also there's no flags in ATS since the state flags are mostly hideous and are
  not usable emoji/Unicode characters."
  [{city-name :city/name
    state     :country/id
    slug      :city/id}]
  {::pco/input [:city/id (pco/? :city/name) (pco/? :country/id)]}
  {:city/human-name   (or city-name slug)
   :city/country-code (or state "???")})

(def index
  (pci/register [cities cargos company-names city-human-name]))
