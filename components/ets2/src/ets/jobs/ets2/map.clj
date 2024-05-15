(ns ets.jobs.ets2.map
  (:require
   [com.wsscode.pathom3.connect.built-in.resolvers :as pbir]
   [com.wsscode.pathom3.connect.indexes :as pci]
   [com.wsscode.pathom3.connect.operation :as pco]))

(def flags
  (pbir/static-attribute-map-resolver
   :country/id :country/flag
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
    "TR"  "&#127481&#127479"}))

(def ^:private cities
  (pbir/static-table-resolver
   :city/id
   {; Spain (E, 39 cities)
    "a_coruna"      {:country/id "E"   :city/name "A Coruña"}
    "albacete"      {:country/id "E"   :city/name "Albacete"}
    "algeciras"     {:country/id "E"   :city/name "Algeciras"}
    "almaraz"       {:country/id "E"   :city/name "Almaraz"}
    "almeria"       {:country/id "E"   :city/name "Almería"}
    "badajoz"       {:country/id "E"   :city/name "Badajoz"}
    "bailen"        {:country/id "E"   :city/name "Bailén"}
    "barcelona"     {:country/id "E"   :city/name "Barcelona"}
    "bilbao"        {:country/id "E"   :city/name "Bilbao"}
    "burgos"        {:country/id "E"   :city/name "Burgos"}
    "ciudad_real"   {:country/id "E"   :city/name "Ciudad Real"}
    "cordoba"       {:country/id "E"   :city/name "Córdoba"}
    "el_ejido"      {:country/id "E"   :city/name "El Ejido"}
    "gijon"         {:country/id "E"   :city/name "Gijón"}
    "granada"       {:country/id "E"   :city/name "Granada"}
    "huelva"        {:country/id "E"   :city/name "Huelva"}
    "leon"          {:country/id "E"   :city/name "León"}
    "lleida"        {:country/id "E"   :city/name "Lleida"}
    "madrid"        {:country/id "E"   :city/name "Madrid"}
    "malaga"        {:country/id "E"   :city/name "Málaga"}
    "mengibar"      {:country/id "E"   :city/name "Mengíbar"}
    "murcia"        {:country/id "E"   :city/name "Murcia"}
    "navia"         {:country/id "E"   :city/name "Navia"}
    "o_barco"       {:country/id "E"   :city/name "O Barco"}
    "pamplona"      {:country/id "E"   :city/name "Pamplona"}
    "port_sagunt"   {:country/id "E"   :city/name "Port de Sagunt"}
    "puertollano"   {:country/id "E"   :city/name "Puertollano"}
    "salamanca"     {:country/id "E"   :city/name "Salamanca"}
    "santander"     {:country/id "E"   :city/name "Santander"}
    "sevilla"       {:country/id "E"   :city/name "Sevilla"}
    "soria"         {:country/id "E"   :city/name "Soria"}
    "tarragona"     {:country/id "E"   :city/name "Tarragona"}
    "teruel"        {:country/id "E"   :city/name "Teruel"}
    "valencia"      {:country/id "E"   :city/name "València"}
    "valladolid"    {:country/id "E"   :city/name "Valladolid"}
    "vandellos"     {:country/id "E"   :city/name "Vandellòs"}
    "vigo"          {:country/id "E"   :city/name "Vigo"}
    "villarreal"    {:country/id "E"   :city/name "Vila-real"}
    "zaragoza"      {:country/id "E"   :city/name "Zaragoza"}

    ; Romania (RO, 16 cities)
    "bacau"         {:country/id "RO"  :city/name "Bacău"}
    "brasov"        {:country/id "RO"  :city/name "Brașov"}
    "bucuresti"     {:country/id "RO"  :city/name "București"}
    "calarasi"      {:country/id "RO"  :city/name "Călărași"}
    "cernavoda"     {:country/id "RO"  :city/name "Cernavodă"}
    "cluj_napoca"   {:country/id "RO"  :city/name "Cluj-Napoca"}
    "constanta"     {:country/id "RO"  :city/name "Constanța"}
    "craiova"       {:country/id "RO"  :city/name "Craiova"}
    "galati"        {:country/id "RO"  :city/name "Galați"}
    "hunedoara"     {:country/id "RO"  :city/name "Romania"}
    "iasi"          {:country/id "RO"  :city/name "Iași"}
    "mangalia"      {:country/id "RO"  :city/name "Mangalia"}
    "pitesti"       {:country/id "RO"  :city/name "Pitești"}
    "resita"        {:country/id "RO"  :city/name "Reșița"}
    "targu_mures"   {:country/id "RO"  :city/name "Târgu Mureș"}
    "timisoara"     {:country/id "RO"  :city/name "Timișoara"}

    ; Italy (I, 27 cities?)
    "ancona"        {:country/id "I"   :city/name "Ancona"}
    "bari"          {:country/id "I"   :city/name "Bari"}
    "bologna"       {:country/id "I"   :city/name "Bologna"}
    "cagliari"      {:country/id "I"   :city/name "Cagliari"}
    "cassino"       {:country/id "I"   :city/name "Cassino"}
    "catania"       {:country/id "I"   :city/name "Catania"}
    "catanzaro"     {:country/id "I"   :city/name "Catanzaro"}
    "firenze"       {:country/id "I"   :city/name "Firenze"}
    "genova"        {:country/id "I"   :city/name "Genoa"}
    "livorno"       {:country/id "I"   :city/name "Livorno"}
    "messina"       {:country/id "I"   :city/name "Messina"}
    "milano"        {:country/id "I"   :city/name "Milano"}
    "napoli"        {:country/id "I"   :city/name "Napoli"}
    "olbia"         {:country/id "I"   :city/name "Olbia"}
    "palermo"       {:country/id "I"   :city/name "Palermo"}
    "parma"         {:country/id "I"   :city/name "Parma"}
    "pescara"       {:country/id "I"   :city/name "Pescara"}
    "roma"          {:country/id "I"   :city/name "Roma"}
    "sangiovanni"   {:country/id "I"   :city/name "Villa San Giovanni"}
    "sassari"       {:country/id "I"   :city/name "Sassari"}
    "suzzara"       {:country/id "I"   :city/name "Suzzara"}
    "taranto"       {:country/id "I"   :city/name "Taranto"}
    "terni"         {:country/id "I"   :city/name "Terni"}
    "torino"        {:country/id "I"   :city/name "Torino"}
    "trieste"       {:country/id "I"   :city/name "Trieste"}
    "venezia"       {:country/id "I"   :city/name "Venezia"}
    "verona"        {:country/id "I"   :city/name "Verona"}

    ; Germany (D, 22 cities)
    "berlin"        {:country/id "D"   :city/name "Berlin"}
    "bremen"        {:country/id "D"   :city/name "Bremen"}
    "dortmund"      {:country/id "D"   :city/name "Dortmund"}
    "dresden"       {:country/id "D"   :city/name "Dresden"}
    "duisburg"      {:country/id "D"   :city/name "Duisburg"}
    "dusseldorf"    {:country/id "D"   :city/name "Düsseldorf"}
    "erfurt"        {:country/id "D"   :city/name "Erfurt"}
    "frankfurt"     {:country/id "D"   :city/name "Frankfurt"}
    "hamburg"       {:country/id "D"   :city/name "Hamburg"}
    "hannover"      {:country/id "D"   :city/name "Hannover"}
    "kassel"        {:country/id "D"   :city/name "Kassel"}
    "kiel"          {:country/id "D"   :city/name "Kiel"}
    "koln"          {:country/id "D"   :city/name "Köln"}
    "leipzig"       {:country/id "D"   :city/name "Leipzig"}
    "magdeburg"     {:country/id "D"   :city/name "Magdeburg"}
    "mannheim"      {:country/id "D"   :city/name "Mannheim"}
    "munchen"       {:country/id "D"   :city/name "Munich"}
    "nurnberg"      {:country/id "D"   :city/name "Nürnberg"}
    "osnabruck"     {:country/id "D"   :city/name "Osnabrück"}
    "rostock"       {:country/id "D"   :city/name "Rostock"}
    "stuttgart"     {:country/id "D"   :city/name "Stuttgart"}
    "travemunde"    {:country/id "D"   :city/name "Travemünde"}

    ; Portugal (P, 12 cities)
    "beja"          {:country/id "P"   :city/name "Beja"}
    "coimbra"       {:country/id "P"   :city/name "Coimbra"}
    "corticadas"    {:country/id "P"   :city/name "Cortiçadas de Lavre"}
    "evora"         {:country/id "P"   :city/name "Évora"}
    "faro"          {:country/id "P"   :city/name "Faro"}
    "guarda"        {:country/id "P"   :city/name "Guarda"}
    "lisboa"        {:country/id "P"   :city/name "Lisboa"}
    "olhao"         {:country/id "P"   :city/name "Olhão"}
    "ponte_de_sor"  {:country/id "P"   :city/name "Ponte de Sor"}
    "porto"         {:country/id "P"   :city/name "Porto"}
    "setubal"       {:country/id "P"   :city/name "Setúbal"}
    "sines"         {:country/id "P"   :city/name "Sines"}

    ; Bulgaria (BG, 11 cities)
    "burgas"        {:country/id "BG"  :city/name "Burgas"}
    "karlovo"       {:country/id "BG"  :city/name "Karlovo"}
    "kozloduy"      {:country/id "BG"  :city/name "Kozloduy"}
    "pernik"        {:country/id "BG"  :city/name "Pernik"}
    "pirdop"        {:country/id "BG"  :city/name "Pirdop"}
    "pleven"        {:country/id "BG"  :city/name "Pleven"}
    "plovdiv"       {:country/id "BG"  :city/name "Plovdiv"}
    "ruse"          {:country/id "BG"  :city/name "Ruse"}
    "sofia"         {:country/id "BG"  :city/name "Sofia"}
    "varna"         {:country/id "BG"  :city/name "Varna"}
    "veli_tarnovo"  {:country/id "BG"  :city/name "Veliko Tarnovo"}

    ; Finland (FI, 10 cities)
    "helsinki"      {:country/id "FI"  :city/name "Helsinki"}
    "kotka"         {:country/id "FI"  :city/name "Kotka"}
    "kouvola"       {:country/id "FI"  :city/name "Kouvola"}
    "lahti"         {:country/id "FI"  :city/name "Lahti"}
    "loviisa"       {:country/id "FI"  :city/name "Loviisa"}
    "naantali"      {:country/id "FI"  :city/name "Naantalli"}
    "olkiluoto"     {:country/id "FI"  :city/name "Olkiluoto"}
    "pori"          {:country/id "FI"  :city/name "Pori"}
    "tampere"       {:country/id "FI"  :city/name "Tampere"}
    "turku"         {:country/id "FI"  :city/name "Turku"}

    ; United Kingdom (GB, 20 cities)
    "aberdeen"      {:country/id "GB"  :city/name "Aberdeen"}
    "birmingham"    {:country/id "GB"  :city/name "Birmingham"}
    "cambridge"     {:country/id "GB"  :city/name "Cambridge"}
    "cardiff"       {:country/id "GB"  :city/name "Cardiff"}
    "carlisle"      {:country/id "GB"  :city/name "Carlisle"}
    "dover"         {:country/id "GB"  :city/name "Dover"}
    "edinburgh"     {:country/id "GB"  :city/name "Edinburgh"}
    "felixstowe"    {:country/id "GB"  :city/name "Felixstowe"}
    "glasgow"       {:country/id "GB"  :city/name "Glasgow"}
    "grimsby"       {:country/id "GB"  :city/name "Grimsby"}
    "harwich"       {:country/id "GB"  :city/name "Harwich"}
    "hull"          {:country/id "GB"  :city/name "Hull"}
    "liverpool"     {:country/id "GB"  :city/name "Liverpool"}
    "london"        {:country/id "GB"  :city/name "London"}
    "manchester"    {:country/id "GB"  :city/name "Manchester"}
    "newcastle"     {:country/id "GB"  :city/name "Newcastle"}
    "plymouth"      {:country/id "GB"  :city/name "Plymouth"}
    "sheffield"     {:country/id "GB"  :city/name "Sheffield"}
    "southampton"   {:country/id "GB"  :city/name "Southampton"}
    "swansea"       {:country/id "GB"  :city/name "Swansea"}

    ; France (F, 36 cities (wiki is out of date...))
    "ajaccio"       {:country/id "F"   :city/name "Ajaccio"}
    "alban"         {:country/id "F"   :city/name "Saint-Alban-du-Rhône"}
    "bastia"        {:country/id "F"   :city/name "Bastia"}
    "bayonne"       {:country/id "F"   :city/name "Bayonne"}
    "bonifacio"     {:country/id "F"   :city/name "Bonifacio"}
    "bordeaux"      {:country/id "F"   :city/name "Bordeaux"}
    "bourges"       {:country/id "F"   :city/name "Bourges"}
    "brest"         {:country/id "F"   :city/name "Brest"}
    "calais"        {:country/id "F"   :city/name "Calais"}
    "calvi"         {:country/id "F"   :city/name "Calvi"}
    "civaux"        {:country/id "F"   :city/name "Civaux"}
    "clermont"      {:country/id "F"   :city/name "Clermont-Ferrand"}
    "dijon"         {:country/id "F"   :city/name "Dijon"}
    "golfech"       {:country/id "F"   :city/name "Golfech"}
    "lacq"          {:country/id "F"   :city/name "Lacq"}
    "larochelle"    {:country/id "F"   :city/name "La Rochelle"}
    "laurent"       {:country/id "F"   :city/name "Saint-Laurent"}
    "lehavre"       {:country/id "F"   :city/name "Le Havre"}
    "lemans"        {:country/id "F"   :city/name "Le Mans"}
    "lile_rousse"   {:country/id "F"   :city/name "L'Île-Rousse"}
    "lille"         {:country/id "F"   :city/name "Lille"}
    "limoges"       {:country/id "F"   :city/name "Limoges"}
    "lyon"          {:country/id "F"   :city/name "Lyon"}
    "marseille"     {:country/id "F"   :city/name "Marseille"}
    "metz"          {:country/id "F"   :city/name "Metz"}
    "montpellier"   {:country/id "F"   :city/name "Montpellier"}
    "nantes"        {:country/id "F"   :city/name "Nantes"}
    "nice"          {:country/id "F"   :city/name "Nice"}
    "paluel"        {:country/id "F"   :city/name "Paluel"}
    "paris"         {:country/id "F"   :city/name "Paris"}
    "porto_vecchi"  {:country/id "F"   :city/name "Porto-Vecchio"}
    "reims"         {:country/id "F"   :city/name "Reims"}
    "rennes"        {:country/id "F"   :city/name "Rennes"}
    "roscoff"       {:country/id "F"   :city/name "Roscoff"}
    "strasbourg"    {:country/id "F"   :city/name "Strasbourg"}
    "toulouse"      {:country/id "F"   :city/name "Toulouse"}

    ; Estonia (EST, 6 cities)
    "kunda"         {:country/id "EST" :city/name "Kunda"}
    "narva"         {:country/id "EST" :city/name "Narva"}
    "paldiski"      {:country/id "EST" :city/name "Paldiski"}
    "parnu"         {:country/id "EST" :city/name "Pärnu"}
    "tallinn"       {:country/id "EST" :city/name "Tallinn"}
    "tartu"         {:country/id "EST" :city/name "Tartu"}

    ; Latvia (LV, 6 cities)
    "daugavpils"    {:country/id "LV"  :city/name "Daugavpils"}
    "liepaja"       {:country/id "LV"  :city/name "Liepāja"}
    "rezekne"       {:country/id "LV"  :city/name "Rēzekne"}
    "riga"          {:country/id "LV"  :city/name "Rīga"}
    "valmiera"      {:country/id "LV"  :city/name "Valmiera"}
    "ventspils"     {:country/id "LV"  :city/name "Ventspils"}

    ; Lithuania (LT, 7 cities)
    "kaunas"        {:country/id "LT"  :city/name "Kaunas"}
    "klaipeda"      {:country/id "LT"  :city/name "Klaipėda"}
    "mazeikiai"     {:country/id "LT"  :city/name "Mažeikiai"}
    "panevezys"     {:country/id "LT"  :city/name "Panevėžys"}
    "siauliai"      {:country/id "LT"  :city/name "Šiauliai"}
    "utena"         {:country/id "LT"  :city/name "Utena"}
    "vilnius"       {:country/id "LT"  :city/name "Vilnius"}

    ; Sweden (S, 16 cities)
    "goteborg"      {:country/id "S"   :city/name "Göteborg"}
    "helsingborg"   {:country/id "S"   :city/name "Helsingborg"}
    "jonkoping"     {:country/id "S"   :city/name "Jönköping"}
    "kalmar"        {:country/id "S"   :city/name "Kalmar"}
    "kapellskar"    {:country/id "S"   :city/name "Kapellskär"}
    "karlskrona"    {:country/id "S"   :city/name "Karlskrona"}
    "linkoping"     {:country/id "S"   :city/name "Linköping"}
    "malmo"         {:country/id "S"   :city/name "Malmö"}
    "nynashamn"     {:country/id "S"   :city/name "Nynäshamn"}
    "orebro"        {:country/id "S"   :city/name "Orebro"}
    "sodertalje"    {:country/id "S"   :city/name "Södertälje"}
    "stockholm"     {:country/id "S"   :city/name "Stockholm"}
    "trelleborg"    {:country/id "S"   :city/name "Trelleborg"}
    "uppsala"       {:country/id "S"   :city/name "Uppsala"}
    "vasteraas"     {:country/id "S"   :city/name "Västerås"}
    "vaxjo"         {:country/id "S"   :city/name "Växjö"}

    ; Denmark (DK, 7 cities)
    "aalborg"       {:country/id "DK"  :city/name "Aalborg"}
    "esbjerg"       {:country/id "DK"  :city/name "Esbjerg"}
    "frederikshv"   {:country/id "DK"  :city/name "Frederikshavn"}
    "gedser"        {:country/id "DK"  :city/name "Gedser"}
    "hirtshals"     {:country/id "DK"  :city/name "Hirtshals"}
    "kobenhavn"     {:country/id "DK"  :city/name "København"}
    "odense"        {:country/id "DK"  :city/name "Odense"}

    ; Norway (N, 4 cities)
    "bergen"        {:country/id "N"   :city/name "Bergen"}
    "kristiansand"  {:country/id "N"   :city/name "Kritiansand"}
    "oslo"          {:country/id "N"   :city/name "Oslo"}
    "stavanger"     {:country/id "N"   :city/name "Stavanger"}

    ; Poland (PL, 11 cities)
    "bialystok"     {:country/id "PL"  :city/name "Białystok"}
    "gdansk"        {:country/id "PL"  :city/name "Gdańsk"}
    "katowice"      {:country/id "PL"  :city/name "Katowice"}
    "krakow"        {:country/id "PL"  :city/name "Kraków"}
    "lodz"          {:country/id "PL"  :city/name "Łódź"}
    "lublin"        {:country/id "PL"  :city/name "Lublin"}
    "olsztyn"       {:country/id "PL"  :city/name "Olsztyn"}
    "poznan"        {:country/id "PL"  :city/name "Poznań"}
    "szczecin"      {:country/id "PL"  :city/name "Szczecin"}
    "warszawa"      {:country/id "PL"  :city/name "Warszawa"}
    "wroclaw"       {:country/id "PL"  :city/name "Wrocław"}

    ; Belgium (B, 2 cities)
    "brussel"       {:country/id "B"   :city/name "Brussel"}
    "liege"         {:country/id "B"   :city/name "Liège"}

    ; Netherlands (NL, 3 cities + 2 port towns)
    "rotterdam"     {:country/id "NL"  :city/name "Rotterdam"}
    "groningen"     {:country/id "NL"  :city/name "Groningen"}
    "amsterdam"     {:country/id "NL"  :city/name "Amsterdam"}

    ; Austria (A, 6 cities)
    "graz"          {:country/id "A"   :city/name "Graz"}
    "innsbruck"     {:country/id "A"   :city/name "Innsbruck"}
    "klagenfurt"    {:country/id "A"   :city/name "Klagenfurt"}
    "linz"          {:country/id "A"   :city/name "Linz"}
    "salzburg"      {:country/id "A"   :city/name "Salzburg"}
    "wien"          {:country/id "A"   :city/name "Wien"}

    ; Czechia (CZ, 3 cities)
    "brno"          {:country/id "CZ"  :city/name "Brno"}
    "ostrava"       {:country/id "CZ"  :city/name "Ostrava"}
    "prague"        {:country/id "CZ"  :city/name "Praha"}

    ; Slovakia (SK, 3 cities)
    "bratislava"    {:country/id "SK"  :city/name "Bratislava"}
    "bystrica"      {:country/id "SK"  :city/name "Banská Bystrica"}
    "kosice"        {:country/id "SK"  :city/name "Kosice"}

    ; Hungary (H, 4 cities)
    "budapest"      {:country/id "H"   :city/name "Budapest"}
    "debrecen"      {:country/id "H"   :city/name "Debrecen"}
    "pecs"          {:country/id "H"   :city/name "Pécs"}
    "szeged"        {:country/id "H"   :city/name "Szeged"}

    ; Turkey (TR, 3 cities)
    "edirne"        {:country/id "TR"  :city/name "Edirne"}
    "istanbul"      {:country/id "TR"  :city/name "İstanbul"}
    "tekirdag"      {:country/id "TR"  :city/name "Tekirdağ"}

    ; Switzerland (CH, 3 cities)
    "bern"          {:country/id "CH"  :city/name "Bern"}
    "geneve"        {:country/id "CH"  :city/name "Genève"}
    "zurich"        {:country/id "CH"  :city/name "Zurich"}

    ; Luxembourg (L, 1 city)
    "luxembourg"    {:country/id "L"   :city/name "Luxembourg"}

    ; Russia (RU, 6 cities)
    "kaliningrad"   {:country/id "RU"  :city/name "Kaliningrad"}
    "luga"          {:country/id "RU"  :city/name "Luga"}
    "petersburg"    {:country/id "RU"  :city/name "St. Petersburg"}
    "pskov"         {:country/id "RU"  :city/name "Pskov"}
    "sosnovy_bor"   {:country/id "RU"  :city/name "Sosnovy Bor"}
    "vyborg"        {:country/id "RU"  :city/name "Vyborg"}}))

