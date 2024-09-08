(ns ets.jobs.database.schema)

(def ^:private core-schema
 [{:db/ident        :sii/block-id
   :db/valueType    :db.type/long
   :db/cardinality  :db.cardinality/one
   :db/unique       :db.unique/identity}])

(def ^:private state-schema
 [{:db/ident        :state/name
   :db/valueType    :db.type/string
   :db/cardinality  :db.cardinality/one}])

(def ^:private city-schema
 [{:db/ident        :city/ident
   :db/valueType    :db.type/string
   :db/cardinality  :db.cardinality/one
   :db/unique       :db.unique/identity
   :db/doc          "City's machine-friendly slug"}
  {:db/ident        :city/name
   :db/valueType    :db.type/string
   :db/cardinality  :db.cardinality/one
   :db/doc          "City's human-friendly name"}
  {:db/ident        :city/state
   :db/valueType    :db.type/ref
   :db/cardinality  :db.cardinality/one}])

(def ^:private company-schema
 [{:db/ident        :company/ident
   :db/valueType    :db.type/string
   :db/cardinality  :db.cardinality/one
   :db/unique       :db.unique/identity
   :db/doc          "Company's machine-friendly slug"}
  {:db/ident        :company/name
   :db/valueType    :db.type/string
   :db/cardinality  :db.cardinality/one
   :db/doc          "Company's human-friendly name"}])

(def ^:private location-schema
 "A Location is a company + city pair, more or less."
 [{:db/ident        :location/company
   :db/valueType    :db.type/ref
   :db/cardinality  :db.cardinality/one
   :db/doc          "The company which owns this location."}
  {:db/ident        :location/city
   :db/valueType    :db.type/ref
   :db/cardinality  :db.cardinality/one
   :db/doc          "The city where this location is found."}
  {:db/ident        :location/pair
   :db/valueType    :db.type/tuple
   :db/tupleAttrs   [:location/company :location/city]
   :db/cardinality  :db.cardinality/one
   :db/unique       :db.unique/identity
   :db/doc          (str "Company + City pairs are unique. "
                         "(Actually the game sometimes duplicates them, but it "
                         " uses an adjacent company name.)")}])

(def ^:private cargo-schema
 [{:db/ident        :cargo/ident
   :db/valueType    :db.type/string
   :db/cardinality  :db.cardinality/one
   :db/unique       :db.unique/identity
   :db/doc          "Cargo's machine-friendly slug"}
  {:db/ident        :cargo/name
   :db/valueType    :db.type/string
   :db/cardinality  :db.cardinality/one
   :db/doc          "Cargo's human-friendly name"}
  {:db/ident        :cargo/adr
   :db/valueType    :db.type/ref
   :db/cardinality  :db.cardinality/many
   :db/doc          "Cargo ADR tags"}
  {:db/ident        :cargo/heavy?
   :db/valueType    :db.type/boolean
   :db/cardinality  :db.cardinality/one
   :db/doc          "Heavy cargo flag"}
  
  {:db/ident        :cargo.adr/corrosive}
  {:db/ident        :cargo.adr/explosive}
  {:db/ident        :cargo.adr/flammable-liquids}
  {:db/ident        :cargo.adr/flammable-solids}
  {:db/ident        :cargo.adr/gases}
  {:db/ident        :cargo.adr/poison}])

 ;; "Offers" are pending jobs, "deliveries" are completed jobs
 ;; "Job" is used for common attributes (most of them) so take care in querying
 ;; to only select the right kind.
 ;; Offers have expiration-time; jobs have end-time, profit, XP.
