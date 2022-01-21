(ns ets.jobs.web
  (:require
    [ets.jobs.core :as jobs]
    [ets.jobs.util :as util]
    [hiccup.core :refer [html]]
    [ets.jobs.map :as map])
  (:import
    [java.io File]))

(defn index [request]
  (let [profiles (jobs/profiles (util/profile-root))]
    {:status 200
     :headers {"Content-Type" "text/html"}
     :body    (html [:div
                     [:h1 "Choose your profile"]
                     [:ul
                      (for [{:keys [name dir]} profiles]
                        [:li [:a {:href (.getName dir)} name]])]
                     [:p "Don't see your profile? Make sure Steam Cloud is *disabled*, and then load it and save your game."]])}))


(defn not-found [_]
  {:status 404
   :body "Not found"})


(def regions
  {:scandinavia
   {:name "Scandinavia"
    :achievements
    [{:key  :whatever-floats-your-boat
      :name "Whatever Floats Your Boat"
      :desc "Deliver to all container ports in Scandinavia (Container Port)."}

     {:key  :sailor
      :name "Sailor"
      :desc "Deliver yachts to all Scandinavian marinas (boat symbol)."}

     {:key  :volvo-trucks-lover
      :name "Volvo Trucks Lover"
      :desc "Deliver trucks from the Volvo factory."}
     {:key  :scania-trucks-lover
      :name "Scania Trucks Lover"
      :desc "Deliver trucks from the Scania factory."}

     {:key  :cattle-drive
      :name "Cattle Drive"
      :desc "Complete a livestock delivery to Scandinavia."}

     {:key  :miner
      :name "Miner"
      :desc "Deliver to all quarries in Scandinavia (Nordic Stenbrott, MS Stein)."}]}


   :baltic
   {:name "Beyond the Baltic Sea"
    :achievements
    [{:key  :concrete-jungle
      :name "Concrete Jungle"
      :desc "Complete 10 deliveries from concrete plants (Radus, Радус)"}

     {:key  :industry-standard
      :name "Industry Standard"
      :desc (str "Complete 2 deliveries to every paper mill, loco factory, and"
                 "furniture maker in the Baltic region (LVR, Renat, Viljo "
                 "Paperitehdas Oy, Estonian Paper AS, VPF).")}

     {:key  :exclave-transit
      :name "Exclave Transit"
      :desc "Complete 5 deliveries from Kaliningrad to any other Russian city."}

     {:key  :like-a-farmer
      :name "Like a Farmer"
      :desc "Deliver to each farm in the Baltic. (Agrominta UAB, Eviksi, Maatila Egres, Onnelik Talu, Zelenye Polja)"}]}

   :black-sea
   {:name "Road to the Black Sea"
    :achievements
    [{:key  :turkish-delight
      :name "Turkish Delight"
      :desc "Complete 3 deliveries from Istanbul which are at least 2500km long."}

     {:key  :along-the-black-sea
      :name "Along the Black Sea"
      :desc "Complete perfect deliveries in any order or direction between these coastal cities."}

     {:key  :orient-express
      :name "Orient Express"
      :desc "Complete deliveries between the following cities, in order: Paris, Strasbourg, Munich, Vienna, Budapest, Bucharest, Istanbul. (Requires Going East as well!)"}]}

   :italia
   {:name "Italia"
    :achievements
    [{:key  :captain
      :name "Captain"
      :desc "Deliver to all Italian shipyards. (Cantiare Navale)"}

     {:key  :michaelangelo
      :name "Michaelangelo"
      :desc "Deliver from the Carrara quarry (Marmo SpA in Livorno)."}]}

   :vive-la-france
   {:name "Vive la France"
    :achievements
    [{:key  :go-nuclear
      :name "Go Nuclear"
      :desc "Deliver to five nuclear power plants in France. (Nucleon)"}

     {:key  :check-in-check-out
      :name "Check in, Check out"
      :desc "Deliver to all cargo airport terminals in France (FLE)."}

     {:key  :gas-must-flow
      :name "Gas Must Flow"
      :desc "Deliver diesel, LPG or gasoline/petrol to all truck stops in France. (Eco)"}]}

   :iberia
   {:name "Iberia"
    :achievements
    [{:key  :lets-get-shipping
      :name "Let's Get Shipping"
      :desc "Deliver to all container ports in Iberia (TS Atlas)."}

     ; TODO Taste the Sun
     {:key  :fleet-builder
      :name "Fleet Builder"
      :desc "Deliver to all shipyards in Iberia (Ocean Solution Group)."}

     {:key  :iberian-pilgrimage
      :name "Iberian Pilgrimage"
      :desc "Deliver to A Coruña from Lisbon, Seville and Pamplona."}]}})

(def job-headings
  ["Expires in" "Origin" "Sender" "Destination" "Recipient" "Distance" "Cargo"])

(defn expiry-time [total-mins]
  (let [hours (quot total-mins 60)
        mins  (mod  total-mins 60)]
    (format "%2dh%02d" hours mins)))

(defn job-block [jobs]
  (html
    [:table
     [:tr (for [h job-headings] [:th h])]
     (for [{:keys [expires-in-mins origin sender
                   destination recipient
                   cargo distance]}                 jobs]
       [:tr
        [:td {:style "text-align: right"} (expiry-time expires-in-mins)]
        [:td (map/human-name origin)]
        [:td (map/company-names sender)]
        [:td (map/human-name destination)]
        [:td (map/company-names recipient)]
        [:td {:style "text-align: right"} (format "%dkm" distance)]
        [:td cargo]])]))

(defn achievement-section [{:keys [key name desc]} all-jobs]
  ; Sorting by descending time-to-live.
  (let [jobs (sort-by #(- (:expires-in-mins %)) (get all-jobs key))]
    (html [:section
           [:h3 name]
           [:p desc]
           (if (empty? jobs)
             [:p {:style "font-style: italic; color: #444"} "No jobs available."]
             (job-block jobs))])))

(defn region-section [{:keys [name achievements]} jobs]
  (html
    [:section
     [:h2 name]
     (for [ach achievements]
       (achievement-section ach jobs))]))

(defn time-str [{:keys [week day hour mins]}]
  (format "Week %2d, %s %02d:%02d" week day hour mins))

(defn sanity-check [s]
  (let [{:keys [zulu local tz]} (jobs/local-time s)]
    [:section
     [:h2 "Sanity Check"]
     [:p "Check you have up-to-date jobs by confirming the in-game time."]
     [:p [:strong "Time zones on:"]  " " (time-str local) " " tz]
     [:p [:strong "Time zones off:"] " " (time-str zulu)]]))

(defn profile-body [profile]
  (let [profile-dir (File. (util/profile-root) profile)
        s           (jobs/parse-latest profile-dir)
        jobs        (jobs/achievable-jobs s)]
    (html [:div
           [:style "td { padding: 0 8px }"]
           [:h1 (str "Jobs for " profile)]
           (sanity-check s)
           (for [r [:baltic :scandinavia :france :italia :iberia :black-sea]]
             (region-section (get regions r) jobs))])))


(comment
  (def d (jobs/find-jobs (File. (util/profile-root) "426973686F7032")))

  (keys d)
  )


(defn profiled [{:keys [uri]}]
  (let [profile (.substring uri 1)]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (profile-body profile)}))

(defn handler [{:keys [uri] :as req}]
  (let [h (cond
            (#{"/" "/index.html"} uri)     index
            (re-matches #"/([\w\d]+)" uri) profiled
            :else not-found)]
    (h req)))

