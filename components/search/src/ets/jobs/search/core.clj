(ns ets.jobs.search.core
  (:require
    [clojure.java.io :as io]
    [clojure.string :as str]
    [ets.jobs.ats.interface :as ats]
    [ets.jobs.decrypt.interface :as decrypt]
    [ets.jobs.ets2.interface :as ets2]
    [ets.jobs.files.interface :as files]
    [ets.jobs.sii.interface :as sii])
  (:import
    [java.io File]))

(defn sii-struct [s name]
  (first (filter #(= name (:name %)) (vals (:structures s)))))

(defn by-type [s typ]
  (for [[_ d] (:data s)
        :when (= typ (:type d))]
    d))

(defn by-id [s id]
  (get-in s [:data id]))

(defn economy [s]
  (let [typ (sii-struct s "economy")]
    (first (by-type s (:id typ)))))

(defn current-time [s]
  (get (economy s) "game_time"))

(def day-length  (* 60 24))
(def week-length (* day-length 7))

(def days ["Mon" "Tue" "Wed" "Thu" "Fri" "Sat" "Sun"])

(defn- time-breakdown [epoch-mins]
  (let [in-week (mod epoch-mins week-length)
        in-day  (mod epoch-mins day-length)]
    {:week (inc (quot epoch-mins week-length))
     :day  (nth days (quot in-week day-length))
     :hour (quot in-day 60)
     :mins (mod epoch-mins 60)}))

(defn time-zone-names [raw]
  (let [[_ tz] (re-matches #"@@tz_(\w+)@@" raw)]
    (.toUpperCase tz)))

(defn local-time
  "Returns a more subjective time in the user's current local time zone.
  0 is week 1, Monday, 00:00Z.
  Returns {:cest {:week 12 :day 'Wednesday' :hour 0 :min 12}
           :local {...}
           :tz    'CEST'}.
  Note that the base time is, I think, CEST (Germany time) and not UTC!
  But the time zone is given relative to UTC, but CEST is UTC+2."
  [s]
  (let [now      (current-time s) ; CEST minutes from game epoch.
        eco      (economy s)
        tz-delta (- (get eco "time_zone") 120)]
    {:cest  (time-breakdown now)
     :local (time-breakdown (+ now tz-delta))
     :tz    (time-zone-names (get eco "time_zone_name"))}))


(defn all-jobs [s]
  (let [now (current-time s)]
    (for [[_ _ sender-company sender-city :as cid] (get (economy s) "companies")
          :let [c (by-id s cid)]
          oid (get c "job_offer")
          :let [o    (by-id s oid)
                exp  (get o "expiration_time")]
          :when (and (pos? exp)
                     (> exp now))]
      (let [[target-company target-city] (str/split (get o "target") #"\.")]
        {:origin          sender-city
         :sender          sender-company
         :recipient       target-company
         :destination     target-city
         :cargo           (get-in o ["cargo" 1])
         :expires-in-mins (- (get o "expiration_time") now)
         :distance        (get o "shortest_distance_km")
         :urgency         (get o "urgency")}))))


(def game-meta
  {:ets2 ets2/ets-meta
   :ats  ats/ats-meta})


(defn jobs-for [jobs {:keys [pred]}]
  (->> jobs
       (filter pred)
       (into #{})
       seq))

(defn achievable-jobs [game s]
  (let [jobs (all-jobs s)
        {:keys [regions open]} (game-meta game)]
    (into {}
          (for [[_ {:keys [achievements]}] regions
                ach                        achievements
                :let     [ak (:key ach)]
                :when    (open ak)]
            [ak (jobs-for jobs ach)]))))

(defn profile-info [dir]
  (let [basics  (->> (File. dir "profile.sii")
                     decrypt/decode
                     sii/parse-profile-basics)]
    (assoc basics :dir dir)))

(defn profiles [dir]
  (for [p (.listFiles dir)]
    (profile-info p)))

(defn parse-latest [profile]
  (->> profile
       (io/file)
       files/latest-save
       (#(do (prn %) %)) ; Dumps the filename.
       decrypt/decode
       sii/parse-sii))

(comment

  (def prof (->> "samples/ets2/426973686F70/profile.sii"
                 decrypt/decode
                 sii/parse-profile-basics
                 ))
  (def profile-dir (->> (profiles (clojure.java.io/file "samples/ets2"))
                        (filter #(= "Bishop2" (:name %)))
                        first
                        :dir))

  (def p (->> (File. (File. (File. profile-dir "save") "1") "game.sii")
              decrypt/decode
              sii/parse-sii))

  (def ap-dir (->> (profiles (clojure.java.io/file "samples/ats"))
                   (filter #(= "Braden" (:name %)))
                   first
                   :dir))
  (def ap (->> (File. (File. (File. ap-dir "save") "1") "game.sii")
               decrypt/decode
               sii/parse-sii))


  (identity p)
  (take 4 (:data p))

  (keys p)
  (:structures ap)
  (select-keys (economy p) ["game_time" "time_zone" "time_zone_name"])
  (all-jobs ap)

  (get-in p [:structures 14])
  (sii-struct p "job_offer")
  ;now: 16425752
  (all-jobs p)
  (achievable-jobs :ets2 p)
  (achievable-jobs :ats  ap)
  (by-type p 1)

  (def cargos (into #{} (map :cargo (all-jobs p))))
  (identity cargos)

  (require 'clojure.set)
  (def companies
    (clojure.set/union (into #{} (map :sender    (all-jobs p)))
                       (into #{} (map :recipient (all-jobs p)))))
  (def cities
    (clojure.set/union (into #{} (map :origin (all-jobs p)))
                       (into #{} (map :destination (all-jobs p)))))

  (identity companies)
  (identity cities)

  (current-time p)

  (get-in p [:structures 20])

  )
