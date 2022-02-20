(ns ets.jobs.sii.text
  (:import
    [java.time Instant]))

(defn parse-profile-basics
  "Expects a ByteBuffer for the parsed profile.sii. Uses a hacky regex
  to winkle out some useful bits."
  [buf]
  (let [in (String. (.array buf))
        [_ name]     (re-find #"profile_name: (.*)" in)
        [_ map-path] (re-find #"map_path: \"([^\"]*)\"" in)
        [_ saved-at] (re-find #"save_time: (\d+)"   in)]
    {:name     name
     :map      (case map-path
                 "/map/europe.mbd" :map/europe
                 "/map/usa.mbd"    :map/usa
                 map-path)
     :saved-at (Instant/ofEpochSecond (Integer/parseInt saved-at))}))
