(ns ets.jobs.ats.map)

(def ^:private cities-ca
  "California (CA, 22 cities)"
  [{:city/ident "bakersfield",  :city/name "Bakersfield",          :city/state :state/ca}
   {:city/ident "barstow",      :city/name "Barstow",              :city/state :state/ca}
   {:city/ident "carlsbad",     :city/name "Carlsbad",             :city/state :state/ca}
   {:city/ident "el_centro",    :city/name "El Centro",            :city/state :state/ca}
   {:city/ident "eureka",       :city/name "Eureka",               :city/state :state/ca}
   {:city/ident "fresno",       :city/name "Fresno",               :city/state :state/ca}
   {:city/ident "hilt",         :city/name "Hilt",                 :city/state :state/ca}
   {:city/ident "huron",        :city/name "Huron",                :city/state :state/ca}
   {:city/ident "los_angeles",  :city/name "Los Angeles",          :city/state :state/ca}
   {:city/ident "modesto",      :city/name "Modesto",              :city/state :state/ca}
   {:city/ident "oakland",      :city/name "Oakland",              :city/state :state/ca}
   {:city/ident "oxnard",       :city/name "Oxnard",               :city/state :state/ca}
   {:city/ident "redding",      :city/name "Redding",              :city/state :state/ca}
   {:city/ident "sacramento",   :city/name "Sacramento",           :city/state :state/ca}
   {:city/ident "san_diego",    :city/name "San Diego",            :city/state :state/ca}
   {:city/ident "san_francisc", :city/name "San Francisco",        :city/state :state/ca}
   {:city/ident "san_jose",     :city/name "San Jose",             :city/state :state/ca}
   {:city/ident "santa_cruz",   :city/name "Santa Cruz",           :city/state :state/ca}
   {:city/ident "santa_maria",  :city/name "Santa Maria",          :city/state :state/ca}
   {:city/ident "stockton",     :city/name "Stockton",             :city/state :state/ca}
   {:city/ident "truckee",      :city/name "Truckee",              :city/state :state/ca}
   {:city/ident "ukiah",        :city/name "Ukiah",                :city/state :state/ca}])

(def ^:private cities-az
  "Arizona (AZ, 16 cities)"
  [{:city/ident "camp_verde",   :city/name "Camp Verde",           :city/state :state/az}
   {:city/ident "clifton",      :city/name "Clifton",              :city/state :state/az}
   {:city/ident "ehrenberg",    :city/name "Ehrenberg",            :city/state :state/az}
   {:city/ident "flagstaff",    :city/name "Flagstaff",            :city/state :state/az}
   {:city/ident "g_canyon_vlg", :city/name "Grand Canyon Village", :city/state :state/az}
   {:city/ident "holbrook",     :city/name "Holbrook",             :city/state :state/az}
   {:city/ident "kayenta",      :city/name "Kayenta",              :city/state :state/az}
   {:city/ident "kingman",      :city/name "Kingman",              :city/state :state/az}
   {:city/ident "nogales",      :city/name "Nogales",              :city/state :state/az}
   {:city/ident "page",         :city/name "Page",                 :city/state :state/az}
   {:city/ident "phoenix",      :city/name "Phoenix",              :city/state :state/az}
   {:city/ident "san_simon",    :city/name "San Simon",            :city/state :state/az}
   {:city/ident "show_low",     :city/name "Show Low",             :city/state :state/az}
   {:city/ident "sierra_vista", :city/name "Sierra Vista",         :city/state :state/az}
   {:city/ident "tucson",       :city/name "Tucson",               :city/state :state/az}
   {:city/ident "yuma",         :city/name "Yuma",                 :city/state :state/az}])

(def ^:private cities-co
  "Colorado (CO, 13 cities)"
  [{:city/ident "alamosa",      :city/name "Alamosa",              :city/state :state/co}
   {:city/ident "burlington",   :city/name "Burlington",           :city/state :state/co}
   {:city/ident "colorado_spr", :city/name "Colorado Springs",     :city/state :state/co}
   {:city/ident "denver",       :city/name "Denver",               :city/state :state/co}
   {:city/ident "durango",      :city/name "Durango",              :city/state :state/co}
   {:city/ident "fort_collins", :city/name "Fort Collins",         :city/state :state/co}
   {:city/ident "grand_juncti", :city/name "Grand Junction",       :city/state :state/co}
   {:city/ident "lamar",        :city/name "Lamar",                :city/state :state/co}
   {:city/ident "montrose",     :city/name "Montrose",             :city/state :state/co}
   {:city/ident "pueblo",       :city/name "Pueblo",               :city/state :state/co}
   {:city/ident "rangely",      :city/name "Rangely",              :city/state :state/co}
   {:city/ident "steamboat_sp", :city/name "Steamboat Springs",    :city/state :state/co}
   {:city/ident "sterling",     :city/name "Sterling",             :city/state :state/co}])

