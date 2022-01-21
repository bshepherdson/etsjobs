(ns ets.jobs.sii-file-text
  (:import
    [java.time Instant]))

(defn parse-profile-basics
  "Expects a ByteBuffer for the parsed profile.sii. Uses a hacky regex
  to winkle out some useful bits."
  [buf]
  (let [in (String. (.array buf))
        [_ name]     (re-find #"profile_name: (.*)" in)
        [_ saved-at] (re-find #"save_time: (\d+)"   in)]
    {:name name
     :saved-at (Instant/ofEpochSecond (Integer/parseInt saved-at))}))
