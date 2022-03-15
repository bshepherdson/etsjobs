(ns ets.jobs.database.schema)

(def schema
  ; Profiles
  [{:db/ident       :profile/id
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one
    :db/unique      :db.unique/identity}
   {:db/ident       :profile/name
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident       :profile/game
    :db/valueType   :db.type/keyword
    :db/cardinality :db.cardinality/one}

   ; Achievement progress
   {:db/ident       :progress/profile
    :db/valueType   :db.type/ref
    :db/cardinality :db.cardinality/one}
   {:db/ident       :progress/achievement
    :db/valueType   :db.type/keyword
    :db/cardinality :db.cardinality/one}
   ; Used by all achievements for speed, but vital to the one-shots.
   {:db/ident       :progress/complete?
    :db/valueType   :db.type/boolean
    :db/cardinality :db.cardinality/one}
   ; Used by eg. "Make N deliveries from these places"
   {:db/ident       :progress/count
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one}
   ; Used by "Deliver to each of these" types.
   {:db/ident       :progress/location
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/many}

   ; Bonus entity for Industry Standard and its double deliveries.
   {:db/ident       :standard/progress
    :db/valueType   :db.type/ref
    :db/cardinality :db.cardinality/one}
   {:db/ident       :standard/location
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident       :standard/count
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one}])