(def ^:private cities-id
  "Idaho (ID, 11 cities)"
  [{:city/ident "boise",        :city/name "Boise",                :city/state :state/id}
   {:city/ident "coeur_dalene", :city/name "Coeur d'Alene",        :city/state :state/id}
   {:city/ident "grangeville",  :city/name "Grangeville",          :city/state :state/id}
   {:city/ident "idaho_falls",  :city/name "Idaho Falls",          :city/state :state/id}
   {:city/ident "ketchum",      :city/name "Ketchum",              :city/state :state/id}
   {:city/ident "lewiston",     :city/name "Lewiston",             :city/state :state/id}
   {:city/ident "nampa",        :city/name "Nampa",                :city/state :state/id}
   {:city/ident "pocatello",    :city/name "Pocatello",            :city/state :state/id}
   {:city/ident "salmon",       :city/name "Salmon",               :city/state :state/id}
   {:city/ident "sandpoint",    :city/name "Sandpoint",            :city/state :state/id}
   {:city/ident "twin_falls",   :city/name "Twin Falls",           :city/state :state/id}])

(def ^:private cities-nv
  "Nevada (NV, 10 cities)"
  [{:city/ident "carson_city",  :city/name "Carson City",          :city/state :state/nv}
   {:city/ident "elko",         :city/name "Elko",                 :city/state :state/nv}
   {:city/ident "ely",          :city/name "Ely",                  :city/state :state/nv}
   {:city/ident "jackpot",      :city/name "Jackpot",              :city/state :state/nv}
   {:city/ident "las_vegas",    :city/name "Las Vegas",            :city/state :state/nv}
   {:city/ident "pioche",       :city/name "Pioche",               :city/state :state/nv}
   {:city/ident "primm",        :city/name "Primm",                :city/state :state/nv}
   {:city/ident "reno",         :city/name "Reno",                 :city/state :state/nv}
   {:city/ident "tonopah",      :city/name "Tonopah",              :city/state :state/nv}
   {:city/ident "winnemucca",   :city/name "Winnemucca",           :city/state :state/nv}])

(def ^:private cities-nm
  "New Mexico (NM, 14 cities)"
  [{:city/ident "alamogordo",   :city/name "Alamogordo",           :city/state :state/nm}
   {:city/ident "albuquerque",  :city/name "Albuquerque",          :city/state :state/nm}
   {:city/ident "artesia",      :city/name "Artesia",              :city/state :state/nm}
   {:city/ident "carlsbad_nm",  :city/name "Carlsbad",             :city/state :state/nm}
   {:city/ident "clovis",       :city/name "Clovis",               :city/state :state/nm}
   {:city/ident "farmington",   :city/name "Farmington",           :city/state :state/nm}
   {:city/ident "gallup",       :city/name "Gallup",               :city/state :state/nm}
   {:city/ident "hobbs",        :city/name "Hobbs",                :city/state :state/nm}
   {:city/ident "las_cruces",   :city/name "Las Cruces",           :city/state :state/nm}
   {:city/ident "raton",        :city/name "Raton",                :city/state :state/nm}
   {:city/ident "roswell",      :city/name "Roswell",              :city/state :state/nm}
   {:city/ident "santa_fe",     :city/name "Santa Fe",             :city/state :state/nm}
   {:city/ident "socorro",      :city/name "Socorro",              :city/state :state/nm}
   {:city/ident "tucumcari",    :city/name "Tucumcari",            :city/state :state/nm}])

