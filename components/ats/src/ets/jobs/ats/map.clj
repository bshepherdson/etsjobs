(ns ets.jobs.ats.map
  (:require
   [clojure.string :as str]
   [ets.jobs.files.interface :as files]
   [ets.jobs.scs.interface :as scs]))

(defn- ->state-kw [country-code]
  (keyword "state" (str/lower-case country-code)))

(defn- tx-country [{:keys [name country_code]}]
  {:db/ident   (->state-kw country_code)
   :state/name name})

(let [def-file  (scs/scs-file :ats "def.scs")
      countries (scs/scs->text-sii def-file "def/country.sii")]
  (def ^:private tx-states
    (mapv tx-country countries))

  (def ^:private sii-countries-to-states
    (->> (for [{code                        :country_code
                [_country _data country-id] :sii/block-id} countries]
           [country-id (->state-kw code)])
         (into {}))))

(defn- dlc-files [dlc-name]
  (let [dlc-name (files/strip-extension dlc-name)]
    {:scs     (str dlc-name ".scs")
     :company (str "def/company." dlc-name ".sii")
     :city    (str "def/city."    dlc-name ".sii")
     :cargo   (str "def/cargo."   dlc-name ".sii")}))

(defn- tx-city [{:keys [city_name]
                 [state]      :country
                 [_city slug] :sii/block-id}]
  {:city/ident slug
   :city/name  city_name
   :city/state (sii-countries-to-states state)})

(defn- tx-company [{:keys [name]
                    [_company _permanent slug] :sii/block-id}]
  {:company/ident slug
   :company/name  name})

(def ^:private adr-classes
  {"1" :cargo.adr/explosive
   "2" :cargo.adr/gases
   "3" :cargo.adr/flammable-liquids
   "4" :cargo.adr/flammable-solids
   "6" :cargo.adr/poison
   "8" :cargo.adr/corrosive})

(def ^:private locale
  (future (scs/locale :ats)))

(defn- i18n [str-with-slugs]
  (let [loc (deref locale)]
    (loop [s str-with-slugs]
      (let [after (str/replace s #"@@([^@]+)@@" (fn [[_match inner]]
                                                  (get loc inner (str "!!" inner "!!"))))]
        (if (= s after)
          s
          (recur after))))))

;; The heavy cargoes don't have a clear signal in the SCS files, so here's
;; a hard-coded lists of the ones that count for the Heavy Cargo Pack.
(def ^:private heavy-cargoes
  #{"asph_miller"
    "coil"
    "cott_harvest"
    "dozer"
    "lift_truck"
    "lift_truck_s"
    "mobile_crane"
    "scraper"
    "tractor_c"
    "transformer"})

(defn- tx-cargo [{:keys [adr_class name]
                  [_cargo slug] :sii/block-id}]
  (merge {:cargo/ident slug
          :cargo/name  (i18n name)}
         (when-let [adr (not-empty (into #{} (map adr-classes) adr_class))]
           {:cargo/adr adr})
         (when (heavy-cargoes slug)
           {:cargo/heavy? true})))

(defn- read-game-file [{:keys [cargo city company scs]}]
  (when-let [file (scs/scs-file :ats scs)]
    {:cities    (into [] (comp (filter #(= (:type %) "city_data"))
                               (map tx-city))
                      (scs/scs->text-sii file city))
     :companies (into [] (comp (filter #(= (:type %) "company_permanent"))
                               (map tx-company))
                      (scs/scs->text-sii file company))
     :cargo     (into [] (map tx-cargo) (scs/scs->text-sii file cargo))}))

(def ^:private core-files
  {:scs     "def.scs"
   :cargo   "def/cargo.sii"
   :city    "def/city.sii"
   :company "def/company.sii"})

(defn- read-game-files []
  (->> (files/scs-files :ats)
       (remove #{"def.scs"})
       (map dlc-files)
       (cons core-files)
       (mapv read-game-file)))

(def ^:private tx-game-files
  (delay
    (let [m (reduce (partial merge-with concat) {} (read-game-files))]
      (into [] cat [(:cities m) (:companies m) (:cargo m)]))))

(def ^:private tx-missing-companies
  [;; Lots of Wallbert locations were removed in 1.53.
   {:company/ident "wal_food_mkt", :company/name "Wallbert"}
   {:company/ident "wal_food_whs", :company/name "Wallbert"}
   {:company/ident "wal_mkt",      :company/name "Wallbert"}
   {:company/ident "wal_whs",      :company/name "Wallbert"}
   ;; Several Darchelle Uzau locations in California were removed.
   {:company/ident "du_farm",      :company/name "Darchelle Uzau"}
   ;; Charged hasn't been updated(?) but still didn't get picked up.
   {:company/ident "cha_el_mkt",   :company/name "Charged"}
   {:company/ident "cha_el_whs",   :company/name "Charged"}])

(def initial-data
  "Initial data for the ATS map, companies, etc."
  (future (into [] cat [tx-states tx-missing-companies @tx-game-files])))

(def ^:private city-renames
  {"san_rafael" "oakland"
   "oakdale"    "modesto"
   "hornbrook"  "hilt"})

(defn canonical-city-name [city-ident]
  (get city-renames city-ident city-ident))
