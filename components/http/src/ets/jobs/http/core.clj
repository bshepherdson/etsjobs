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

(defn expiry-time [total-mins]
  (let [hours (quot total-mins 60)
        mins  (mod  total-mins 60)]
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

(defn job-block [_ctx jobs]
  (html
    [:table
     [:tr (for [h job-headings] [:th h])]
     (for [{:job/keys [cargo distance-km source target]} jobs]
       [:tr
        [:td {:style "text-align: right"} (expiry-time 300 #_expires-in-mins)] ;; XXX: debugging
        (city-td (:location/city source))
        [:td (:company/name (:location/company source))]
        (city-td (:location/city target))
        [:td (:company/name (:location/company target))]
        [:td {:style "text-align: right"} (format "%dkm" distance-km)]
        (cargo-description cargo)])]))

(defn- achievement-progress [{:keys [game db]} cheevo]
 (case game
  :ats  (atsmap/achievement-progress  db cheevo)
  :ets2 {} #_(ets2map/achievement-progress db cheevo)))

(defn- relevant-jobs [{:keys [game db]} cheevo]
 (case game
  :ats  (atsmap/relevant-jobs  db cheevo)
  :ets2 [] #_(ets2map/relevant-jobs db cheevo)))

(defn- sort-by-expiration [{:keys [time]} jobs]
 ;; Negative time remaining to get a descending sort.
  (sort-by #(- time (:offer/expiration-time %)) jobs))

(defmulti ^:private progress-block
 (fn [_ctx progress]
  (:type progress)))

(defn- progress-frame [inner]
 (html
  [:section
   [:h4 "Progress"]
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

(defmethod progress-block :count [_ctx {:keys [completed needed]}]
 (progress-frame
  (let [total (+ completed needed)]
   [:div
    [:progress {:max   total
                :value completed
                :style "margin-right: 1em"}
     (str completed " / " total)]
    (str completed " / " total)])))

;; START HERE: Output is ready, at least far enough to be getting along with.
;; Clean up the codebase, at least for ATS, and get the linter under control.
;; Then reformat everything it broke and commit it to git.
;; Then add the rest of the achievements.
(defn achievement-section [ctx {:keys [id name desc]}]
  ; Sorting by descending time-to-live.
  (let [progress (achievement-progress ctx id)
        jobs     (->> (relevant-jobs ctx id)
                      (sort-by-expiration ctx))]
    (html
      [:section
       [:h3 name]
       [:p desc]
       (progress-block ctx progress)
       #_[:pre (with-out-str (clojure.pprint/pprint progress))]
       #_[:pre (with-out-str (clojure.pprint/pprint jobs))]
       (if (empty? jobs)
         [:p {:style "font-style: italic; color: #444"} "No jobs available."]
         (job-block ctx jobs))])))

(defn region-section [ctx {:keys [name cheevos]}]
  (html
    [:section
     [:h2 name]
     #_[:p (pr-str cheevos)]
     (for [ach cheevos]
       (achievement-section ctx ach))]))

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
                  #_#_:ets2 ets2map/achievement-groups
                  :ats  atsmap/achievement-groups)]
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
