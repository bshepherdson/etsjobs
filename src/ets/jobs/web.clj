(ns ets.jobs.web
  (:require
    [ets.jobs.core :as jobs]
    [ets.jobs.util :as util]
    [hiccup.core :refer [html]]
    [ets.jobs.ats.map  :as atsmap]
    [ets.jobs.ets2.map :as ets2map])
  (:import
    [java.io File]))


(defn ^:private human-name [game city]
  (case game
    :ets2 (ets2map/human-name city)
    :ats  (atsmap/human-name  city)))

(defn ^:private cargos [game slug]
  (case game
    :ets2 (ets2map/cargos slug)
    :ats  (atsmap/cargos  slug)))

(defn ^:private company-names [game slug]
  (case game
    :ets2 (ets2map/company-names slug)
    :ats  (atsmap/company-names  slug)))


(defn index [_]
  (let [ets-profiles (jobs/profiles (util/profile-root :ets2))
        ats-profiles (jobs/profiles (util/profile-root :ats))]
    {:status 200
     :headers {"Content-Type" "text/html"}
     :body
     (html [:div
            [:h1 "Choose your profile"]
            [:h2 "Euro Truck Simulator 2"]
            [:ul
             (for [{:keys [name dir]} ets-profiles]
               [:li [:a {:href (str "ets2/" (.getName dir))} name]])]
            [:h2 "American Truck Simulator"]
            [:ul
             (for [{:keys [name dir]} ats-profiles]
               [:li [:a {:href (str "ats/" (.getName dir))} name]])]
            [:p "Don't see your profile? Make sure Steam Cloud is"
             [:strong "disabled"]
             ", and then load it and save your game."]])}))


(defn not-found [_]
  {:status 404
   :body "Not found"})


(def job-headings
  ["Expires in" "Origin" "Sender" "Destination" "Recipient" "Distance" "Cargo"])

(defn expiry-time [total-mins]
  (let [hours (quot total-mins 60)
        mins  (mod  total-mins 60)]
    (format "%2dh%02d" hours mins)))

(def adr-symbols
  {:poison
   [:span.adr.poison
    {:style "border: 1px solid black; background: white; color: black"}
    "Poison"]

   :explosive
   [:span.adr.explosive
    {:style "background: orange; color: black"}
    "Exposives"]

   :gases
   [:span.adr.gases
    {:style "background: #0c7; color: black"}
    "Gases"]

   :flammable-liquids
   [:span.adr.flammable-liquids
    {:style "background: red; color: black"}
    "Flammable Liquids"]

   :flammable-solids
   [:span.adr.flammable-liquids
    {:style "background: #35d; color: white"}
    "Flammable Solids"]

   :corrosive
   [:span.adr.flammable-liquids
    {:style "background: black; color: white"}
    "Corrosive"]})

(defn cargo-description [game slug]
  (let [{:keys [name adr]} (cargos game slug)]
    [:td name (when adr (adr-symbols (first adr)))]))

(defn city-td [game slug]
  (let [{:keys [city country flag]} (human-name game slug)]
    [:td.city city
     [:span.grow]
     [:span.country (when (= country "I") {:style "font-family: serif"}) country]
     [:span.flag flag]]))

(defn job-block [game jobs]
  (html
    [:table
     [:tr (for [h job-headings] [:th h])]
     (for [{:keys [expires-in-mins origin sender
                   destination recipient
                   cargo distance]}                 jobs]
       [:tr
        [:td {:style "text-align: right"} (expiry-time expires-in-mins)]
        (city-td game origin)
        [:td (company-names game sender)]
        (city-td game destination)
        [:td (company-names game recipient)]
        [:td {:style "text-align: right"} (format "%dkm" distance)]
        (cargo-description game cargo)])]))

(defn achievement-section [game {:keys [key name desc]} all-jobs]
  ; Sorting by descending time-to-live.
  (let [jobs (sort-by #(- (:expires-in-mins %)) (get all-jobs key))]
    (html [:section
           [:h3 name]
           [:p desc]
           (if (empty? jobs)
             [:p {:style "font-style: italic; color: #444"} "No jobs available."]
             (job-block game jobs))])))

(defn region-section [{:keys [name achievements]} game jobs]
  (html
    [:section
     [:h2 name]
     (for [ach achievements]
       (achievement-section game ach jobs))]))

(defn time-str [{:keys [week day hour mins]}]
  (format "Week %2d, %s %02d:%02d" week day hour mins))

(defn sanity-check [s]
  (let [{:keys [cest local tz]} (jobs/local-time s)]
    [:section
     [:h2 "Sanity Check"]
     [:p "Check you have up-to-date jobs by confirming the in-game time."]
     [:p [:strong "Time zones on:"]  " " (time-str local) " " tz]
     [:p [:strong "Time zones off:"] " " (time-str cest)]]))

(def ^:private region-list
 {:ets2 [:baltic :scandinavia :france :italia :iberia :black-sea]
  :ats  [:ca :nv :or :wa :id :ut :wy :co :nm :az]})

(defn profile-body [game profile]
  (let [profile-dir (File. (util/profile-root game) profile)
        s           (jobs/parse-latest profile-dir)
        jobs        (jobs/achievable-jobs game s)]
    (html [:div
           [:style "body {font-family: sans-serif;}
                   td { padding: 0 8px; }
                   td.city {display: flex;}
                   .flag {margin-left: 4px}
                   .grow {flex-grow: 1; min-width: 12px;}
                   .adr {margin: 0 4px; padding: 0 2px;}"]
           [:h1 (str "Jobs for " profile)]
           (sanity-check s)
           (for [r (region-list game)]
             (-> (jobs/game-meta game)
                 :regions
                 (get r)
                 (region-section game jobs)))])))


(defn profiled [{:keys [uri]}]
  (let [[_  game-raw profile] (re-matches #"/(ats|ets2)/([\w\d]+)" uri)]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (profile-body (keyword game-raw) profile)}))

(defn handler [{:keys [uri] :as req}]
  (let [h (cond
            (#{"/" "/index.html"} uri) index
            (re-matches #"/(ats|ets2)/([\w\d]+)" uri) profiled
            :else not-found)]
    (h req)))

