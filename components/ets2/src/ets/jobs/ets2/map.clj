(ns ets.jobs.ets2.map)

(def flags
  {"A"   "&#127462&#127481"
   "B"   "&#127463&#127466"
   "BG"  "&#127463&#127468"
   "CH"  "&#127464&#127469"
   "CZ"  "&#127464&#127487"
   "D"   "&#127465&#127466"
   "DK"  "&#127465&#127472"
   "E"   "&#127466&#127480"
   "EST" "&#127466&#127466"
   "F"   "&#127467&#127479"
   "FI"  "&#127467&#127470"
   "GB"  "&#127468&#127463"
   "H"   "&#127469&#127482"
   "I"   "&#127470&#127481"
   "L"   "&#127473&#127482"
   "LT"  "&#127473&#127481"
   "LV"  "&#127473&#127483"
   "N"   "&#127475&#127476"
   "NL"  "&#127475&#127473"
   "P"   "&#127477&#127481"
   "PL"  "&#127477&#127473"
   "RO"  "&#127479&#127476"
   "RU"  "&#127479&#127482"
   "S"   "&#127480&#127466"
   "SK"  "&#127480&#127472"
   "TR"  "&#127481&#127479"})

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
    {:city    (or name slug)
     :flag    (flags c)
     :country (or c "???")}))


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

