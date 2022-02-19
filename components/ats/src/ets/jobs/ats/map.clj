(ns ets.jobs.ats.map)

(def cities
  {
   ; California (CA, 22 cities)
  "bakersfield"  {:c "CA" :name "Bakersfield"}
  "barstow"      {:c "CA" :name "Barstow"}
  "carlsbad"     {:c "CA" :name "Carlsbad"}
  "el_centro"    {:c "CA" :name "El Centro"}
  "eureka"       {:c "CA" :name "Eureka"}
  "fresno"       {:c "CA" :name "Fresno"}
  "hilt"         {:c "CA" :name "Hilt"}
  "huron"        {:c "CA" :name "Huron"}
  "los_angeles"  {:c "CA" :name "Los Angeles"}
  "oakdale"      {:c "CA" :name "Oakdale"}
  "oakland"      {:c "CA" :name "Oakland"}
  "oxnard"       {:c "CA" :name "Oxnard"}
  "redding"      {:c "CA" :name "Redding"}
  "sacramento"   {:c "CA" :name "Sacramento"}
  "san_diego"    {:c "CA" :name "San Diego"}
  "san_francisc" {:c "CA" :name "San Francisc"}
  "san_rafael"   {:c "CA" :name "San Rafael"}
  "santa_cruz"   {:c "CA" :name "Santa Cruz"}
  "santa_maria"  {:c "CA" :name "Santa Maria"}
  "stockton"     {:c "CA" :name "Stockton"}
  "truckee"      {:c "CA" :name "Truckee"}
  "ukiah"        {:c "CA" :name "Ukiah"}

  ; Arizona (AZ, 16 cities)
  "camp_verde"    {:c "AZ" :name "Camp Verde"}
  "clifton"       {:c "AZ" :name "Clifton"}
  "ehrenberg"     {:c "AZ" :name "Ehrenberg"}
  "flagstaff"     {:c "AZ" :name "Flagstaff"}
  "g_canyon_vlg"  {:c "AZ" :name "Grand Canyon Village"}
  "holbrook"      {:c "AZ" :name "Holbrook"}
  "kayenta"       {:c "AZ" :name "Kayenta"}
  "kingman"       {:c "AZ" :name "Kingman"}
  "nogales"       {:c "AZ" :name "Nogales"}
  "page"          {:c "AZ" :name "Page"}
  "phoenix"       {:c "AZ" :name "Phoenix"}
  "san_simon"     {:c "AZ" :name "San Simon"}
  "show_low"      {:c "AZ" :name "Show Low"}
  "sierra_vista"  {:c "AZ" :name "Sierra Vista"}
  "tucson"        {:c "AZ" :name "Tucson"}
  "yuma"          {:c "AZ" :name "Yuma"}


  ; Colorado (CO, 13 cities)
  "alamosa"       {:c "CO" :name "Alamosa"}
  "burlington"    {:c "CO" :name "Burlington"}
  "colorado_spr"  {:c "CO" :name "Colorado Springs"}
  "denver"        {:c "CO" :name "Denver"}
  "durango"       {:c "CO" :name "Durango"}
  "fort_collins"  {:c "CO" :name "Fort Collins"}
  "grand_juncti"  {:c "CO" :name "Grand Junction"}
  "lamar"         {:c "CO" :name "Lamar"}
  "montrose"      {:c "CO" :name "Montrose"}
  "pueblo"        {:c "CO" :name "Pueblo"}
  "rangely"       {:c "CO" :name "Rangely"}
  "steamboat_sp"  {:c "CO" :name "Steamboat Springs"}
  "sterling"      {:c "CO" :name "Sterling"}


  ; Idaho (ID, 11 cities)
  "boise"         {:c "ID" :name "Boise"}
  "coeur_dalene"  {:c "ID" :name "Coeur d'Alene"}
  "grangeville"   {:c "ID" :name "Grangeville"}
  "idaho_falls"   {:c "ID" :name "Idaho Falls"}
  "ketchum"       {:c "ID" :name "Ketchum"}
  "lewiston"      {:c "ID" :name "Lewiston"}
  "nampa"         {:c "ID" :name "Nampa"}
  "pocatello"     {:c "ID" :name "Pocatello"}
  "salmon"        {:c "ID" :name "Salmon"}
  "sandpoint"     {:c "ID" :name "Sandpoint"}
  "twin_falls"    {:c "ID" :name "Twin Falls"}

  ; Nevada (NV, 10 cities)
  "carson_city"   {:c "NV" :name "Carson City"}
  "elko"          {:c "NV" :name "Elko"}
  "ely"           {:c "NV" :name "Ely"}
  "jackpot"       {:c "NV" :name "Jackpot"}
  "las_vegas"     {:c "NV" :name "Las Vegas"}
  "pioche"        {:c "NV" :name "Pioche"}
  "primm"         {:c "NV" :name "Primm"}
  "reno"          {:c "NV" :name "Reno"}
  "tonopah"       {:c "NV" :name "Tonopah"}
  "winnemucca"    {:c "NV" :name "Winnemucca"}

  ; New Mexico (NM, 14 cities)
  "alamogordo"    {:c "NM" :name "Alamogordo"}
  "albuquerque"   {:c "NM" :name "Albuquerque"}
  "artesia"       {:c "NM" :name "Artesia"}
  "carlsbad_nm"   {:c "NM" :name "Carlsbad"}
  "clovis"        {:c "NM" :name "Clovis"}
  "farmington"    {:c "NM" :name "Farmington"}
  "gallup"        {:c "NM" :name "Gallup"}
  "hobbs"         {:c "NM" :name "Hobbs"}
  "las_cruces"    {:c "NM" :name "Las Cruces"}
  "raton"         {:c "NM" :name "Raton"}
  "roswell"       {:c "NM" :name "Roswell"}
  "santa_fe"      {:c "NM" :name "Santa Fe"}
  "socorro"       {:c "NM" :name "Socorro"}
  "tucumcari"     {:c "NM" :name "Tucumcari"}

  ; Oregen (OR, 14 cities)
  "astoria"       {:c "OR" :name "Astoria"}
  "bend"          {:c "OR" :name "Bend"}
  "burns"         {:c "OR" :name "Burns"}
  "coos_bay"      {:c "OR" :name "Coos Bay"}
  "eugene"        {:c "OR" :name "Eugene"}
  "klamath_f"     {:c "OR" :name "Klamath Falls"}
  "lakeview"      {:c "OR" :name "Lakeview"}
  "medford"       {:c "OR" :name "Medford"}
  "newport"       {:c "OR" :name "Newport"}
  "ontario"       {:c "OR" :name "Ontario"}
  "pendleton"     {:c "OR" :name "Pendleton"}
  "portland"      {:c "OR" :name "Portland"}
  "salem"         {:c "OR" :name "Salem"}
  "the_dalles"    {:c "OR" :name "The Dalles"}

  ; Utah (UT, 10 cities)
  "cedar_city"    {:c "UT" :name "Cedar City"}
  "logan"         {:c "UT" :name "Logan"}
  "moab"          {:c "UT" :name "Moab"}
  "ogden"         {:c "UT" :name "Ogden"}
  "price"         {:c "UT" :name "Price"}
  "provo"         {:c "UT" :name "Provo"}
  "salina"        {:c "UT" :name "Salina"}
  "salt_lake"     {:c "UT" :name "Salt Lake City"}
  "st_george"     {:c "UT" :name "St. George"}
  "vernal"        {:c "UT" :name "Vernal"}

  ; Washington (WA, 16 cities)
  "aberdeen_wa"   {:c "WA" :name "Aberdeen"}
  "bellingham"    {:c "WA" :name "Bellingham"}
  "colville"      {:c "WA" :name "Colville"}
  "everett"       {:c "WA" :name "Everett"}
  "grand_coulee"  {:c "WA" :name "Grand Coulee"}
  "kennewick"     {:c "WA" :name "Kennewick"}
  "longview"      {:c "WA" :name "Longview"}
  "olympia"       {:c "WA" :name "Olympia"}
  "omak"          {:c "WA" :name "Omak"}
  "port_angeles"  {:c "WA" :name "Port Angeles"}
  "seattle"       {:c "WA" :name "Seattle"}
  "spokane"       {:c "WA" :name "Spokane"}
  "tacoma"        {:c "WA" :name "Tacoma"}
  "vancouver"     {:c "WA" :name "Vancouver"}
  "wenatchee"     {:c "WA" :name "Wenatchee"}
  "yakima"        {:c "WA" :name "Yakima"}

  ; Wyoming (WY, 10 cities)
  "casper"        {:c "WY" :name "Casper"}
  "cheyenne"      {:c "WY" :name "Cheyenne"}
  "evanston"      {:c "WY" :name "Evanston"}
  "gillette"      {:c "WY" :name "Gillette"}
  "jackson"       {:c "WY" :name "Jackson"}
  "laramie"       {:c "WY" :name "Laramie"}
  "rawlins"       {:c "WY" :name "Rawlins"}
  "riverton"      {:c "WY" :name "Riverton"}
  "rock_springs"  {:c "WY" :name "Rock Springs"}
  "sheridan"      {:c "WY" :name "Sheridan"}
  })