(pco/defresolver city-human-name
  "Given the machine-readable slug (eg. 'laurent') returns the human-readable
  name with country abbreviation, like in the game (eg. 'Saint-Laurent (F)').

  If the slug is unknown (the table of cities is ad hoc and might have missed some)
  it returns 'slug (??)'."
  [{city-name :city/name
    country   :country/id
    slug      :city/id}]
  {::pco/input [:city/id (pco/? :city/name) (pco/? :country/id)]}
  {:city/human-name   (or city-name slug)
   :city/country-code (or country "???")})

(def ^:private company-names
  (pbir/static-attribute-map-resolver
   :company/id :company/name
   {"aaa"            "AA - Auto di Alonso"
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
    "zelenye_a"      "Зелëные Поля"}))

(def ^:private cargos
  (pbir/static-table-resolver :cargo/id
   {"acetylene"      {:cargo/name "Acetylene" :cargo/adr #{:adr/gases}}
    "acid"           {:cargo/name "Acid" :cargo/adr #{:adr/corrosive}}
    "air_mails"      {:cargo/name "Air Mails"}
    "aircft_tires"   {:cargo/name "Aircraft Tyres"}
    "aircond"        {:cargo/name "Air Conditioners"}
    "almond"         {:cargo/name "Almonds"}
    "ammunition"     {:cargo/name "Ammunition" :cargo/adr #{:adr/explosive}}
    "apples"         {:cargo/name "Apples"}
    "apples_c"       {:cargo/name "Apples"}
    "arsenic"        {:cargo/name "Arsenic" :cargo/adr #{:adr/poison}}
    "asph_miller"    {:cargo/name "Asphalt Miller"}
    "atl_cod_flt"    {:cargo/name "Atlantic Cod Fillet"}
    "backfl_prev"    {:cargo/name "Backflow Preventers"}
    "barley"         {:cargo/name "Barley"}
    "basil"          {:cargo/name "Basil"}
    "beans"          {:cargo/name "Beans"}
    "beef_meat"      {:cargo/name "Beef"}
    "beverages"      {:cargo/name "Beverages"}
    "beverages_c"    {:cargo/name "Beverages"}
    "beverages_t"    {:cargo/name "Beverages"}
    "big_bag_seed"   {:cargo/name "Big-Bags of Seeds"}
    "boric_acid"     {:cargo/name "Boric Acid"}
    "bottle_water"   {:cargo/name "Bottled Water"}
    "brake_fluid"    {:cargo/name "Brake Fluid"}
    "brake_pads"     {:cargo/name "Brake Pads"}
    "bricks"         {:cargo/name "Bricks"}
    "cable_reel"     {:cargo/name "Industrial Cable Reel"}
    "can_sardines"   {:cargo/name "Canned Sardines"}
    "canned_beans"   {:cargo/name "Canned Beans"}
    "canned_beef"    {:cargo/name "Canned Beef"}
    "canned_pork"    {:cargo/name "Canned Pork"}
    "canned_tuna"    {:cargo/name "Canned Tuna"}
    "car_balt1"      {:cargo/name "Cars"}
    "car_balt2"      {:cargo/name "Cars?"}
    "car_ibe"        {:cargo/name "Cars"}
    "car_it"         {:cargo/name "Cars"}
    "carb_water"     {:cargo/name "Carbonated Water"}
    "carbn_pwdr_c"   {:cargo/name "Carbon Black Powder"}
    "carrots"        {:cargo/name "Carrots"}
    "carrots_c"      {:cargo/name "Carrots"}
    "cars_fr"        {:cargo/name "Cars"}
    "cauliflower"    {:cargo/name "Cauliflower"}
    "caviar"         {:cargo/name "Caviar"}
    "cement"         {:cargo/name "Cement"}
    "cheese"         {:cargo/name "Cheese"}
    "chem_sorb_c"    {:cargo/name "Chemical Sorbent"}
    "chem_sorbent"   {:cargo/name "Chemical Sorbet"}
    "chemicals"      {:cargo/name "Chemicals" :cargo/adr #{:adr/gases}}
    "chewing_gums"   {:cargo/name "Chewing Gums"}
    "chicken_meat"   {:cargo/name "Chicken Meat"}
    "chimney_syst"   {:cargo/name "Chimney Systems"}
    "chlorine"       {:cargo/name "Chlorine" :cargo/adr #{:adr/gases}}
    "chocolate"      {:cargo/name "Chocolate"}
    "clothes"        {:cargo/name "Clothes"}
    "clothes_c"      {:cargo/name "Clothes"}
    "coal"           {:cargo/name "Coal"}
    "coconut_milk"   {:cargo/name "Coconut Milk"}
    "coconut_oil"    {:cargo/name "Coconut Oil"}
    "comp_process"   {:cargo/name "Computer Processors"}
    "conc_juice_t"   {:cargo/name "Concentrate Juices"}
    "concen_juice"   {:cargo/name "Concentrate Juices"}
    "concr_beams"    {:cargo/name "Concrete Beams"}
    "concr_beams2"   {:cargo/name "Concrete Beams"}
    "concr_cent"     {:cargo/name "Concrete Centering"}
    "concr_stair"    {:cargo/name "Concrete Stairs"}
    "cont_trees"     {:cargo/name "Containerized Trees"}
    "contamin"       {:cargo/name "Contaminated Material" :cargo/adr #{:adr/poison}}
    "copp_rf_gutt"   {:cargo/name "Copper Roof Gutters"}
    "corks"          {:cargo/name "Corks"}
    "cott_cheese"    {:cargo/name "Cottage Cheese"}
    "cut_flowers"    {:cargo/name "Cut Flowers"}
    "cyanide"        {:cargo/name "Cyanide" :cargo/adr #{:adr/poison}}
    "desinfection"   {:cargo/name "Disinfectant"}
    "diesel"         {:cargo/name "Diesel" :cargo/adr #{:adr/flammable-liquids}}
    "diesel_gen"     {:cargo/name "Diesel Generators"}
    "digger1000"     {:cargo/name "Wheel Loader"}
    "digger500"      {:cargo/name "Backhoe Loader"}
    "diggers"        {:cargo/name "Loaders"}
    "dozer"          {:cargo/name "Dozer Crawl - Z35K"}
    "driller"        {:cargo/name "Driller - D-50"}
    "dryers"         {:cargo/name "Dryers"}
    "drymilk"        {:cargo/name "Dry Milk"}
    "dynamite"       {:cargo/name "Dynamite" :cargo/adr #{:adr/explosive}}
    "elect_wiring"   {:cargo/name "Electrical Wiring"}
    "electronics"    {:cargo/name "Electronics"}
    "emp_wine_bot"   {:cargo/name "Empty Wine Bottles"}
    "empty_barr"     {:cargo/name "Empty Barrels"}
    "empty_palet"    {:cargo/name "Empty Palettes"}
    "emptytank"      {:cargo/name "Reservoir Tank?"}
    "excav_soil"     {:cargo/name "Excavated Soil"}
    "excavator"      {:cargo/name "Excavator"}
    "exhausts_c"     {:cargo/name "Exhaust Systems"}
    "explosives"     {:cargo/name "Explosives" :cargo/adr #{:adr/explosive}}
    "fertilizer"     {:cargo/name "Fertilizer"}
    "fireworks"      {:cargo/name "Fireworks" :cargo/adr #{:adr/explosive}}
    "fish_chips"     {:cargo/name "Fish Fingers"}
    "floorpanels"    {:cargo/name "Floor Panels"}
    "flour"          {:cargo/name "Flour"}
    "fluorine"       {:cargo/name "Fluorine" :cargo/adr #{:adr/gases}}
    "forklifts"      {:cargo/name "Forklifts"}
    "fresh_fish"     {:cargo/name "Fresh Fish"}
    "froz_octopi"    {:cargo/name "Frozen Octopi"}
    "frozen_hake"    {:cargo/name "Frozen Hake"}
    "frsh_herbs"     {:cargo/name "Fresh Herbs"}
    "fuel_oil"       {:cargo/name "Fuel Oil" :cargo/adr #{:adr/flammable-liquids}}
    "fuel_tanks"     {:cargo/name "Fuel Tanks"}
    "fueltanker"     {:cargo/name "Fuel Tanker" :cargo/adr #{:adr/flammable-liquids}}
    "furniture"      {:cargo/name "Furniture"}
    "garlic"         {:cargo/name "Garlic"}
    "glass"          {:cargo/name "Glass Panels?"}
    "glass_packed"   {:cargo/name "Packed Glass"}
    "gnocchi"        {:cargo/name "Gnocchi"}
    "goat_cheese"    {:cargo/name "Goat Cheese"}
    "granite_cube"   {:cargo/name "Granite Cube"}
    "grapes"         {:cargo/name "Grapes"}
    "graph_grease"   {:cargo/name "Graphite Grease"}
    "grass_rolls"    {:cargo/name "Grass Rolls"}
    "gravel"         {:cargo/name "Gravel"}
    "guard_rails"    {:cargo/name "Guard Rails"}
    "gummy_bears"    {:cargo/name "Gummy Bears"}
    "harvest_bins"   {:cargo/name "Harvesting Bins"}
    "hchemicals"     {:cargo/name "Hot Chemicals" :cargo/adr #{:adr/gases}}
    "helicopter"     {:cargo/name "Helicopter - 1808TX"}
    "hi_volt_cabl"   {:cargo/name "High Voltage Cables"}
    "hipresstank"    {:cargo/name "Pressure Tank"}
    "hmetal"         {:cargo/name "Heavy Metals" :cargo/adr #{:adr/poison}}
    "honey"          {:cargo/name "Honey"}
    "hwaste"         {:cargo/name "Hospital Waste" :cargo/adr #{:adr/poison}}
    "ibc_cont"       {:cargo/name "IBC Containers"}
    "icecream"       {:cargo/name "Ice Cream"}
    "iced_coffee"    {:cargo/name "Canned Iced Coffee"}
    "iron_pipes"     {:cargo/name "Iron Pipes"}
    "iveco_vans"     {:cargo/name "Braco Vans"}
    "kerosene"       {:cargo/name "Kerosene" :cargo/adr #{:adr/flammable-liquids}}
    "ketchup"        {:cargo/name "Ketchup"}
    "lamb_stom"      {:cargo/name "Lamb Stomachs"}
    "largetubes"     {:cargo/name "Large Tubes"}
    "lavender"       {:cargo/name "Lavender"}
    "lead"           {:cargo/name "Lead"}
    "limonades"      {:cargo/name "Lemonade"}
    "live_catt_fr"   {:cargo/name "Live Cattle"}
    "live_cattle"    {:cargo/name "Live Cattle"}
    "liver_paste"    {:cargo/name "Liver Paste"}
    "locomotive"     {:cargo/name "Locomotive - Vossloh G6"}
    "logs"           {:cargo/name "Logs"}
    "lpg"            {:cargo/name "LPG" :cargo/adr #{:adr/flammable-liquids}}
    "lumber"         {:cargo/name "Lumber"}
    "lux_yacht"      {:cargo/name "Yacht - Queen V39"}
    "magnesium"      {:cargo/name "Magnesium" :cargo/adr #{:adr/flammable-solids}}
    "maple_syrup"    {:cargo/name "Maple Syrup"}
    "marb_blck"      {:cargo/name "Marble Blocks"}
    "marb_blck2"     {:cargo/name "Marble Blocks"}
    "marb_slab"      {:cargo/name "Marble Slab"}
    "mason_jars"     {:cargo/name "Mason Jars"}
    "med_equip"      {:cargo/name "Medical Equipment"}
    "med_vaccine"    {:cargo/name "Medical Vaccines" :cargo/adr #{:adr/poison}}
    "mercuric"       {:cargo/name "Mercuric Chloride" :cargo/adr #{:adr/poison}}
    "metal_beams"    {:cargo/name "Metal Beams"}
    "metal_cans"     {:cargo/name "Metal Cans"}
    "metal_center"   {:cargo/name "Metal Centering"}
    "metal_pipes"    {:cargo/name "Iron Pipes"}
    "milk"           {:cargo/name "Milk"}
    "milk_t"         {:cargo/name "Milk"}
    "mobile_crane"   {:cargo/name "Mobile Crane"}
    "mondeos"        {:cargo/name "mondeos???"}
    "moto_tires"     {:cargo/name "Motorcycle Tyres"}
    "motor_oil"      {:cargo/name "Motor Oil"}
    "motor_oil_c"    {:cargo/name "Motor Oil"}
    "motorcycles"    {:cargo/name "Motorcycles"}
    "mozzarela"      {:cargo/name "Mozzarela"}
    "mtl_coil"       {:cargo/name "Metal Coil"}
    "natur_rubber"   {:cargo/name "Natural Rubber"}
    "neon"           {:cargo/name "Neon" :cargo/adr #{:adr/gases}}
    "nitrocel"       {:cargo/name "Nitrocellulose" :cargo/adr #{:adr/flammable-solids}}
    "nitrogen"       {:cargo/name "Nitrogen" :cargo/adr #{:adr/gases}}
    "nonalco_beer"   {:cargo/name "Non-alcoholic Beer"}
    "nuts"           {:cargo/name "Nuts"}
    "nylon_cord"     {:cargo/name "Nylon Cord"}
    "oil"            {:cargo/name "Oil" :cargo/adr #{:adr/flammable-liquids}}
    "oil_filt_c"     {:cargo/name "Oil Filters"}
    "oil_filters"    {:cargo/name "Oil Filters"}
    "olive_oil"      {:cargo/name "Olive Oil"}
    "olive_oil_t"    {:cargo/name "Olive Oil"}
    "olive_tree"     {:cargo/name "Olive tree??"}
    "olives"         {:cargo/name "Olives"}
    "onion"          {:cargo/name "Onions"}
    "oranges"        {:cargo/name "Oranges"}
    "ore"            {:cargo/name "Ore"}
    "outdr_flr_tl"   {:cargo/name "Outdoor Floor Tiles"}
    "overweight"     {:cargo/name "Overweight something???"}
    "packag_food"    {:cargo/name "Packaged Food"}
    "paper"          {:cargo/name "Paper"}
    "pears"          {:cargo/name "Pears"}
    "peas"           {:cargo/name "Peas"}
    "perfor_frks"    {:cargo/name "Performance Forks"}
    "pesticide"      {:cargo/name "Pesticides" :cargo/adr #{:adr/poison}}
    "pesto"          {:cargo/name "Pesto"}
    "pet_food"       {:cargo/name "Pet Food"}
    "pet_food_c"     {:cargo/name "Pet Food"}
    "petrol"         {:cargo/name "Gasoline/Petrol" :cargo/adr #{:adr/flammable-liquids}}
    "phosphor"       {:cargo/name "White Phosphorus" :cargo/adr #{:adr/flammable-solids}}
    "plant_substr"   {:cargo/name "Plant Substrate"}
    "plast_film"     {:cargo/name "Plastic Film Rolls"}
    "plast_film_c"   {:cargo/name "Plastic Film Rolls"}
    "plastic_gra"    {:cargo/name "Plastic Granules"}
    "plumb_suppl"    {:cargo/name "Plumbing Supplies"}
    "plums"          {:cargo/name "Plums"}
    "pnut_butter"    {:cargo/name "Peanut Butter"}
    "polyst_box"     {:cargo/name "Polystyrene Boxes"}
    "pork_meat"      {:cargo/name "Pork"}
    "post_packag"    {:cargo/name "Post Packages"}
    "pot_flowers"    {:cargo/name "Potted Flowers"}
    "potahydro"      {:cargo/name "Potassium Hydroxide" :cargo/adr #{:adr/corrosive}}
    "potassium"      {:cargo/name "Potassium" :cargo/adr #{:adr/flammable-solids}}
    "potatoes"       {:cargo/name "Potatoes"}
    "precast_strs"   {:cargo/name "Precast Stairs"}
    "prosciutto"     {:cargo/name "Prosciutto"}
    "protec_cloth"   {:cargo/name "Protective Clothing"}
    "radiators"      {:cargo/name "Radiators"}
    "re_bars"        {:cargo/name "Reinforcing Bars"}
    "refl_posts"     {:cargo/name "Reflective Posts"}
    "rice"           {:cargo/name "Rice"}
    "rice_c"         {:cargo/name "Rice"}
    "roller"         {:cargo/name "Roller - DYNA CC-2200"}
    "roof_tiles"     {:cargo/name "Roof Tiles"}
    "roofing_felt"   {:cargo/name "Roofing Felt"}
    "rooflights"     {:cargo/name "Rooflights"}
    "rye"            {:cargo/name "Rye"}
    "salm_fillet"    {:cargo/name "Salmon Fillet"}
    "salt_spice_c"   {:cargo/name "Salt & Spices"}
    "salt_spices"    {:cargo/name "Salt & Spices"}
    "sand"           {:cargo/name "Sand"}
    "sandwch_pnls"   {:cargo/name "Sandwich Panels"}
    "sausages"       {:cargo/name "Sausages"}
    "sawpanels"      {:cargo/name "Sawdust Panels"}
    "scaffoldings"   {:cargo/name "Scaffoldings"}
    "scania_tr"      {:cargo/name "Scania Trucks"}
    "scooters"       {:cargo/name "Scooters"}
    "scrap_metals"   {:cargo/name "Scrap Metals"}
    "seal_bearing"   {:cargo/name "Sealed Bearings"}
    "sheep_wool"     {:cargo/name "Sheep Wool"}
    "shock_absorb"   {:cargo/name "Shock Absorbers"}
    "silica"         {:cargo/name "Silica"}
    "smokd_eel"      {:cargo/name "Smoked Eel"}
    "smokd_sprats"   {:cargo/name "Smoked Sprats"}
    "sodchlor"       {:cargo/name "Sodium Hypochloride" :cargo/adr #{:adr/corrosive}}
    "sodhydro"       {:cargo/name "Sodium Hydroxide" :cargo/adr #{:adr/corrosive}}
    "sodium"         {:cargo/name "Sodium" :cargo/adr #{:adr/flammable-solids}}
    "soy_milk"       {:cargo/name "Soy Milk"}
    "soy_milk_t"     {:cargo/name "Soy Milk"}
    "sq_tub"         {:cargo/name "Square Tubings"}
    "steel_cord"     {:cargo/name "Steel Cord"}
    "stone_dust"     {:cargo/name "Stone Dust"}
    "stone_wool"     {:cargo/name "Stone Wool"}
    "stones"         {:cargo/name "Stones"}
    "straw_bales"    {:cargo/name "Straw Bales"}
    "sugar"          {:cargo/name "Sugar"}
    "sulfuric"       {:cargo/name "Sulfuric Acid" :cargo/adr #{:adr/corrosive}}
    "tableware"      {:cargo/name "Tableware"}
    "tomatoes"       {:cargo/name "Tomatoes"}
    "toys"           {:cargo/name "Toys"}
    "tracks"         {:cargo/name "Tracks"}
    "tractor"        {:cargo/name "Crawler Tractor"}
    "tractors"       {:cargo/name "Tractors"}
    "train_part"     {:cargo/name "Train Axles?"}
    "train_part2"    {:cargo/name "Train Undercarriage?"}
    "transformat"    {:cargo/name "Transformer - PK900"}
    "transmis"       {:cargo/name "Transmissions"}
    "truck_batt"     {:cargo/name "Truck Batteries"}
    "truck_batt_c"   {:cargo/name "Truck Batteries"}
    "truck_rims"     {:cargo/name "Truck Rims"}
    "truck_rims_c"   {:cargo/name "Truck Rims"}
    "truck_tyres"    {:cargo/name "Truck Tyres"}
    "tube"           {:cargo/name "Large Tubes"}
    "tyres"          {:cargo/name "Tyres"}
    "used_battery"   {:cargo/name "Used Car Batteries"}
    "used_packag"    {:cargo/name "Used Packaging"}
    "used_plast"     {:cargo/name "Used Plastics"}
    "used_plast_c"   {:cargo/name "Used Plastics?"}
    "vent_tube"      {:cargo/name "Ventilation Shaft"}
    "vinegar"        {:cargo/name "Vinegar"}
    "vinegar_c"      {:cargo/name "Vinegar"}
    "volvo_cars"     {:cargo/name "Cars"}
    "wallpanels"     {:cargo/name "Wall Panels"}
    "watermelons"    {:cargo/name "Watermelons"}
    "wheat"          {:cargo/name "Wheat"}
    "windml_eng"     {:cargo/name "Wind Turbine Nacelle"}
    "windml_tube"    {:cargo/name "Wind Turbine Tower"}
    "wood_bark"      {:cargo/name "Wood Bark"}
    "wooden_beams"   {:cargo/name "Wooden Beams"}
    "wrk_cloth"      {:cargo/name "Work Clothes"}
    "wshavings"      {:cargo/name "Wood Shavings"}
    "yacht"          {:cargo/name "Yacht"}
    "yogurt"         {:cargo/name "Yoghurt"}
    "young_seed"     {:cargo/name "Young Seedlings"}}))

(def index
  (pci/register [flags cities cargos company-names city-human-name]))
