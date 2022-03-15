(ns ets.jobs.api-server.routes
  (:require
    [ets.jobs.database.interface :as db]
    [io.pedestal.http.body-params :as body-params]
    [io.pedestal.http.route :as route]))

(defn db-injector [conn]
  {:name  :db-injector
   :enter (fn [ctx] (assoc ctx :db @conn))
   :leave
   (fn [{:keys [tx-data] :as ctx}]
     (when tx-data
       (prn "transacting" tx-data)
       (db/transact conn tx-data))
     ctx)})

(def respond-hello
  {:name :greet
   :enter
   (fn [ctx]
     (assoc ctx :response {:status 200 :body "Hello, World"}))})

(defn response
  ([status ctx] (assoc ctx :response {:status status}))
  ([status ctx body] (assoc ctx :response {:status status :body body})))

(defn ok [& args] (apply response 200 args))
(defn created [& args] (apply response 201 args))

(def put-profile
  "Idempotent creation of the achievement state for a specific profile.
  Expects a body parameter of EDN-formatted profile resembling the schema:
  {:profile/id ... :profile/name ... :profile/game :ats}"
  {:name ::put-profile
   :enter
   (fn [ctx]
     (let [input    (select-keys (get-in ctx [:request :edn-params])
                                 [:profile/id :profile/name :profile/game])]
       (-> ctx
           (assoc :tx-data [input])
           created)))})

(def get-profile
  "Reads a profile by ID, with full details of its achievement progress."
  {:name ::get-profile
   :enter
   (fn [ctx]
     (let [id      (get-in ctx [:request :path-params :id])
           profile (db/fetch-profile (:db ctx) id)]
       (ok ctx profile)))})

(defn routes [conn]
  (let [dbi (db-injector conn)]
    (route/expand-routes
      #{["/greet"   :get [respond-hello]]
        ["/profile" :put [dbi (body-params/body-params) put-profile]]
        ["/profile/:id" :get [dbi get-profile]]
        })))

