(ns ets.jobs.ats.oversize
  (:require
   [ets.jobs.scs.interface :as scs]))

(defn- read-file []
  (scs/scs-file :ats "dlc_oversize.scs"))

(defn- tx-oversize-job-spec
  [{[_cargo cargo-slug]      :cargo
    [_route-data route-slug] :route
    [_spec-offer offer-id]   :sii/block-id}]
  {:template.special/name  offer-id
   :template.special/cargo {:cargo/ident cargo-slug}
   :template.special/route [:route.special/name route-slug]})

(defn- tx-oversize-job-specs [scs-file]
  (into [] (map tx-oversize-job-spec)
        (scs/scs->text-sii scs-file "def/oversize_offer_data.sii")))

(defn- tx-oversize-route
  [{[_ route-id] :sii/block-id
    [from-slug]  :from_city
    [to-slug]    :to_city}]
  {:route.special/name        route-id
   :route.special/source-city {:city/ident from-slug}
   :route.special/target-city {:city/ident to-slug}})

(defn- tx-oversize-routes [scs-file]
  (into [] (map tx-oversize-route)
        (scs/scs->text-sii scs-file "def/route.sii")))

(defn- build-tx-oversize []
  (let [scs (read-file)]
    (into [] cat [(tx-oversize-routes scs)
                  (tx-oversize-job-specs scs)])))

(def tx-oversize (delay (build-tx-oversize)))
