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
(defn deleted [ctx] (response 204 ctx))

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
           profile (db/fetch-profile (:db ctx) id)
           ]
       (ok ctx profile)))})

(def put-industry-standard
  {:name ::put-industry-standard
   :enter
   (fn [ctx]
     (let [id       (get-in ctx [:request :path-params :id])
           body     (select-keys (get-in ctx [:request :edn-params])
                                 [:standard/location :standard/count])]
       (-> ctx
           (assoc :tx-data (db/update-industry-standard (:db ctx) id body))
           (ok))))})

(def put-location-list
  {:name ::put-location-list
   :enter
   (fn [ctx]
     (let [id       (get-in ctx [:request :path-params :id])
           {:keys [achievement location]}
           (select-keys (get-in ctx [:request :edn-params])
                        [:achievement :location])]
       (-> ctx
           (assoc :tx-data (db/put-location (:db ctx) id
                                            achievement location))
           (ok))))})

(def delete-location-list
  {:name ::delete-location-list
   :enter
   (fn [ctx]
     (let [id       (get-in ctx [:request :path-params :id])
           {:keys [achievement location]}
           (select-keys (get-in ctx [:request :edn-params])
                        [:achievement :location])]
       (-> ctx
           (assoc :tx-data (db/delete-location (:db ctx) id
                                               achievement location))
           (deleted))))})

(def put-counted
  {:name ::put-counted
   :enter
   (fn [ctx]
     (let [id (get-in ctx [:request :path-params :id])
           body (select-keys (get-in ctx [:request :edn-params])
                             [:achievement :count])]
       (-> ctx
          (assoc :tx-data (db/update-count (:db ctx) id
                                           (:achievement body) (:count body)))
          (ok))))})


(defn- boolean-toggle [name completed?]
  {:name name
   :enter
   (fn [ctx]
     (let [id   (get-in ctx [:request :path-params :id])
           body (get-in ctx [:request :edn-params])]
       (-> ctx
           (assoc :tx-data (db/update-completion
                             (:db ctx) id (:achievement body) completed?))
           (ok))))})

(def put-completed
  (boolean-toggle ::put-completed true))
(def delete-completed
  (boolean-toggle ::delete-completed false))

(defn routes [conn]
  (let [dbi (db-injector conn)]
    (route/expand-routes
      #{["/greet"   :get [respond-hello]]
        ["/profile" :put [dbi (body-params/body-params) put-profile]]
        ["/profile/:id" :get [dbi get-profile]]
        ["/profile/:id/standard" :put
         [dbi (body-params/body-params) put-industry-standard]]
        ["/profile/:id/loclist" :put
         [dbi (body-params/body-params) put-location-list]]
        ["/profile/:id/loclist" :delete
         [dbi (body-params/body-params) delete-location-list]]
        ["/profile/:id/counted" :put
         [dbi (body-params/body-params) put-counted]]
        ["/profile/:id/completed" :put
         [dbi (body-params/body-params) put-completed]]
        ["/profile/:id/completed" :delete
         [dbi (body-params/body-params) delete-completed]]})))