(def company-names
  {
   "42p_pck_pln"   "42 Print"
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
   "ws_trk_pln"    "???"
   })

(def cargos
  {"aircraft_eng"  {:name "Aircraft Engine"}
   "aromatics"     {:name "Aromatics" :adr #{:flammable-liquids}}
   "asph_miller"   {:name "Asphalt Miller"}
   "beverages"     {:name "Beverages"}
   "boat"          {:name "Boat"}
   "bottles"       {:name "Empty Bottles"}
   "bulldozer"     {:name "Bulldozer"}
   "butter"        {:name "Butter"}
   "cable"         {:name "Cable Reel"}
   "cans"          {:name "Cans??"}
   "carcomp"       {:name "carcomp???"}
   "cars_big"      {:name "Cars"}
   "cars_mix"      {:name "Cars"}
   "cars_pickup"   {:name "Cars"}
   "cars_small"    {:name "Cars"}
   "cattle"        {:name "Live Cattle"}
   "cement"        {:name "Cement"}
   "cheese"        {:name "Cheese"}
   "clothes"       {:name "Clothes"}
   "coil"          {:name "Metal Coil?"}
   "colors"        {:name "colors???"}
   "computers"     {:name "Computers"}
   "const_house"   {:name "Construction Houses"}
   "crude_oil"     {:name "Crude Oil" :adr #{:flammable-liquids}}
   "ctubes"        {:name "Concrete Tubes (small?)"}
   "ctubes_b"      {:name "Concrete Tubes (large?)"}
   "curtains"      {:name "Curtains"}
   "diesel"        {:name "Diesel" :adr #{:flammable-liquids}}
   "digger1000"    {:name "Wheel Loader"}
   "digger500"     {:name "Backhoe Loader"}
   "dozer"         {:name "Bulldozer"}
   "dry_fruit"     {:name "Dry Fruits"}
   "dry_milk"      {:name "Dry Milk"}
   "dynamite"      {:name "Dynamite" :adr #{:explosive}}
   "electro_comp"  {:name "Electronic Components"}
   "empty_palet"   {:name "Empty Pallets"}
   "emptytank"     {:name "Reservoir Tank?"}
   "ethane"        {:name "Ethane" :adr #{:flammable-liquids}}
   "excavator"     {:name "Excavator"}
   "fertilizer"    {:name "Fertilizer"}
   "fireworks"     {:name "Fireworks" :adr #{:explosive}}
   "flour"         {:name "Flour"}
   "food"          {:name "Packaged Food?"}
   "forklifts"     {:name "Forklifts"}
   "forwarder"     {:name "Forwarder"}
   "frac_tank"     {:name "Frac Tank"}
   "frozen_food"   {:name "Frozen Food"}
   "frozen_fruit"  {:name "Frozen Fruits"}
   "frozen_veget"  {:name "Frozen Vegetables"}
   "fruit_juic_t"  {:name "Fruit Juice?"}
   "fruits"        {:name "Fruits"}
   "furniture"     {:name "Furniture"}
   "generator_c"   {:name "Power Generator"}
   "grain"         {:name "Grain"}
   "gravel"        {:name "Gravel"}
   "gypsum"        {:name "Gypsum"}
   "harvester"     {:name "Harvester"}
   "hay"           {:name "Hay"}
   "hipresstank"   {:name "Pressure Tank"}
   "home_acc"      {:name "Home Accessories"}
   "house_pref"    {:name "House Prefabs"}
   "househd_appl"  {:name "Household Appliances"}
   "hydrochlor"    {:name "Hydrochloric Acid" :adr #{:corrosive}}
   "icecream"      {:name "Ice Cream"}
   "kb_loader"     {:name "Knuckleboom Loader"}
   "kw_t680"       {:name "Kenworth Trucks"}
   "largetubes"    {:name "Large Tubes"}
   "lift_truck"    {:name "Lift Truck?"}
   "lift_truck_s"  {:name "Lift Truck Chassis?"}
   "limestone"     {:name "Limestone"}
   "log_harvest"   {:name "Log Harvester"}
   "log_loader"    {:name "Log Loader"}
   "log_stacker"   {:name "Log Stacker"}
   "logs"          {:name "Logs"}
   "lumber"        {:name "Lumber (beams?)"}
   "lumber_b"      {:name "Lumber (boards?)"}
   "lye"           {:name "Lye" :adr #{:poison}}
   "machine_pts"   {:name "Machine Parts"}
   "mbt"           {:name "Mobile Barrier?"}
   "meat"          {:name "Meat"}
   "milk"          {:name "Milk"}
   "milk_t"        {:name "Raw Milk?"}
   "mixtank"       {:name "mixtank???"}
   "mobile_crane"  {:name "All Terrain Crane"}
   "moor_buoy"     {:name "Mooring Buoy"}
   "mortar"        {:name "Mortar"}
   "mtl_coil"      {:name "Metal Coil?"}
   "mulcher"       {:name "Mulcher"}
   "nitrocel"      {:name "Nitrocellulose" :adr #{:flammable-solids}} ; TODO Check ADR?
   "nuts"          {:name "Nuts"}
   "office_suppl"  {:name "Office Supplies"}
   "paper"         {:name "Paper?"}
   "pellet_afood"  {:name "Pelleted Animal Food"}
   "pesticide"     {:name "Pesticides" :adr #{:poison}}
   "petrol"        {:name "Petrol/Gasoline" :adr #{:flammable-liquids}}
   "pipes"         {:name "Iron Pipes"}
   "plows"         {:name "Plows"}
   "potassium"     {:name "Potassium" :adr #{:flammable-solids}}
   "potatoes"      {:name "Potatoes?"}
   "propane"       {:name "Propane" :adr #{:flammable-liquids}}
   "rails"         {:name "Rails?"}
   "rawmilk"       {:name "Raw Milk"}
   "rice"          {:name "Rice"}
   "roadroller"    {:name "Roadroller"}
   "salt"          {:name "Salt"}
   "sand"          {:name "Sand"}
   "sawpanels"     {:name "Sawdust Panels"}
   "scrap_cars"    {:name "Scrapped Cars"}
   "scrap_metals"  {:name "Scrap Metals"}
   "scraper"       {:name "Scraper"}
   "silica"        {:name "Silica"}
   "soda_ash"      {:name "Soda Ash?"}
   "soil"          {:name "Excavated Soil"}
   "solvents"      {:name "Solvents" :adr #{:flammable-liquids}}
   "sq_tub"        {:name "Square Tubing"}
   "stones"        {:name "Stones"}
   "stumper"       {:name "Stumper"}
   "sugar"         {:name "Sugar"}
   "tableware"     {:name "Tableware"}
   "tamp_machine"  {:name "Tamping Machine"}
   "toys"          {:name "Toys"}
   "tractor_c"     {:name "Crawler Tractor"}
   "tractors"      {:name "Tractors"}
   "train_part"    {:name "Train Axles?"}
   "transformer"   {:name "Transformer"}
   "tub_grinder"   {:name "Tub Grinder"}
   "tvs"           {:name "TVs"}
   "tyres"         {:name "Tires??"}
   "used_pack"     {:name "Used Packaging"}
   "vegetable"     {:name "Vegetables"}
   "ventilation"   {:name "Ventilation Shaft"}
   "windml_eng"    {:name "Wind Turbine Nacelle?"}
   "windml_tube"   {:name "Wind Turbine Tower?"}
   "ws_49x"        {:name "Some kind of truck?"}
   "wshavings"     {:name "Wood Shavings"}
   "yogurt"        {:name "Yoghurt"}
   })


(defn human-name
  "Given the machine-readable slug (eg. 'laurent') returns the human-readable
  name with state abbreviation, like in the game (eg. 'Tacoma (WA)').
  If the slug is unknown (the list above is ad hoc and might have missed some)
  it returns 'slug (??)'.

  NOTE the field is called :country, not :state, for unity with ETS2.
  Also there's no flags in ATS since the state flags are mostly hideous and are
  not usable emoji/Unicode characters."
  [slug]
  (let [{:keys [c name]} (cities slug)]
    {:city    (or name slug)
     :country (or c "???")}))

