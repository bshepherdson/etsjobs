(ns ets.jobs.map)

(def cities
  {
   ; Spain (E, 39 cities)
   "a_coruna"      {:c "E"   :name "A Coruña"}
   "albacete"      {:c "E"   :name "Albacete"}
   "algeciras"     {:c "E"   :name "Algeciras"}
   "almaraz"       {:c "E"   :name "Almaraz"}
   "almeria"       {:c "E"   :name "Almería"}
   "badajoz"       {:c "E"   :name "Badajoz"}
   "bailen"        {:c "E"   :name "Bailén"}
   "barcelona"     {:c "E"   :name "Barcelona"}
   "bilbao"        {:c "E"   :name "Bilbao"}
   "burgos"        {:c "E"   :name "Burgos"}
   "ciudad_real"   {:c "E"   :name "Ciudad Real"}
   "cordoba"       {:c "E"   :name "Córdoba"}
   "el_ejido"      {:c "E"   :name "El Ejido"}
   "gijon"         {:c "E"   :name "Gijón"}
   "granada"       {:c "E"   :name "Granada"}
   "huelva"        {:c "E"   :name "Huelva"}
   "leon"          {:c "E"   :name "León"}
   "lleida"        {:c "E"   :name "Lleida"}
   "madrid"        {:c "E"   :name "Madrid"}
   "malaga"        {:c "E"   :name "Málaga"}
   "mengibar"      {:c "E"   :name "Mengíbar"}
   "murcia"        {:c "E"   :name "Murcia"}
   "navia"         {:c "E"   :name "Navia"}
   "o_barco"       {:c "E"   :name "O Barco"}
   "pamplona"      {:c "E"   :name "Pamplona"}
   "port_sagunt"   {:c "E"   :name "Port de Sagunt"}
   "puertollano"   {:c "E"   :name "Puertollano"}
   "salamanca"     {:c "E"   :name "Salamanca"}
   "santander"     {:c "E"   :name "Santander"}
   "sevilla"       {:c "E"   :name "Sevilla"}
   "soria"         {:c "E"   :name "Soria"}
   "tarragona"     {:c "E"   :name "Tarragona"}
   "teruel"        {:c "E"   :name "Teruel"}
   "valencia"      {:c "E"   :name "València"}
   "valladolid"    {:c "E"   :name "Valladolid"}
   "vandellos"     {:c "E"   :name "Vandellòs"}
   "vigo"          {:c "E"   :name "Vigo"}
   "villarreal"    {:c "E"   :name "Vila-real"}
   "zaragoza"      {:c "E"   :name "Zaragoza"}

   ; Romania (RO, 16 cities)
   "bacau"         {:c "RO"  :name "Bacău"}
   "brasov"        {:c "RO"  :name "Brașov"}
   "bucuresti"     {:c "RO"  :name "București"}
   "calarasi"      {:c "RO"  :name "Călărași"}
   "cernavoda"     {:c "RO"  :name "Cernavodă"}
   "cluj_napoca"   {:c "RO"  :name "Cluj-Napoca"}
   "constanta"     {:c "RO"  :name "Constanța"}
   "craiova"       {:c "RO"  :name "Craiova"}
   "galati"        {:c "RO"  :name "Galați"}
   "hunedoara"     {:c "RO"  :name "Romania"}
   "iasi"          {:c "RO"  :name "Iași"}
   "mangalia"      {:c "RO"  :name "Mangalia"}
   "pitesti"       {:c "RO"  :name "Pitești"}
   "resita"        {:c "RO"  :name "Reșița"}
   "targu_mures"   {:c "RO"  :name "Târgu Mureș"}
   "timisoara"     {:c "RO"  :name "Timișoara"}

   ; Italy (I, 27 cities?)
   "ancona"        {:c "I"   :name "Ancona"}
   "bari"          {:c "I"   :name "Bari"}
   "bologna"       {:c "I"   :name "Bologna"}
   "cagliari"      {:c "I"   :name "Cagliari"}
   "cassino"       {:c "I"   :name "Cassino"}
   "catania"       {:c "I"   :name "Catania"}
   "catanzaro"     {:c "I"   :name "Catanzaro"}
   "firenze"       {:c "I"   :name "Firenze"}
   "genova"        {:c "I"   :name "Genoa"}
   "livorno"       {:c "I"   :name "Livorno"}
   "messina"       {:c "I"   :name "Messina"}
   "milano"        {:c "I"   :name "Milano"}
   "napoli"        {:c "I"   :name "Napoli"}
   "olbia"         {:c "I"   :name "Olbia"}
   "palermo"       {:c "I"   :name "Palermo"}
   "parma"         {:c "I"   :name "Parma"}
   "pescara"       {:c "I"   :name "Pescara"}
   "roma"          {:c "I"   :name "Roma"}
   "sangiovanni"   {:c "I"   :name "Villa San Giovanni"}
   "sassari"       {:c "I"   :name "Sassari"}
   "suzzara"       {:c "I"   :name "Suzzara"}
   "taranto"       {:c "I"   :name "Taranto"}
   "terni"         {:c "I"   :name "Terni"}
   "torino"        {:c "I"   :name "Torino"}
   "venezia"       {:c "I"   :name "Venezia"}
   "verona"        {:c "I"   :name "Verona"}

   ; Germany (D, 22 cities)
   "berlin"        {:c "D"   :name "Berlin"}
   "bremen"        {:c "D"   :name "Bremen"}
   "dortmund"      {:c "D"   :name "Dortmund"}
   "dresden"       {:c "D"   :name "Dresden"}
   "duisburg"      {:c "D"   :name "Duisburg"}
   "dusseldorf"    {:c "D"   :name "Düsseldorf"}
   "erfurt"        {:c "D"   :name "Erfurt"}
   "frankfurt"     {:c "D"   :name "Frankfurt"}
   "hamburg"       {:c "D"   :name "Hamburg"}
   "hannover"      {:c "D"   :name "Hannover"}
   "kassel"        {:c "D"   :name "Kassel"}
   "kiel"          {:c "D"   :name "Kiel"}
   "koln"          {:c "D"   :name "Köln"}
   "leipzig"       {:c "D"   :name "Leipzig"}
   "magdeburg"     {:c "D"   :name "Magdeburg"}
   "mannheim"      {:c "D"   :name "Mannheim"}
   "munchen"       {:c "D"   :name "Munich"}
   "nurnberg"      {:c "D"   :name "Nürnberg"}
   "osnabruck"     {:c "D"   :name "Osnabrück"}
   "rostock"       {:c "D"   :name "Rostock"}
   "stuttgart"     {:c "D"   :name "Stuttgart"}
   "travemunde"    {:c "D"   :name "Travemünde"}

   ; Portugal (P, 12 cities)
   "beja"          {:c "P"   :name "Beja"}
   "coimbra"       {:c "P"   :name "Coimbra"}
   "corticadas"    {:c "P"   :name "Cortiçadas de Lavre"}
   "evora"         {:c "P"   :name "Évora"}
   "faro"          {:c "P"   :name "Faro"}
   "guarda"        {:c "P"   :name "Guarda"}
   "lisboa"        {:c "P"   :name "Lisboa"}
   "olhao"         {:c "P"   :name "Olhão"}
   "ponte_de_sor"  {:c "P"   :name "Ponte de Sor"}
   "porto"         {:c "P"   :name "Porto"}
   "setubal"       {:c "P"   :name "Setúbal"}
   "sines"         {:c "P"   :name "Sines"}

   ; Bulgaria (BG, 11 cities)
   "burgas"        {:c "BG"  :name "Burgas"}
   "karlovo"       {:c "BG"  :name "Karlovo"}
   "kozloduy"      {:c "BG"  :name "Kozloduy"}
   "pernik"        {:c "BG"  :name "Pernik"}
   "pirdop"        {:c "BG"  :name "Pirdop"}
   "pleven"        {:c "BG"  :name "Pleven"}
   "plovdiv"       {:c "BG"  :name "Plovdiv"}
   "ruse"          {:c "BG"  :name "Ruse"}
   "sofia"         {:c "BG"  :name "Sofia"}
   "varna"         {:c "BG"  :name "Varna"}
   "veli_tarnovo"  {:c "BG"  :name "Veliko Tarnovo"}

   ; Finland (FI, 10 cities)
   "helsinki"      {:c "FI"  :name "Helsinki"}
   "kotka"         {:c "FI"  :name "Kotka"}
   "kouvola"       {:c "FI"  :name "Kouvola"}
   "lahti"         {:c "FI"  :name "Lahti"}
   "loviisa"       {:c "FI"  :name "Loviisa"}
   "naantali"      {:c "FI"  :name "Naantalli"}
   "olkiluoto"     {:c "FI"  :name "Olkiluoto"}
   "pori"          {:c "FI"  :name "Pori"}
   "tampere"       {:c "FI"  :name "Tampere"}
   "turku"         {:c "FI"  :name "Turku"}

   ; United Kingdom (GB, 20 cities)
   "aberdeen"      {:c "GB"  :name "Aberdeen"}
   "birmingham"    {:c "GB"  :name "Birmingham"}
   "cambridge"     {:c "GB"  :name "Cambridge"}
   "cardiff"       {:c "GB"  :name "Cardiff"}
   "carlisle"      {:c "GB"  :name "Carlisle"}
   "dover"         {:c "GB"  :name "Dover"}
   "edinburgh"     {:c "GB"  :name "Edinburgh"}
   "felixstowe"    {:c "GB"  :name "Felixstowe"}
   "glasgow"       {:c "GB"  :name "Glasgow"}
   "grimsby"       {:c "GB"  :name "Grimsby"}
   "harwich"       {:c "GB"  :name "Harwich"}
   "hull"          {:c "GB"  :name "Hull"}
   "liverpool"     {:c "GB"  :name "Liverpool"}
   "london"        {:c "GB"  :name "London"}
   "manchester"    {:c "GB"  :name "Manchester"}
   "newcastle"     {:c "GB"  :name "Newcastle"}
   "plymouth"      {:c "GB"  :name "Plymouth"}
   "sheffield"     {:c "GB"  :name "Sheffield"}
   "southampton"   {:c "GB"  :name "Southampton"}
   "swansea"       {:c "GB"  :name "Swansea"}

   ; France (F, 36 cities (wiki is out of date...))
   "ajaccio"       {:c "F"   :name "Ajaccio"}
   "alban"         {:c "F"   :name "Saint-Alban-du-Rhône"}
   "bastia"        {:c "F"   :name "Bastia"}
   "bayonne"       {:c "F"   :name "Bayonne"}
   "bonifacio"     {:c "F"   :name "Bonifacio"}
   "bordeaux"      {:c "F"   :name "Bordeaux"}
   "bourges"       {:c "F"   :name "Bourges"}
   "brest"         {:c "F"   :name "Brest"}
   "calais"        {:c "F"   :name "Calais"}
   "calvi"         {:c "F"   :name "Calvi"}
   "civaux"        {:c "F"   :name "Civaux"}
   "clermont"      {:c "F"   :name "Clermont-Ferrand"}
   "dijon"         {:c "F"   :name "Dijon"}
   "golfech"       {:c "F"   :name "Golfech"}
   "lacq"          {:c "F"   :name "Lacq"}
   "larochelle"    {:c "F"   :name "La Rochelle"}
   "laurent"       {:c "F"   :name "Saint-Laurent"}
   "lehavre"       {:c "F"   :name "Le Havre"}
   "lemans"        {:c "F"   :name "Le Mans"}
   "lile_rousse"   {:c "F"   :name "L'Île-Rousse"}
   "lille"         {:c "F"   :name "Lille"}
   "limoges"       {:c "F"   :name "Limoges"}
   "lyon"          {:c "F"   :name "Lyon"}
   "marseille"     {:c "F"   :name "Marseille"}
   "metz"          {:c "F"   :name "Metz"}
   "montpellier"   {:c "F"   :name "Montpellier"}
   "nantes"        {:c "F"   :name "Nantes"}
   "nice"          {:c "F"   :name "Nice"}
   "paluel"        {:c "F"   :name "Paluel"}
   "paris"         {:c "F"   :name "Paris"}
   "porto_vecchi"  {:c "F"   :name "Porto-Vecchio"}
   "reims"         {:c "F"   :name "Reims"}
   "rennes"        {:c "F"   :name "Rennes"}
   "roscoff"       {:c "F"   :name "Roscoff"}
   "strasbourg"    {:c "F"   :name "Strasbourg"}
   "toulouse"      {:c "F"   :name "Toulouse"}

   ; Estonia (EST, 6 cities)
   "kunda"         {:c "EST" :name "Kunda"}
   "narva"         {:c "EST" :name "Narva"}
   "paldiski"      {:c "EST" :name "Paldiski"}
   "parnu"         {:c "EST" :name "Pärnu"}
   "tallinn"       {:c "EST" :name "Tallinn"}
   "tartu"         {:c "EST" :name "Tartu"}

   ; Latvia (LV, 6 cities)
   "daugavpils"    {:c "LV"  :name "Daugavpils"}
   "liepaja"       {:c "LV"  :name "Liepāja"}
   "rezekne"       {:c "LV"  :name "Rēzekne"}
   "riga"          {:c "LV"  :name "Rīga"}
   "valmiera"      {:c "LV"  :name "Valmiera"}
   "ventspils"     {:c "LV"  :name "Ventspils"}

   ; Lithuania (LT, 7 cities)
   "kaunas"        {:c "LT"  :name "Kaunas"}
   "klaipeda"      {:c "LT"  :name "Klaipėda"}
   "mazeikiai"     {:c "LT"  :name "Mažeikiai"}
   "panevezys"     {:c "LT"  :name "Panevėžys"}
   "siauliai"      {:c "LT"  :name "Šiauliai"}
   "utena"         {:c "LT"  :name "Utena"}
   "vilnius"       {:c "LT"  :name "Vilnius"}

   ; Sweden (S, 16 cities)
   "goteborg"      {:c "S"   :name "Göteborg"}
   "helsingborg"   {:c "S"   :name "Helsingborg"}
   "jonkoping"     {:c "S"   :name "Jönköping"}
   "kalmar"        {:c "S"   :name "Kalmar"}
   "kapellskar"    {:c "S"   :name "Kapellskär"}
   "karlskrona"    {:c "S"   :name "Karlskrona"}
   "linkoping"     {:c "S"   :name "Linköping"}
   "malmo"         {:c "S"   :name "Malmö"}
   "nynashamn"     {:c "S"   :name "Nynäshamn"}
   "orebro"        {:c "S"   :name "Orebro"}
   "sodertalje"    {:c "S"   :name "Södertälje"}
   "stockholm"     {:c "S"   :name "Stockholm"}
   "trelleborg"    {:c "S"   :name "Trelleborg"}
   "uppsala"       {:c "S"   :name "Uppsala"}
   "vasteraas"     {:c "S"   :name "Västerås"}
   "vaxjo"         {:c "S"   :name "Växjö"}

   ; Denmark (DK, 7 cities)
   "aalborg"       {:c "DK"  :name "Aalborg"}
   "esbjerg"       {:c "DK"  :name "Esbjerg"}
   "frederikshv"   {:c "DK"   :name "Frederikshavn"}
   "gedser"        {:c "DK"  :name "Gedser"}
   "hirtshals"     {:c "DK"  :name "Hirtshals"}
   "kobenhavn"     {:c "DK"  :name "København"}
   "odense"        {:c "DK"  :name "Odense"}

   ; Norway (N, 4 cities)
   "bergen"        {:c "N"   :name "Bergen"}
   "kristiansand"  {:c "N"   :name "Kritiansand"}
   "oslo"          {:c "N"   :name "Oslo"}
   "stavanger"     {:c "N"   :name "Stavanger"}

   ; Poland (PL, 11 cities)
   "bialystok"     {:c "PL"  :name "Białystok"}
   "gdansk"        {:c "PL"  :name "Gdańsk"}
   "katowice"      {:c "PL"  :name "Katowice"}
   "krakow"        {:c "PL"  :name "Kraków"}
   "lodz"          {:c "PL"  :name "Łódź"}
   "lublin"        {:c "PL"  :name "Lublin"}
   "olsztyn"       {:c "PL"  :name "Olsztyn"}
   "poznan"        {:c "PL"  :name "Poznań"}
   "szczecin"      {:c "PL"  :name "Szczecin"}
   "warszawa"      {:c "PL"  :name "Warszawa"}
   "wroclaw"       {:c "PL"  :name "Wrocław"}

   ; Belgium (B, 2 cities)
   "brussel"       {:c "B"   :name "Brussel"}
   "liege"         {:c "B"   :name "Liège"}

   ; Netherlands (NL, 3 cities + 2 port towns)
   "rotterdam"     {:c "NL"  :name "Rotterdam"}
   "groningen"     {:c "NL"  :name "Groningen"}
   "amsterdam"     {:c "NL"  :name "Amsterdam"}

   ; Austria (A, 6 cities)
   "graz"          {:c "A"   :name "Graz"}
   "innsbruck"     {:c "A"   :name "Innsbruck"}
   "klagenfurt"    {:c "A"   :name "Klagenfurt"}
   "linz"          {:c "A"   :name "Linz"}
   "salzburg"      {:c "A"   :name "Salzburg"}
   "wien"          {:c "A"   :name "Wien"}

   ; Czechia (CZ, 3 cities)
   "brno"          {:c "CZ"  :name "Brno"}
   "ostrava"       {:c "CZ"  :name "Ostrava"}
   "prague"        {:c "CZ"  :name "Praha"}

   ; Slovakia (SK, 3 cities)
   "bratislava"    {:c "SK"  :name "Bratislava"}
   "bystrica"      {:c "SK"  :name "Banská Bystrica"}
   "kosice"        {:c "SK"  :name "Kosice"}

   ; Hungary (H, 4 cities)
   "budapest"      {:c "H"   :name "Budapest"}
   "debrecen"      {:c "H"   :name "Debrecen"}
   "pecs"          {:c "H"   :name "Pécs"}
   "szeged"        {:c "H"   :name "Szeged"}

   ; Turkey (TR, 3 cities)
   "edirne"        {:c "TR"  :name "Edirne"}
   "istanbul"      {:c "TR"  :name "İstanbul"}
   "tekirdag"      {:c "TR"  :name "Tekirdağ"}

   ; Switzerland (CH, 3 cities)
   "bern"          {:c "CH"  :name "Bern"}
   "geneve"        {:c "CH"  :name "Genève"}
   "zurich"        {:c "CH"  :name "Zurich"}

   ; Luxembourg (L, 1 city)
   "luxembourg"    {:c "L"   :name "Luxembourg"}

   ; Russia (RU, 6 cities)
   "kaliningrad"   {:c "RU"  :name "Kaliningrad"}
   "luga"          {:c "RU"  :name "Luga"}
   "petersburg"    {:c "RU"  :name "St. Petersburg"}
   "pskov"         {:c "RU"  :name "Pskov"}
   "sosnovy_bor"   {:c "RU"  :name "Sosnovy Bor"}
   "vyborg"        {:c "RU"  :name "Vyborg"}
   })