(def ^:private cities-or
  "Oregon (OR, 14 cities)"
  [{:city/ident "astoria",      :city/name "Astoria",              :city/state :state/or}
   {:city/ident "bend",         :city/name "Bend",                 :city/state :state/or}
   {:city/ident "burns",        :city/name "Burns",                :city/state :state/or}
   {:city/ident "coos_bay",     :city/name "Coos Bay",             :city/state :state/or}
   {:city/ident "eugene",       :city/name "Eugene",               :city/state :state/or}
   {:city/ident "klamath_f",    :city/name "Klamath Falls",        :city/state :state/or}
   {:city/ident "lakeview",     :city/name "Lakeview",             :city/state :state/or}
   {:city/ident "medford",      :city/name "Medford",              :city/state :state/or}
   {:city/ident "newport",      :city/name "Newport",              :city/state :state/or}
   {:city/ident "ontario",      :city/name "Ontario",              :city/state :state/or}
   {:city/ident "pendleton",    :city/name "Pendleton",            :city/state :state/or}
   {:city/ident "portland",     :city/name "Portland",             :city/state :state/or}
   {:city/ident "salem",        :city/name "Salem",                :city/state :state/or}
   {:city/ident "the_dalles",   :city/name "The Dalles",           :city/state :state/or}])

(def ^:private cities-ut
  "Utah (UT, 10 cities)"
  [{:city/ident "cedar_city",   :city/name "Cedar City",           :city/state :state/ut}
   {:city/ident "logan",        :city/name "Logan",                :city/state :state/ut}
   {:city/ident "moab",         :city/name "Moab",                 :city/state :state/ut}
   {:city/ident "ogden",        :city/name "Ogden",                :city/state :state/ut}
   {:city/ident "price",        :city/name "Price",                :city/state :state/ut}
   {:city/ident "provo",        :city/name "Provo",                :city/state :state/ut}
   {:city/ident "salina",       :city/name "Salina",               :city/state :state/ut}
   {:city/ident "salt_lake",    :city/name "Salt Lake City",       :city/state :state/ut}
   {:city/ident "st_george",    :city/name "St. George",           :city/state :state/ut}
   {:city/ident "vernal",       :city/name "Vernal",               :city/state :state/ut}])

(def ^:private cities-wa
  "Washington (WA, 16 cities)"
  [{:city/ident "aberdeen_wa",  :city/name "Aberdeen",             :city/state :state/wa}
   {:city/ident "bellingham",   :city/name "Bellingham",           :city/state :state/wa}
   {:city/ident "colville",     :city/name "Colville",             :city/state :state/wa}
   {:city/ident "everett",      :city/name "Everett",              :city/state :state/wa}
   {:city/ident "grand_coulee", :city/name "Grand Coulee",         :city/state :state/wa}
   {:city/ident "kennewick",    :city/name "Kennewick",            :city/state :state/wa}
   {:city/ident "longview",     :city/name "Longview",             :city/state :state/wa}
   {:city/ident "olympia",      :city/name "Olympia",              :city/state :state/wa}
   {:city/ident "omak",         :city/name "Omak",                 :city/state :state/wa}
   {:city/ident "port_angeles", :city/name "Port Angeles",         :city/state :state/wa}
   {:city/ident "seattle",      :city/name "Seattle",              :city/state :state/wa}
   {:city/ident "spokane",      :city/name "Spokane",              :city/state :state/wa}
   {:city/ident "tacoma",       :city/name "Tacoma",               :city/state :state/wa}
   {:city/ident "vancouver",    :city/name "Vancouver",            :city/state :state/wa}
   {:city/ident "wenatchee",    :city/name "Wenatchee",            :city/state :state/wa}
   {:city/ident "yakima",       :city/name "Yakima",               :city/state :state/wa}])

(def ^:private cities-wy
  "Wyoming (WY, 11 cities)"
  [{:city/ident "casper",       :city/name "Casper",               :city/state :state/wy}
   {:city/ident "cheyenne",     :city/name "Cheyenne",             :city/state :state/wy}
   {:city/ident "cody",         :city/name "Cody",                 :city/state :state/wy}
   {:city/ident "evanston",     :city/name "Evanston",             :city/state :state/wy}
   {:city/ident "gillette",     :city/name "Gillette",             :city/state :state/wy}
   {:city/ident "jackson",      :city/name "Jackson",              :city/state :state/wy}
   {:city/ident "laramie",      :city/name "Laramie",              :city/state :state/wy}
   {:city/ident "rawlins",      :city/name "Rawlins",              :city/state :state/wy}
   {:city/ident "riverton",     :city/name "Riverton",             :city/state :state/wy}
   {:city/ident "rock_springs", :city/name "Rock Springs",         :city/state :state/wy}
   {:city/ident "sheridan",     :city/name "Sheridan",             :city/state :state/wy}])

