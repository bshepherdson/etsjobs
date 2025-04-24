(ns ets.jobs.http.core
  (:require
   [clojure.string :as str]
   [ets.jobs.ats.interface  :as atsmap]
   #_[ets.jobs.ets2.interface :as ets2map]
   [ets.jobs.search.interface :as jobs]
   [hiccup.core :refer [html]]))

(defn index [_]
  (let [ets-profiles (jobs/profiles :ets2)
        ats-profiles (jobs/profiles :ats)]
    {:status 200
     :headers {"Content-Type" "text/html"}
     :body
     (html [:div
            [:h1 "Choose your profile"]
            [:h2 "Euro Truck Simulator 2"]
            [:ul
             (for [{:profile/keys [name dir]} ets-profiles]
               [:li [:a {:href (str "ets2/" (.getName dir))} name]])]
            [:h2 "American Truck Simulator"]
            [:ul
             (for [{:profile/keys [name dir]} ats-profiles]
               [:li [:a {:href (str "ats/" (.getName dir))} name]])]
            [:p "Don't see your profile? Make sure Steam Cloud is"
             [:strong "disabled"]
             ", and then load it and save your game."]])}))

(defn not-found [_]
  {:status 404
   :body "Not found"})

(def job-headings
  ["Expires in" "Origin" "Sender" "Destination" "Recipient" "Distance" "Cargo"])

(defn expiry-time [ctx expires-at]
  (let [time       (-> ctx :time :cest :game)
        total-mins (- expires-at time)
        hours      (quot total-mins 60)
        mins       (mod  total-mins 60)]
    (format "%2dh%02d" hours mins)))

(def adr-symbols
  {:adr/poison
   [:span.adr.poison
    {:style "border: 1px solid black; background: white; color: black"}
    "Poison"]

   :adr/explosive
   [:span.adr.explosive
    {:style "background: orange; color: black"}
    "Exposives"]

   :adr/gases
   [:span.adr.gases
    {:style "background: #0c7; color: black"}
    "Gases"]

   :adr/flammable-liquids
   [:span.adr.flammable-liquids
    {:style "background: red; color: black"}
    "Flammable Liquids"]

   :adr/flammable-solids
   [:span.adr.flammable-liquids
    {:style "background: #35d; color: white"}
    "Flammable Solids"]

   :adr/corrosive
   [:span.adr.flammable-liquids
    {:style "background: black; color: white"}
    "Corrosive"]})

(defn cargo-description [{:cargo/keys [name adr]}]
  [:td name (when adr
              (-> adr first :db/ident adr-symbols))])