(def jobs-schema
 [;; First some pieces common to job offers and deliveries.
  {:db/ident        :job/cargo
   :db/valueType    :db.type/ref
   :db/cardinality  :db.cardinality/one
   :db/doc          "Ref to an entity with :cargo/ident."}
  {:db/ident        :job/source
   :db/valueType    :db.type/ref
   :db/cardinality  :db.cardinality/one
   :db/doc          "Ref to a :location/* entity."}
  {:db/ident        :job/target
   :db/valueType    :db.type/ref
   :db/cardinality  :db.cardinality/one
   :db/doc          "Ref to a :location/* entity."}
  {:db/ident        :job/urgency
   :db/valueType    :db.type/long
   :db/cardinality  :db.cardinality/one
   :db/doc          "1, 2 or 3. Note that the raw data is 0 to 2 for offers, 1 to 3 for deliveries!"}
  {:db/ident        :job/distance-km
   :db/valueType    :db.type/long
   :db/cardinality  :db.cardinality/one
   ;; TODO: Are achievements with min distances reckoned by the planned distance or that driven?
   ;; Like can you take a 990-mile job, drive out of your way, and boom it's magically a 1000-mile job?
   :db/doc          "For offers, the shortest distance. For deliveries, the distance driven."}

  {:db/ident        :job/type
   :db/valueType    :db.type/ref
   :db/cardinality  :db.cardinality/one
   :db/doc          ":job.type/freerm, quick, compn, on_compn, or spec_oversize"}
  {:db/ident        :job.type/freerm}
  {:db/ident        :job.type/quick}
  {:db/ident        :job.type/compn}
  {:db/ident        :job.type/on_compn}
  {:db/ident        :job.type/spec_oversize}

  ;; Special Transport jobs ==================================================
  ;; - There are standardized *routes*, which link two cities and have a name
  ;;   (`:route.special/name`) like "src_dst1".
  ;; - Each route has one or more *templates*, which link a route to a cargo.
  ;; - Each *template* may or may not be available as a *special job* offer,
  ;;   which links a template to an expiration time.
  
  ;; Special job offers get their cargo from the template:
  ;; (-> % :offer.special/template :template.special/cargo)
  ;; Special job deliveries get their cargo from the usual :job/cargo.
  {:db/ident        :job.special/route
   :db/valueType    :db.type/ref
   :db/cardinality  :db.cardinality/one
   :db/doc          "Ref to the unique index for the special route."}
  {:db/ident        :offer.special/template
   :db/valueType    :db.type/ref
   :db/cardinality  :db.cardinality/one
   :db/doc          "Ref to the unique index for the special job template."}
  
  ;; Special Transport routes are a separate table. These are indexed by the
  ;; :route.special/name, a string slug like "src_dst1".
  {:db/ident        :route.special/name
   :db/valueType    :db.type/string
   :db/cardinality  :db.cardinality/one
   :db/unique       :db.unique/identity
   :db/doc          "Game-defined slug for a given special route."}
  {:db/ident        :route.special/source-city
   :db/valueType    :db.type/ref
   :db/cardinality  :db.cardinality/one}
  {:db/ident        :route.special/target-city
   :db/valueType    :db.type/ref
   :db/cardinality  :db.cardinality/one}

  ;; Special job offers *templates* are in a separate table, and referenced by
  ;; name rather than giving their details directly.
  ;; :offer.special/template points to these entities.
  {:db/ident        :template.special/name
   :db/valueType    :db.type/string
   :db/cardinality  :db.cardinality/one
   :db/unique       :db.unique/identity
   :db/doc          "Game-defined slug for a special transport job offer *template*."}
  {:db/ident        :template.special/cargo
   :db/valueType    :db.type/ref
   :db/cardinality  :db.cardinality/one
   :db/doc          "Ref to the cargo carried by this special job template."}
  {:db/ident        :template.special/route
   :db/valueType    :db.type/ref
   :db/cardinality  :db.cardinality/one
   :db/doc          "Ref to the route for this special transport job offer template."}

  ;; Specific to job offers
  {:db/ident        :offer/expiration-time
   :db/valueType    :db.type/long
   :db/cardinality  :db.cardinality/one
   :db/doc          "Game time after which this job can no longer be started, I think."}

  ;; Specific to completed deliveries
  {:db/ident        :delivery/start-time
   :db/valueType    :db.type/long
   :db/cardinality  :db.cardinality/one
   :db/doc          "Game time when this job was accepted"}
  {:db/ident        :delivery/end-time
   :db/valueType    :db.type/long
   :db/cardinality  :db.cardinality/one
   :db/doc          "Game time when this job was completed"}
  {:db/ident        :delivery/remaining-time
   :db/valueType    :db.type/long
   :db/cardinality  :db.cardinality/one
   :db/doc          "Minutes to go before the deadline at delivery. Negative when late!"}
  {:db/ident        :delivery/xp
   :db/valueType    :db.type/long
   :db/cardinality  :db.cardinality/one
   :db/doc          "XP gained"}
  {:db/ident        :delivery/profit
   :db/valueType    :db.type/long
   :db/cardinality  :db.cardinality/one
   :db/doc          "Profit in dollars"}
  {:db/ident        :delivery/index
   :db/valueType    :db.type/long
   :db/cardinality  :db.cardinality/one
   :db/doc          "0-based delivery number"}
  {:db/ident        :delivery/damage-ratio
   :db/valueType    :db.type/double
   :db/cardinality  :db.cardinality/one
   :db/doc          "Damage ratio 0-1: 0.000 for no damage"}
  {:db/ident        :delivery/parking-difficulty
   :db/valueType    :db.type/long
   :db/cardinality  :db.cardinality/one
   :db/doc          "0 for auto, 1 for easy, 2 for normal, 3 for hard"}
  {:db/ident        :delivery/company-truck?
   :db/valueType    :db.type/boolean
   :db/cardinality  :db.cardinality/one
   :db/doc          "True when the truck was provided, ie. a Quick Job."}
  {:db/ident        :delivery/fines
   :db/valueType    :db.type/long
   :db/cardinality  :db.cardinality/one
   :db/doc          "Fines levied during this job."}
  {:db/ident        :delivery/cargo-mass-kg
   :db/valueType    :db.type/double
   :db/cardinality  :db.cardinality/one
   :db/doc          "Mass of the cargo in kg."}])

  ;; TODO: ADR and trailer types are needed in ETS2's achievements, but not in ATS?

(def schema
 (into [] cat [core-schema    state-schema city-schema
               company-schema cargo-schema location-schema
               jobs-schema]))