(def ^:private cities-mt
 "Montana (MT, 15 cities)"
 [{:city/ident "billings",     :city/name "Billings",             :city/state :state/mt}
  {:city/ident "bozeman",      :city/name "Bozeman",              :city/state :state/mt}
  {:city/ident "butte",        :city/name "Butte",                :city/state :state/mt}
  {:city/ident "glasgow_mt",   :city/name "Glasgow",              :city/state :state/mt}
  {:city/ident "glendive",     :city/name "Glendive",             :city/state :state/mt}
  {:city/ident "great_falls",  :city/name "Great Falls",          :city/state :state/mt}
  {:city/ident "havre",        :city/name "Havre",                :city/state :state/mt}
  {:city/ident "helena",       :city/name "Helena",               :city/state :state/mt}
  {:city/ident "kalispell",    :city/name "Kalispell",            :city/state :state/mt}
  {:city/ident "laurel",       :city/name "Laurel",               :city/state :state/mt}
  {:city/ident "lewistown",    :city/name "Lewistown",            :city/state :state/mt}
  {:city/ident "miles_city",   :city/name "Miles City",           :city/state :state/mt}
  {:city/ident "missoula",     :city/name "Missoula",             :city/state :state/mt}
  {:city/ident "sidney",       :city/name "Sidney",               :city/state :state/mt}
  {:city/ident "thompson_f",   :city/name "Thompson Falls",       :city/state :state/mt}])

(def ^:private cities-tx
 "Texas (TX, 30 cities)"
 [{:city/ident "abilene",      :city/name "Abilene",              :city/state :state/tx}
  {:city/ident "amarillo",     :city/name "Amarillo",             :city/state :state/tx}
  {:city/ident "austin",       :city/name "Austin",               :city/state :state/tx}
  {:city/ident "beaumont",     :city/name "Beaumont",             :city/state :state/tx}
  {:city/ident "brownsville",  :city/name "Brownsville",          :city/state :state/tx}
  {:city/ident "corpus_chris", :city/name "Corpus Christi",       :city/state :state/tx}
  {:city/ident "dalhart",      :city/name "Dalhart",              :city/state :state/tx}
  {:city/ident "dallas",       :city/name "Dallas",               :city/state :state/tx}
  {:city/ident "del_rio",      :city/name "Del Rio",              :city/state :state/tx}
  {:city/ident "el_paso",      :city/name "El Paso",              :city/state :state/tx}
  {:city/ident "fort_stockto", :city/name "Fort Stockton",        :city/state :state/tx}
  {:city/ident "fort_worth",   :city/name "Fort Worth",           :city/state :state/tx}
  {:city/ident "galveston",    :city/name "Galveston",            :city/state :state/tx}
  {:city/ident "houston",      :city/name "Houston",              :city/state :state/tx}
  {:city/ident "huntsville",   :city/name "Huntsville",           :city/state :state/tx}
  {:city/ident "junction",     :city/name "Junction",             :city/state :state/tx}
  {:city/ident "laredo",       :city/name "Laredo",               :city/state :state/tx}
  {:city/ident "longview",     :city/name "Longview",             :city/state :state/tx}
  {:city/ident "lubbock",      :city/name "Lubbock",              :city/state :state/tx}
  {:city/ident "lufkin",       :city/name "Lufkin",               :city/state :state/tx}
  {:city/ident "mcallen",      :city/name "McAllen",              :city/state :state/tx}
  {:city/ident "odessa",       :city/name "Odessa",               :city/state :state/tx}
  {:city/ident "san_angelo",   :city/name "San Angelo",           :city/state :state/tx}
  {:city/ident "san_antonio",  :city/name "San Antonio",          :city/state :state/tx}
  {:city/ident "texarkana",    :city/name "Texarkana",            :city/state :state/tx}
  {:city/ident "tyler",        :city/name "Tyler",                :city/state :state/tx}
  {:city/ident "van_horn",     :city/name "Van Horn",             :city/state :state/tx}
  {:city/ident "victoria",     :city/name "Victoria",             :city/state :state/tx}
  {:city/ident "waco",         :city/name "Waco",                 :city/state :state/tx}
  {:city/ident "wichita_fall", :city/name "Wichita Falls",        :city/state :state/tx}])