(defn city-td [city]
  [:td.city (:city/name city)
   [:span.grow]
   (let [state (-> city :city/state :db/ident)]
     [:span.country (when (= state :state/i)
                      {:style "font-family: serif"})
      (str/upper-case (name state))])
  ;; TODO: Bring back the flags for ETS2.
   #_[:span.flag "flag"]])

(defn- company-name [{:company/keys [name ident]}]
 (or name ident))

(defn job-block [ctx jobs]
  (html
   [:table
    [:tr (for [h job-headings] [:th h])]
    (for [{:offer/keys [expiration-time]
           :job/keys   [cargo distance-km source target]} jobs]
      [:tr
       [:td {:style "text-align: right"} (expiry-time ctx expiration-time)]
       (city-td (:location/city source))
       [:td (company-name (:location/company source))]
       (city-td (:location/city target))
       [:td (company-name (:location/company target))]
       [:td {:style "text-align: right"} (format "%dkm" distance-km)]
       (cargo-description cargo)])]))

(defn- oversize-block [ctx jobs]
  (html
   [:table
    [:tr
     [:th "Expires in"]
     [:th "Origin"]
     [:th ""]
     [:th "Destination"]
    ;; TODO: Is there distance for these?
     #_[:th "Distance"]
     [:th "Cargo"]]
    (for [{:offer/keys [expiration-time]
           {:template.special/keys [cargo]
            {:route.special/keys [source-city target-city]}
            :template.special/route}
           :offer.special/template} jobs
          :when (and (:city/name source-city)
                     (:city/name target-city))]
      [:tr
       [:td {:style "text-align: right"} (expiry-time ctx expiration-time)]
       (city-td source-city)
       [:td]
       (city-td target-city)
       (cargo-description cargo)])]))

(defn- achivement-info [{:keys [game db]} cheevo]
  (case game
    :ats  (atsmap/achievement-info  db cheevo)
    :ets2 {} #_(ets2map/achievement-info db cheevo)))

(defn- sort-by-expiration [{{{time :game} :cest} :time} jobs]
 ;; Negative time remaining to get a descending sort.
  (sort-by #(- time (:offer/expiration-time %)) jobs))

(defn- sort-by-city [_ctx jobs]
 (sort-by (comp (juxt (comp :db/ident :city/state) :city/name)
             :route.special/source-city
             :template.special/route
             :offer.special/template)
   jobs))

(defmulti ^:private progress-block
  (fn [_ctx {:keys [special type]}]
    (or special type)))

(defmulti ^:private completed?
  (fn [{:keys [special type]}]
    (or special type)))

(defn- progress-frame [inner]
  (html
   [:section.progress
    inner]))

(defn- progress-list [f {:keys [completed needed]}]
  (progress-frame
   [:ul.progress
    (for [x completed]
      [:li {:style "text-decoration: line-through; color: #777"}
       (f x)])
    (for [x needed]
      [:li (f x)])]))

(defmethod progress-block :set/cargo [_ctx progress]
  (progress-list :cargo/name progress))

(defmethod progress-block :set/city [_ctx progress]
  (progress-list (comp :city/name :location/city) progress))

(defmethod progress-block :set/company [_ctx progress]
  (progress-list (comp company-name :location/company) progress))

(defmethod progress-block :set/strings [_ctx progress]
  (progress-list identity progress))

(defmethod progress-block :set/oversize-route
  [_ctx {:keys [completed needed]}]
  #_[:pre (with-out-str (clojure.pprint/pprint needed))]
  (let [completed (count completed)
        total     (+ completed (count needed))]
    (progress-frame
     [:div
      [:progress {:max   total
                  :value completed
                  :style "margin-right: 1em"}
       (str completed " / " total)]
      (str completed " / " total)])))

(doseq [type [:set/cargo :set/city :set/company
              :set/strings :set/oversize-route]]
  (derive type :set/*))

(defmethod completed? :set/* [{:keys [completed needed sufficient]}]
  (if sufficient
    (>= (count completed) sufficient)
    (empty? needed)))

(defn- progress-bar [nominator denominator]
  [:div
   [:progress {:max   denominator
               :value nominator
               :style "margin-right: 1em"}
    #_(str completed " / " total)]
   (str nominator " / " denominator)])

(defmethod progress-block :count [_ctx {:keys [completed total]}]
  (progress-frame (progress-bar completed total)))

(defmethod completed? :count [{:keys [completed total]}]
  (>= completed total))

(defmethod progress-block :frequencies [_ctx {:keys [freqs]}]
  (progress-frame
   [:dl (mapcat (fn [[loc {:keys [completed total]}]]
                  [[:dt (-> loc :location/city :city/name)]
                   [:dd (progress-bar completed total)]])
                freqs)]))

(defmethod completed? :frequencies [{:keys [freqs]}]
  (every? (fn [{:keys [completed total]}]
            (>= completed total))
          (vals freqs)))

(def ^:private snake-river-pairs
  {["kennewick" "lewiston"]     "Kennewick - Lewiston"
   ["boise" "twin_falls"]       "Boise - Twin Falls"
   ["twin_falls" "pocatello"]   "Twin Falls - Pocatello"
   ["pocatello"  "idaho_falls"] "Pocatello - Idaho Falls"})

(defmethod progress-block :along-the-snake-river [_ctx progress]
  (progress-list snake-river-pairs progress))

(defmethod completed? :along-the-snake-river [{:keys [needed]}]
  (empty? needed))

(defn achievement-section [ctx cheevo-group {:keys [id name desc]}]
  ; Sorting by descending time-to-live.
  (let [{:keys [jobs progress]} (achivement-info ctx id)
        sorter                  (case cheevo-group
                                 :group/oversize sort-by-city
                                 sort-by-expiration)
        jobs                    (sorter ctx jobs)
        done?                   (completed? progress)]
    (if done?
      (html
       [:section
        [:h3 {:style "font-style: italic; color: #7c7"}
         (str "\u2713  " name)]])
      (html
       [:section
        [:h3 name]
        [:p desc]
        (progress-block ctx progress)
        (if (empty? jobs)
          [:p {:style "font-style: italic; color: #444"} "No jobs available."]
          (if (= cheevo-group :group/oversize)
            (oversize-block ctx jobs)
            (job-block ctx jobs)))]))))

(defn region-section [ctx {:keys [cheevos group name]}]
  (html
   [:section
    [:h2 name]
    #_[:p (pr-str cheevos)]
    (for [ach cheevos]
      (achievement-section ctx group ach))]))

(defn time-str [{:keys [week day hour mins]}]
  (format "Week %2d, %s %02d:%02d" week day hour mins))

(defn sanity-check [{{:keys [cest local zone-name]} :time}]
  [:section
   [:h2 "Sanity Check"]
   [:p "Check you have up-to-date jobs by confirming the in-game time."]
   [:p [:strong "Time zones on:"]  " " (time-str local) " " zone-name]
   [:p [:strong "Time zones off:"] " " (time-str cest)]])

(defn profile-body [{:keys [game profile] :as ctx}]
  (html [:div
         [:style "body {font-family: sans-serif;}
          td { padding: 0 8px; }
          td.city {display: flex;}
          .flag {margin-left: 4px}
          .grow {flex-grow: 1; min-width: 12px;}
          .adr {margin: 0 4px; padding: 0 2px;}"]
         [:h1 (str "Jobs for " (:profile/id profile))]
         (sanity-check ctx)

         (for [region (case game
                        #_#_:ets2 (ets2map/achievement-groups)
                        :ats  (atsmap/achievement-groups))]
           (region-section ctx region)
           #_(-> (jobs/game-meta game)
                 :regions
                 (get r)
                 (region-section game jobs)))
         #_(for [r (jobs/region-list game)])]))

(defn profiled [{:keys [uri]}]
  (let [[_  game-raw profile] (re-matches #"/(ats|ets2)/([\w\d]+)" uri)
        ctx (jobs/parse-latest-save (keyword game-raw) profile)]
    {:status 200
     :headers {"Content-Type" "text/html"}
     :body    (profile-body ctx)}))

(defn handler [{:keys [uri] :as req}]
  (let [h (cond
            (#{"/" "/index.html"} uri) index
            (re-matches #"/(ats|ets2)/([\w\d]+)" uri) profiled
            :else not-found)]
    (h req)))
