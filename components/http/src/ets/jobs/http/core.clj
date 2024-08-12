(ns ets.jobs.http.core
  (:require
   [com.wsscode.pathom3.interface.eql :as p.eql]
   [ets.jobs.ats.interface  :as atsmap]
   [ets.jobs.ets2.interface :as ets2map]
   [ets.jobs.files.interface :as files]
   [ets.jobs.search.interface :as jobs]
   [hiccup.core :refer [html]])
  (:import
   [java.io File]))

(defn ^:private human-name [game city]
  (str city "_FIXME")
  #_(case game
    :ets2 (ets2map/human-name city)
    :ats  (atsmap/human-name  city)))

(defn ^:private cargos [game slug]
  (case game
    :ets2 [] #_(ets2map/cargos slug)
    :ats  [] #_(atsmap/cargos  slug)))

(defn ^:private company-names [game slug]
  (str slug "_FIXME")
  #_(case game
    :ets2 (ets2map/company-names slug)
    :ats  (atsmap/company-names  slug)))

(comment

  (p.eql/process 
    jobs/env
    )
  )

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

(defn achievement-section [game profile {:cheevo/keys [id flag name desc]}]
  ; Sorting by descending time-to-live.
  (let [jobs-key (keyword "jobs" (clojure.core/name id))
        jobs     (jobs/jobs-query game profile [jobs-key])
        #_#_jobs (sort-by #(- (:expires-in-mins %)) (get all-jobs key))]
    (html
      [:section
       [:h3 name]
       [:p desc]
       [:p (pr-str jobs-key)]
       [:p (pr-str jobs)]
       (if (empty? jobs)
         [:p {:style "font-style: italic; color: #444"} "No jobs available."]
         [:p "jobs here"]
         #_(job-block game jobs))])))

(defn region-section [game profile {:region/keys [id name achievements]}]
  (html
    [:section
     [:h2 name]
     [:p (str "ID " id)]
     [:p (pr-str achievements)]
     (for [ach achievements]
       (achievement-section game profile ach))]))

(defn time-str [{:keys [week day hour mins]}]
  (format "Week %2d, %s %02d:%02d" week day hour mins))

(defn sanity-check [game profile]
  (let [{:time/keys [cest local zone-name]} (jobs/time-info game profile)]
    [:section
     [:h2 "Sanity Check"]
     [:p "Check you have up-to-date jobs by confirming the in-game time."]
     [:p [:strong "Time zones on:"]  " " (time-str local) " " zone-name]
     [:p [:strong "Time zones off:"] " " (time-str cest)]]))

(def ^:private region-query
  [:region/id :region/name :region/achievements])

(comment
  (jobs/jobs-query
    :ets2 "426973686F7032"
    [{:regions/all region-query}])
  )

(defn profile-body [game profile]
  (html [:div
         [:style "body {font-family: sans-serif;}
          td { padding: 0 8px; }
          td.city {display: flex;}
          .flag {margin-left: 4px}
          .grow {flex-grow: 1; min-width: 12px;}
          .adr {margin: 0 4px; padding: 0 2px;}"]
         [:h1 (str "Jobs for " profile)]
         (sanity-check game profile)

         ;; XXX: Remove this debug output
         [:ul (for [job (take 20 (jobs/jobs-query game profile [:jobs/all-available]))]
                [:li (pr-str job)])]
         
         (for [r (:regions/all (jobs/jobs-query
                                 game profile
                                 [{:regions/all region-query}]))]
           (region-section game profile r)
           #_(-> (jobs/game-meta game)
                 :regions
                 (get r)
                 (region-section game jobs)))
         #_(for [r (jobs/region-list game)])]))

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