(def ^:private cities-ok
 "Oklahoma (OK, 10 cities)"
 [{:city/ident "ardmore",      :city/name "Ardmore",              :city/state :state/ok}
  {:city/ident "clinton",      :city/name "Clinton",              :city/state :state/ok}
  {:city/ident "enid",         :city/name "Enid",                 :city/state :state/ok}
  {:city/ident "guymon",       :city/name "Guymon",               :city/state :state/ok}
  {:city/ident "idabel",       :city/name "Idabel",               :city/state :state/ok}
  {:city/ident "lawton",       :city/name "Lawton",               :city/state :state/ok}
  {:city/ident "mcalester",    :city/name "McAlester",            :city/state :state/ok}
  {:city/ident "oklahoma_cit", :city/name "Oklahoma City",        :city/state :state/ok}
  {:city/ident "tulsa",        :city/name "Tulsa",                :city/state :state/ok}
  {:city/ident "woodward",     :city/name "Woodward",             :city/state :state/ok}])

(def ^:private cities-ks
 "Kansas (KS, 14 cities)"
 [{:city/ident "colby",        :city/name "Colby",                :city/state :state/ks}
  {:city/ident "dodge_city",   :city/name "Dodge City",           :city/state :state/ks}
  {:city/ident "emporia",      :city/name "Emporia",              :city/state :state/ks}
  {:city/ident "garden_city",  :city/name "Garden City",          :city/state :state/ks}
  {:city/ident "hays",         :city/name "Hays",                 :city/state :state/ks}
  {:city/ident "hutchinson",   :city/name "Hutchinson",           :city/state :state/ks}
  {:city/ident "junction_cit", :city/name "Junction City",        :city/state :state/ks}
  {:city/ident "kansas_city",  :city/name "Kansas City",          :city/state :state/ks}
  {:city/ident "marysville",   :city/name "Marysville",           :city/state :state/ks}
  {:city/ident "phillipsburg", :city/name "Phillipsburg",         :city/state :state/ks}
  {:city/ident "pittsburg",    :city/name "Pittsburg",            :city/state :state/ks}
  {:city/ident "salina",       :city/name "Salina",               :city/state :state/ks}
  {:city/ident "topeka",       :city/name "Topeka",               :city/state :state/ks}
  {:city/ident "wichita",      :city/name "Wichita",              :city/state :state/ks}])

(def ^:private cities-ne
 "Nebraska (NE, 12 cities)"
 [{:city/ident "alliance",     :city/name "Alliance",             :city/state :state/ne}
  {:city/ident "chadron",      :city/name "Chadron",              :city/state :state/ne}
  {:city/ident "columbus",     :city/name "Columbus",             :city/state :state/ne}
  {:city/ident "grand_island", :city/name "Grand Island",         :city/state :state/ne}
  {:city/ident "lincoln",      :city/name "Lincoln",              :city/state :state/ne}
  {:city/ident "mccook",       :city/name "McCook",               :city/state :state/ne}
  {:city/ident "norfolk",      :city/name "Norfolk",              :city/state :state/ne}
  {:city/ident "north_platte", :city/name "North Platte",         :city/state :state/ne}
  {:city/ident "omaha",        :city/name "Omaha",                :city/state :state/ne}
  {:city/ident "sidney",       :city/name "Sidney",               :city/state :state/ne}
  {:city/ident "scottsbluff",  :city/name "Scottsbluff",          :city/state :state/ne}
  {:city/ident "valentine",    :city/name "Valentine",            :city/state :state/ne}])

(def ^:private cities
 "List of cities in ATS."
 (into [] cat [cities-az cities-ca cities-co cities-id cities-ks
               cities-mt cities-ne cities-nv cities-nm cities-ok
               cities-or cities-tx cities-ut cities-wa cities-wy]))