(defn human-name
  "Given the machine-readable slug (eg. 'laurent') returns the human-readable
  name with country abbreviation, like in the game (eg. 'Saint-Laurent (F)').
  If the slug is unknown (the list above is ad hoc and might have missed some)
  it returns 'slug (??)'."
  [slug]
  (let [{:keys [c name]} (cities slug)]
    (if name
      (str name " (" c ")")
      (str slug " (??)"))))




(def company-names
  {
   "aaa"            "AA - Auto di Alonso"
   "acc"            "ACC"
   "aci"            "ACI SRL"
   "aerobalt"       "Aerobaltica"
   "aerobalt_ru"    "АэроБалтика"
   "agregados"      "Agregados"
   "agrominta"      "Agrominta UAB"
   "agrominta_a"    "Agrominta UAB"
   "agronord"       "Agronord"
   "app"            "APP"
   "aria_fd_albg"   "ARIA Food"
   "aria_fd_esbj"   "ARIA Food"
   "aria_fd_jnpg"   "ARIA Food"
   "aria_fd_trbg"   "ARIA Food"
   "ateria"         "Ateria AS"
   "balkan_loco"    "Balkan Loco"
   "baltomors_ru"   "Балтоморск"
   "baltomorsk"     "Baltomorsk"
   "baltrak"        "Baltrak Lojistik"
   "batisse_base"   "Bâtisse"
   "batisse_hs"     "Bâtisse"
   "batisse_road"   "Bâtisse"
   "batisse_wind"   "Bâtisse"
   "bcp"            "BCP"
   "bhb_raffin"     "BHB La Raffinerie"
   "bhv"            "BHV"
   "bjork"          "Björk"
   "blt"            "BLT - Baltic Logistic Transport"
   "blt_ru"         "Балтийский Логистический Транспорт"
   "blt_yacht"      "BLT - Baltic Logistic Transport"
   "bltmetal"       "Baltic Metallurgy"
   "bltmetal_ru"    "Балтийская Металлургия"
   "boisserie"      "Boisserie Jean-Pierre"
   "brawen"         "Brawen Transport"
   "c_navale"       "Cantiere Navale"
   "cantera"        "Cantera Ibérica"
   "canteras_ds"    "Canteras del Sur"
   "cargotras"      "Cargotras"
   "casa_olivera"   "Casa Olivera"
   "cemelt_bas"     "Cemeltex"
   "cemelt_fl_ru"   "Цемелтекс"
   "cemelt_fla"     "Cemeltex"
   "cemelt_hal"     "Cemeltex"
   "cemelt_win"     "Cemeltex"
   "cesare_smar"    "Cesare Supermercato"
   "cesta_sl"       "Cesta"
   "cgla"           "CGLA"
   "chimi"          "Chimi"
   "cnp"            "CNP"
   "comoto"         "Comoto"
   "cont_port"      "Container Port"
   "cont_port_fr"   "Port de Conteneur"
   "cont_port_it"   "Terminal Container"
   "cont_port_ru"   "Контейнерный Пункт"
   "cortica"        "Cortiça Prima"
   "costruzi_bas"   "CDE SRL"
   "costruzi_fla"   "CDE SRL"
   "costruzi_hal"   "CDE SRL"
   "costruzi_win"   "CDE SRL"
   "dans_jardin"    "Dans le Jardin"
   "dobr_ferm_bg"   "Dobra Ferma"
   "domdepo"        "Domdepo"
   "domdepo_ru"     "Домдепо"
   "drekkar"        "Drekkar Trans"
   "dulcis"         "Dulcis"
   "ee_paper"       "Estonian Paper AS"
   "egres"          "Maatila Egres"
   "elcano_fla"     "COnstrucciones Elcano"
   "engeron"        "Engeron"
   "eolo_lines"     "Eolo Lines"
   "eppa"           "EPPA"
   "euroacres"      "eAcres"
   "eurogoodies"    "EuroGoodies"
   "eviksi"         "Evikši ZS"
   "eviksi_a"       "Evikši ZS"
   "exomar"         "Exomar"
   "fallow"         "Fallow Cargo"
   "fattoria_f"     "Fattoria Felice"
   "fcp"            "FCP"
   "fintyre"        "Finnish Tyres"
   "fintyre_ru"     "Финские Шины"
   "fle"            "FLE"
   "fui"            "FUISpA"
   "gallia_ferry"   "Gallia Ferries"
   "globeur"        "Globeur"
   "gnt"            "GNT"
   "gomme_monde"    "Gomme du Monde"
   "huerta"         "Huerta del Mar Menor"
   "huilant"        "Huilant"
   "iberatomica"    "Iberatómica"
   "ibp"            "IBP"
   "ika_bohag"      "IKA Bohag"
   "ika_ru"         "ИКА Мебель"
   "imp_otel"       "Oțel Impecabil"
   "itcc"           "ITCC"
   "kaarfor"        "Kaarfor"
   "kamen_ru"       "Старый Камень"
   "kivi"           "Vanha Kivi"
   "kolico"         "Kolico"
   "konstnr"        "Konstnorr"
   "konstnr_br"     "Konstnorr"
   "konstnr_hs"     "Konstnorr"
   "konstnr_wind"   "Konstnorr"
   "ladoga"         "Ladoga Auto"
   "ladoga_ru"      "Ладога Авто"
   "lateds"         "Lateds AS"
   "lavish_food"    "Lavish Foods"
   "libellula"      "Libellula"
   "lintukainen"    "Lindakäinen"
   "lisette_log"    "Lisette Logistics"
   "lkwlog"         "LKW"
   "log_atlan"      "Logística Atlântica"
   "lognstick"      "Log n Stick"
   "low_field"      "Low Field"
   "lvr"            "LVR - Latvijas Vagonu Rūpnīca"
   "marina"         "Marina (yacht)"
   "marina_fr"      "Marina (anchor)"
   "marina_it"      "Marina (crown)"
   "marmo"          "Marmo SpA"
   "ms_stein"       "MS Stein"
   "mvm_carriere"   "MVM Carrière"
   "nbfc"           "NBFC"
   "nch"            "NCH"
   "nch_ru"         "НКХ"
   "nord_crown"     "Nordic Crown"
   "nord_sten"      "Nordic Stenbrott"
   "norr_food"      "Norrfood"
   "norrsken"       "Norrsken"
   "nos_pat_brg"    "Nos Pâturages"
   "nos_pat_cf"     "Nos Pâturages"
   "nos_pat_lhv"    "Nos Pâturages"
   "nosko"          "Noskonitta"
   "ns_chem"        "NS Chemicals"
   "ns_chem_ru"     "НС Химия"
   "ns_oil"         "NS Oil"
   "ns_oil_ru"      "НС Ойл"
   "nucleon"        "Nucléon"
   "ocean_sol"      "Ocean Solution Group"
   "onnelik"        "Õnnelik Talu"
   "onnelik_a"      "Õnnelik Talu"
   "ortiz"          "Ortiz"
   "piac"           "PIAC"
   "pk_medved_ru"   "ПК Meдвeдь"
   "polar_fish"     "Polar Fish"
   "polarislines"   "Polaris Lines"
   "posped"         "Posped"
   "pp_chimica"     "PP Chimica Italia SRL"
   "quadrelli"      "Quadrelli SpA"
   "quarry"         "Stein Bruch"
   "radus"          "Radus"
   "radus_ru"       "Радус"
   "renar"          "Renar Logistik"
   "renat"          "Renat"
   "rimaf"          "Rimaf"
   "rock_eater"     "Rock Eater Quarry"
   "rosmark"        "Rosmark"
   "rosmark_ru"     "Росмарк"
   "rt_log"         "RTLOG"
   "rump"           "Rump"
   "sag_tre"        "Sag & Tre"
   "sal"            "S.A.L. S.R.L."
   "sal_fi"         "S.A.L. S.R.L."
   "sanbuilders"    "Sanbuilders"
   "scania_dlr"     "Scania"
   "scania_fac"     "Scania"
   "scs_paper"      "SCS Paper"
   "sellplan"       "SellPlan"
   "severoatm_ru"   "Ceвepoaтoм"
   "skoda"          "Scout"
   "spinelli"       "Spinelli"
   "sporklift"      "Sporklift"
   "st_roza_bg"     "Стоманена Роза"
   "stokes"         "Stokes"
   "subse"          "Subse"
   "supercesta"     "SuperCesta"
   "suprema"        "Suprema"
   "suprema_ru"     "Супpeмa"
   "tdc_auto"       "TDC Auto Terminal"
   "te_logistica"   "TE Logistica"
   "tesore_gust"    "Tesoro Gustoso"
   "timberturtle"   "Timber Turtle"
   "tm_istanbul"    "TM-Istanbul"
   "tradeaux"       "Tradeaux"
   "trainfoundry"   "The Train Foundry"
   "trameri"        "Trameri"
   "trans_cab"      "Transportes Caballería"
   "transinet"      "Transinet"
   "tras_med"       "Trasporti Mediterraneo"
   "tree_et"        "TREE-ET"
   "ts_atlas"       "TS Atlas"
   "ttk_bg"         "Теодора"
   "ukko"           "Ukko Voima"
   "viljo_paper"    "Viljo Paperitehdas Oy"
   "viln_paper"     "VPF - Vilniaus Popieriaus Fabrikas"
   "vitas_pwr"      "Vitas Power"
   "vitas_pwr_co"   "Vitas Power"
   "voitureux"      "Voitureux"
   "volvo_fac"      "Volvo"
   "vpc"            "VPC"
   "wgcc"           "WGCC"
   "wilnet_trans"   "Wilnet Transport"
   "zelenye"        "Зелëные Поля"
   "zelenye_a"      "Зелëные Поля"
   })