(def cargos
  {"acetylene"      {:name "Acetylene" :adr #{:gases}}
   "acid"           {:name "Acid" :adr #{:corrosive}}
   "air_mails"      {:name "Air Mails"}
   "aircft_tires"   {:name "Aircraft Tyres"}
   "aircond"        {:name "Air Conditioners"}
   "almond"         {:name "Almonds"}
   "ammunition"     {:name "Ammunition" :adr #{:explosive}}
   "apples"         {:name "Apples"}
   "apples_c"       {:name "Apples"}
   "arsenic"        {:name "Arsenic" :adr #{:poison}}
   "asph_miller"    {:name "Asphalt Miller"}
   "atl_cod_flt"    {:name "Atlantic Cod Fillet"}
   "backfl_prev"    {:name "Backflow Preventers"}
   "barley"         {:name "Barley"}
   "basil"          {:name "Basil"}
   "beans"          {:name "Beans"}
   "beef_meat"      {:name "Beef"}
   "beverages"      {:name "Beverages"}
   "beverages_c"    {:name "Beverages"}
   "beverages_t"    {:name "Beverages"}
   "big_bag_seed"   {:name "Big-Bags of Seeds"}
   "boric_acid"     {:name "Boric Acid"}
   "bottle_water"   {:name "Bottled Water"}
   "brake_fluid"    {:name "Brake Fluid"}
   "brake_pads"     {:name "Brake Pads"}
   "bricks"         {:name "Bricks"}
   "cable_reel"     {:name "Industrial Cable Reel"}
   "can_sardines"   {:name "Canned Sardines"}
   "canned_beans"   {:name "Canned Beans"}
   "canned_beef"    {:name "Canned Beef"}
   "canned_pork"    {:name "Canned Pork"}
   "canned_tuna"    {:name "Canned Tuna"}
   "car_balt1"      {:name "Cars"}
   "car_balt2"      {:name "Cars?"}
   "car_ibe"        {:name "Cars"}
   "car_it"         {:name "Cars"}
   "carb_water"     {:name "Carbonated Water"}
   "carbn_pwdr_c"   {:name "Carbon Black Powder"}
   "carrots"        {:name "Carrots"}
   "carrots_c"      {:name "Carrots"}
   "cars_fr"        {:name "Cars"}
   "cauliflower"    {:name "Cauliflower"}
   "caviar"         {:name "Caviar"}
   "cement"         {:name "Cement"}
   "cheese"         {:name "Cheese"}
   "chem_sorb_c"    {:name "Chemical Sorbent"}
   "chem_sorbent"   {:name "Chemical Sorbet"}
   "chemicals"      {:name "Chemicals" :adr #{:gases}}
   "chewing_gums"   {:name "Chewing Gums"}
   "chicken_meat"   {:name "Chicken Meat"}
   "chimney_syst"   {:name "Chimney Systems"}
   "chlorine"       {:name "Chlorine" :adr #{:gases}}
   "chocolate"      {:name "Chocolate"}
   "clothes"        {:name "Clothes"}
   "clothes_c"      {:name "Clothes"}
   "coal"           {:name "Coal"}
   "coconut_milk"   {:name "Coconut Milk"}
   "coconut_oil"    {:name "Coconut Oil"}
   "comp_process"   {:name "Computer Processors"}
   "conc_juice_t"   {:name "Concentrate Juices"}
   "concen_juice"   {:name "Concentrate Juices"}
   "concr_beams"    {:name "Concrete Beams"}
   "concr_beams2"   {:name "Concrete Beams"}
   "concr_cent"     {:name "Concrete Centering"}
   "concr_stair"    {:name "Concrete Stairs"}
   "cont_trees"     {:name "Containerized Trees"}
   "contamin"       {:name "Contaminated Material" :adr #{:poison}}
   "copp_rf_gutt"   {:name "Copper Roof Gutters"}
   "corks"          {:name "Corks"}
   "cott_cheese"    {:name "Cottage Cheese"}
   "cut_flowers"    {:name "Cut Flowers"}
   "cyanide"        {:name "Cyanide" :adr #{:poison}}
   "desinfection"   {:name "Disinfectant"}
   "diesel"         {:name "Diesel" :adr #{:flammable-liquids}}
   "diesel_gen"     {:name "Diesel Generators"}
   "digger1000"     {:name "Wheel Loader"}
   "digger500"      {:name "Backhoe Loader"}
   "diggers"        {:name "Loaders"}
   "dozer"          {:name "Dozer Crawl - Z35K"}
   "driller"        {:name "Driller - D-50"}
   "dryers"         {:name "Dryers"}
   "drymilk"        {:name "Dry Milk"}
   "dynamite"       {:name "Dynamite" :adr #{:explosive}}
   "elect_wiring"   {:name "Electrical Wiring"}
   "electronics"    {:name "Electronics"}
   "emp_wine_bot"   {:name "Empty Wine Bottles"}
   "empty_barr"     {:name "Empty Barrels"}
   "empty_palet"    {:name "Empty Palettes"}
   "emptytank"      {:name "Reservoir Tank?"}
   "excav_soil"     {:name "Excavated Soil"}
   "excavator"      {:name "Excavator"}
   "exhausts_c"     {:name "Exhaust Systems"}
   "explosives"     {:name "Explosives" :adr #{:explosive}}
   "fertilizer"     {:name "Fertilizer"}
   "fireworks"      {:name "Fireworks" :adr #{:explosive}}
   "fish_chips"     {:name "Fish Fingers"}
   "floorpanels"    {:name "Floor Panels"}
   "flour"          {:name "Flour"}
   "fluorine"       {:name "Fluorine" :adr #{:gases}}
   "forklifts"      {:name "Forklifts"}
   "fresh_fish"     {:name "Fresh Fish"}
   "froz_octopi"    {:name "Frozen Octopi"}
   "frozen_hake"    {:name "Frozen Hake"}
   "frsh_herbs"     {:name "Fresh Herbs"}
   "fuel_oil"       {:name "Fuel Oil" :adr #{:flammable-liquids}}
   "fuel_tanks"     {:name "Fuel Tanks"}
   "fueltanker"     {:name "Fuel Tanker" :adr #{:flammable-liquids}}
   "furniture"      {:name "Furniture"}
   "garlic"         {:name "Garlic"}
   "glass"          {:name "Glass Panels?"}
   "glass_packed"   {:name "Packed Glass"}
   "gnocchi"        {:name "Gnocchi"}
   "goat_cheese"    {:name "Goat Cheese"}
   "granite_cube"   {:name "Granite Cube"}
   "grapes"         {:name "Grapes"}
   "graph_grease"   {:name "Graphite Grease"}
   "grass_rolls"    {:name "Grass Rolls"}
   "gravel"         {:name "Gravel"}
   "guard_rails"    {:name "Guard Rails"}
   "gummy_bears"    {:name "Gummy Bears"}
   "harvest_bins"   {:name "Harvesting Bins"}
   "hchemicals"     {:name "Hot Chemicals" :adr #{:gases}}
   "helicopter"     {:name "Helicopter - 1808TX"}
   "hi_volt_cabl"   {:name "High Voltage Cables"}
   "hipresstank"    {:name "Pressure Tank"}
   "hmetal"         {:name "Heavy Metals" :adr #{:poison}}
   "honey"          {:name "Honey"}
   "hwaste"         {:name "Hospital Waste" :adr #{:poison}}
   "ibc_cont"       {:name "IBC Containers"}
   "icecream"       {:name "Ice Cream"}
   "iced_coffee"    {:name "Canned Iced Coffee"}
   "iron_pipes"     {:name "Iron Pipes"}
   "iveco_vans"     {:name "Braco Vans"}
   "kerosene"       {:name "Kerosene" :adr #{:flammable-liquids}}
   "ketchup"        {:name "Ketchup"}
   "lamb_stom"      {:name "Lamb Stomachs"}
   "largetubes"     {:name "Large Tubes"}
   "lavender"       {:name "Lavender"}
   "lead"           {:name "Lead"}
   "limonades"      {:name "Lemonade"}
   "live_catt_fr"   {:name "Live Cattle"}
   "live_cattle"    {:name "Live Cattle"}
   "liver_paste"    {:name "Liver Paste"}
   "locomotive"     {:name "Locomotive - Vossloh G6"}
   "logs"           {:name "Logs"}
   "lpg"            {:name "LPG" :adr #{:flammable-liquids}}
   "lumber"         {:name "Lumber"}
   "lux_yacht"      {:name "Yacht - Queen V39"}
   "magnesium"      {:name "Magnesium" :adr #{:flammable-solids}}
   "maple_syrup"    {:name "Maple Syrup"}
   "marb_blck"      {:name "Marble Blocks"}
   "marb_blck2"     {:name "Marble Blocks"}
   "marb_slab"      {:name "Marble Slab"}
   "mason_jars"     {:name "Mason Jars"}
   "med_equip"      {:name "Medical Equipment"}
   "med_vaccine"    {:name "Medical Vaccines" :adr #{:poison}}
   "mercuric"       {:name "Mercuric Chloride" :adr #{:poison}}
   "metal_beams"    {:name "Metal Beams"}
   "metal_cans"     {:name "Metal Cans"}
   "metal_center"   {:name "Metal Centering"}
   "metal_pipes"    {:name "Iron Pipes"}
   "milk"           {:name "Milk"}
   "milk_t"         {:name "Milk"}
   "mobile_crane"   {:name "Mobile Crane"}
   "mondeos"        {:name "mondeos???"}
   "moto_tires"     {:name "Motorcycle Tyres"}
   "motor_oil"      {:name "Motor Oil"}
   "motor_oil_c"    {:name "Motor Oil"}
   "motorcycles"    {:name "Motorcycles"}
   "mozzarela"      {:name "Mozzarela"}
   "mtl_coil"       {:name "Metal Coil"}
   "natur_rubber"   {:name "Natural Rubber"}
   "neon"           {:name "Neon" :adr #{:gases}}
   "nitrocel"       {:name "Nitrocellulose" :adr #{:flammable-solids}}
   "nitrogen"       {:name "Nitrogen" :adr #{:gases}}
   "nonalco_beer"   {:name "Non-alcoholic Beer"}
   "nuts"           {:name "Nuts"}
   "nylon_cord"     {:name "Nylon Cord"}
   "oil"            {:name "Oil" :adr #{:flammable-liquids}}
   "oil_filt_c"     {:name "Oil Filters"}
   "oil_filters"    {:name "Oil Filters"}
   "olive_oil"      {:name "Olive Oil"}
   "olive_oil_t"    {:name "Olive Oil"}
   "olive_tree"     {:name "Olive tree??"}
   "olives"         {:name "Olives"}
   "onion"          {:name "Onions"}
   "oranges"        {:name "Oranges"}
   "ore"            {:name "Ore"}
   "outdr_flr_tl"   {:name "Outdoor Floor Tiles"}
   "overweight"     {:name "Overweight something???"}
   "packag_food"    {:name "Packaged Food"}
   "paper"          {:name "Paper"}
   "pears"          {:name "Pears"}
   "peas"           {:name "Peas"}
   "perfor_frks"    {:name "Performance Forks"}
   "pesticide"      {:name "Pesticides" :adr #{:poison}}
   "pesto"          {:name "Pesto"}
   "pet_food"       {:name "Pet Food"}
   "pet_food_c"     {:name "Pet Food"}
   "petrol"         {:name "Gasoline/Petrol" :adr #{:flammable-liquids}}
   "phosphor"       {:name "White Phosphorus" :adr #{:flammable-solids}}
   "plant_substr"   {:name "Plant Substrate"}
   "plast_film"     {:name "Plastic Film Rolls"}
   "plast_film_c"   {:name "Plastic Film Rolls"}
   "plastic_gra"    {:name "Plastic Granules"}
   "plumb_suppl"    {:name "Plumbing Supplies"}
   "plums"          {:name "Plums"}
   "pnut_butter"    {:name "Peanut Butter"}
   "polyst_box"     {:name "Polystyrene Boxes"}
   "pork_meat"      {:name "Pork"}
   "post_packag"    {:name "Post Packages"}
   "pot_flowers"    {:name "Potted Flowers"}
   "potahydro"      {:name "Potassium Hydroxide" :adr #{:corrosive}}
   "potassium"      {:name "Potassium" :adr #{:flammable-solids}}
   "potatoes"       {:name "Potatoes"}
   "precast_strs"   {:name "Precast Stairs"}
   "prosciutto"     {:name "Prosciutto"}
   "protec_cloth"   {:name "Protective Clothing"}
   "radiators"      {:name "Radiators"}
   "re_bars"        {:name "Reinforcing Bars"}
   "refl_posts"     {:name "Reflective Posts"}
   "rice"           {:name "Rice"}
   "rice_c"         {:name "Rice"}
   "roller"         {:name "Roller - DYNA CC-2200"}
   "roof_tiles"     {:name "Roof Tiles"}
   "roofing_felt"   {:name "Roofing Felt"}
   "rooflights"     {:name "Rooflights"}
   "rye"            {:name "Rye"}
   "salm_fillet"    {:name "Salmon Fillet"}
   "salt_spice_c"   {:name "Salt & Spices"}
   "salt_spices"    {:name "Salt & Spices"}
   "sand"           {:name "Sand"}
   "sandwch_pnls"   {:name "Sandwich Panels"}
   "sausages"       {:name "Sausages"}
   "sawpanels"      {:name "Sawdust Panels"}
   "scaffoldings"   {:name "Scaffoldings"}
   "scania_tr"      {:name "Scania Trucks"}
   "scooters"       {:name "Scooters"}
   "scrap_metals"   {:name "Scrap Metals"}
   "seal_bearing"   {:name "Sealed Bearings"}
   "sheep_wool"     {:name "Sheep Wool"}
   "shock_absorb"   {:name "Shock Absorbers"}
   "silica"         {:name "Silica"}
   "smokd_eel"      {:name "Smoked Eel"}
   "smokd_sprats"   {:name "Smoked Sprats"}
   "sodchlor"       {:name "Sodium Hypochloride" :adr #{:corrosive}}
   "sodhydro"       {:name "Sodium Hydroxide" :adr #{:corrosive}}
   "sodium"         {:name "Sodium" :adr #{:flammable-solids}}
   "soy_milk"       {:name "Soy Milk"}
   "soy_milk_t"     {:name "Soy Milk"}
   "sq_tub"         {:name "Square Tubings"}
   "steel_cord"     {:name "Steel Cord"}
   "stone_dust"     {:name "Stone Dust"}
   "stone_wool"     {:name "Stone Wool"}
   "stones"         {:name "Stones"}
   "straw_bales"    {:name "Straw Bales"}
   "sugar"          {:name "Sugar"}
   "sulfuric"       {:name "Sulfuric Acid" :adr #{:corrosive}}
   "tableware"      {:name "Tableware"}
   "tomatoes"       {:name "Tomatoes"}
   "toys"           {:name "Toys"}
   "tracks"         {:name "Tracks"}
   "tractor"        {:name "Crawler Tractor"}
   "tractors"       {:name "Tractors"}
   "train_part"     {:name "Train Axles?"}
   "train_part2"    {:name "Train Undercarriage?"}
   "transformat"    {:name "Transformer - PK900"}
   "transmis"       {:name "Transmissions"}
   "truck_batt"     {:name "Truck Batteries"}
   "truck_batt_c"   {:name "Truck Batteries"}
   "truck_rims"     {:name "Truck Rims"}
   "truck_rims_c"   {:name "Truck Rims"}
   "truck_tyres"    {:name "Truck Tyres"}
   "tube"           {:name "Large Tubes"}
   "tyres"          {:name "Tyres"}
   "used_battery"   {:name "Used Car Batteries"}
   "used_packag"    {:name "Used Packaging"}
   "used_plast"     {:name "Used Plastics"}
   "used_plast_c"   {:name "Used Plastics?"}
   "vent_tube"      {:name "Ventilation Shaft"}
   "vinegar"        {:name "Vinegar"}
   "vinegar_c"      {:name "Vinegar"}
   "volvo_cars"     {:name "Cars"}
   "wallpanels"     {:name "Wall Panels"}
   "watermelons"    {:name "Watermelons"}
   "wheat"          {:name "Wheat"}
   "windml_eng"     {:name "Wind Turbine Nacelle"}
   "windml_tube"    {:name "Wind Turbine Tower"}
   "wood_bark"      {:name "Wood Bark"}
   "wooden_beams"   {:name "Wooden Beams"}
   "wrk_cloth"      {:name "Work Clothes"}
   "wshavings"      {:name "Wood Shavings"}
   "yacht"          {:name "Yacht"}
   "yogurt"         {:name "Yoghurt"}
   "young_seed"     {:name "Young Seedlings"}})