(def ^:private companies
 "List of companies in ATS."
 (for [[ident label]
       {"18w_trl_svc"   "18 Wheels Garage"
        "42p_pck_pln"   "42 Print"
        "42p_print"     "42 Print"
        "aml_rail_str"  "American Lines"
        "aport_abq"     "ABQ Cargo Center"
        "aport_den"     "Denver Air Cargo"
        "aport_gtf"     "Great Falls Cargo Terminal"
        "aport_pcc"     "Portland Cargo Central"
        "aport_phx"     "Phoenix Freight"
        "aport_ult"     "Ultimus"
        "avs_met_scr"   "Avalanche Steel"
        "avs_met_sml"   "Avalanche Steel"
        "az_gls_pln"    "Azure Glasswork"
        "bit_rd_grg"    "Bitumen"
        "bit_rd_svc"    "Bitumen"
        "bit_rd_wrk"    "Bitumen"
        "bn_farm"       "Bushnell Farms"
        "bn_live_auc"   "Bushnell Farms"
        "cal_farm_alm"  "Calimondo"
        "cha_el_mkt"    "Charged"
        "cha_el_whs"    "Charged"
        "chm_che_plns"  "Chemso"
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
        "du_farm"       "Darchelle Uzau"
        "dw_air_pln"    "Darwing"
        "ed_mkt"        "Eddy's"
        "fb_farm_mkt"   "Farmer's Barn"
        "fb_farm_pln"   "Farmer's Barn"
        "frd_epw_sit"   "Faraday Energy"
        "frd_epw_svc"   "Faraday Energy"
        "ftf_food_pln"  "Fish Tail Foods"
        "gal_oil_gst"   "Gallon Oil"
        "gal_oil_ref"   "Gallon Oil"
        "gal_oil_sit"   "Gallon Oil"
        "gal_oil_str"   "Gallon Oil"
        "gal_oil_str1"  "Gallon Oil"
        "gm_chs_plnt"   "Global Mills"
        "gm_food_plnt"  "Global Mills"
        "gm_food_str"   "Global Mills"
        "gol_trk_pln"   "Goliath"
        "hds_met_shp"   "Haddock Shipyard"
        "hf_wd_pln"     "Heartwood Furniture"
        "hms_con_svc"   "HMS Machinery"
        "hs_mkt"        "Home Store"
        "hs_whs"        "Home Store"
        "jns_rail_str"  "Johnson & Smith"
        "jns_rail_wrk"  "Johnson & Smith"
        "kw_trk_dlr"    "Kenworth Trucks???"
        "kw_trk_pln"    "Kenworth Trucks???"
        "lum_car_dlr"   "Lumenauto"
        "mon_farm"      "Mon Coeur Vineyards"
        "mon_food_pln"  "Mon Coeur Vineyards"
        "mwm_wst_whs"   "Municipal Waste Management"
        "nmq_min_pln1"  "NAMIQ"
        "nmq_min_plnt"  "NAMIQ"
        "nmq_min_qry"   "NAMIQ"
        "nmq_min_qrya"  "NAMIQ"
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
        "pt_trk_dlr"    "Peterbilt Trucks??"
        "re_train"      "Rail Export"
        "sc_frm"        "Sunshine Crops"
        "sc_frm_grg"    "Sunshine Crops"
        "sf_port"       "Port of San Francisco"
        "sg_whs"        "Sell Goods"
        "sh_shp_mar"    "Sea Horizon"
        "sh_shp_plnt"   "Sea Horizon"
        "st_met_whs"    "Steeler"
        "st_met_wrk"    "Steeler"
        "swb_sug_plnt"  "Sweet Beets"
        "tid_mkt"       "Tidbit"
        "usb_food_pln"  "USBB"
        "vm_car_dlr"    "Voltison Motors"
        "vm_car_exp"    "Voltison Motors"
        "vm_car_pln"    "Voltison Motors"
        "vm_car_whs"    "Voltison Motors"
        "vor_oil_gst"   "Vortex"
        "vor_oil_ref"   "Vortex"
        "vor_oil_str"   "Vortex"
        "vor_oil_str1"  "Vortex"
        "vp_epw_pln"    "Vitas Power"
        "vp_epw_sit"    "Vitas Power"
        "wal_food_mkt"  "Wallbert"
        "wal_food_whs"  "Wallbert"
        "wal_mkt"       "Wallbert"
        "wal_whs"       "Wallbert"
        "wld_frm_str"   "Walden's Landscape Supplies"
        "ws_trk_dlr"    "Western Star Trucks"
        "ws_trk_pln"    "Western Star Trucks"}]
  {:company/ident ident, :company/name label}))

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
 (into [] cat [cargoes companies cities]))

(def ^:private city-renames
 {"san_rafael" "oakland"
  "oakdale"    "modesto"
  "hornbrook"  "hilt"})

(defn canonical-city-name [city-ident]
 (get city-renames city-ident city-ident))
