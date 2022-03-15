(ns ets.jobs.api-server.core
  (:require
    [ets.jobs.api-server.routes :as routes]
    [ets.jobs.database.interface :as database]
    [integrant.core :as ig]
    [io.pedestal.http :as http]
    [datahike.api :as d]))

(def base-config
  {:db/config {:path "etsjobs"}
   :db/conn   {:cfg  (ig/ref :db/config)}
   :http/server {:conn  (ig/ref :db/conn)
                 :port  8890
                 :join? false}})

(def config (ig/prep base-config))

(defmethod ig/init-key :db/config [_ {:keys [path]}]
  (database/create path))

(defmethod ig/init-key :db/conn [_ {:keys [cfg]}]
  (database/open cfg))

(defmethod ig/halt-key! :db/conn [_ conn]
  (database/close conn))


(defmethod ig/init-key :http/server [_ {:keys [conn port join?]}]
  (-> {::http/routes (routes/routes conn)
       ::http/type   :jetty
       ::http/join?  join?
       ::http/port   port}
      http/create-server
      http/start))

(defmethod ig/halt-key! :http/server [_ server]
  (http/stop server))

(defonce system (atom nil))

(defn restart []
  (when @system
    (ig/halt! @system))
  (reset! system (ig/init config)))

(comment
  (restart)
  (require '[datahike.api :as d])
  (let [conn (:db/conn (deref system))
        db   @conn]
    (d/pull db '[*] [:profile/id "426973686F7032"])
    )

  ; Capturing my current state for a few different achievements.
  (d/transact (:db/conn @system)
              [])
  (d/transact (:db/conn @system)
              (concat
                (map #(assoc % :progress/profile [:profile/id "426973686F7032"])
                     [{:progress/achievement :concrete-jungle
                       :progress/count       7}
                      {:progress/achievement :exclave-transit
                       :progress/count       1}

                      {:progress/achievement :like-a-farmer
                       :progress/location
                       ["utena/agrominta"
                        "utena/agrominta_a"
                        "daugavpils/eviksi"
                        "liepaja/eviksi"
                        "liepaja/eviksi_a"
                        "riga/eviksi"
                        "valmiera/eviksi"
                        "ventspils/eviksi"
                        "helsinki/egres"
                        "kouvola/egres"
                        "narva/onnelik"
                        "parnu/onnelik"
                        "parnu/onnelik_a"
                        "tartu/onnelik"
                        "kaliningrad/zelenye"
                        "petersburg/zelenye"]}

                      {:db/id "industry-standard"
                       :progress/achievement :industry-standard}])

                [; Industry Standard is special
                 {:standard/progress "industry-standard"
                  :standard/location "riga/lvr"
                  :standard/count    1}
                 {:standard/progress "industry-standard"
                  :standard/location "daugavpils/lvr"
                  :standard/count    2}
                 {:standard/progress "industry-standard"
                  :standard/location "tartu/renat"
                  :standard/count    2}
                 ; Helsinki has 0, so omitted.
                 {:standard/progress "industry-standard"
                  :standard/location "daugavpils/renat"
                  :standard/count    1}
                 {:standard/progress "industry-standard"
                  :standard/location "rezekne/renat"
                  :standard/count    1}
                 {:standard/progress "industry-standard"
                  :standard/location "riga/renat"
                  :standard/count    1}
                 {:standard/progress "industry-standard"
                  :standard/location "siauliai/renat"
                  :standard/count    1}
                 {:standard/progress "industry-standard"
                  :standard/location "kunda/ee_paper"
                  :standard/count    2}
                 ; Kouvola is also 0
                 {:standard/progress "industry-standard"
                  :standard/location "tampere/viljo_paper"
                  :standard/count    2}
                 {:standard/progress "industry-standard"
                  :standard/location "vilnius/viln_paper"
                  :standard/count    2}]))
  )

