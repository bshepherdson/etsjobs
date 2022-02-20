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
    :db/valueType   :db.type/ref
    :db/cardinality :db.cardinality/one}

   ; Game maps
   {:db/ident       :map/usa}
   {:db/ident       :map/europe}

   ; Achievement progress
   {:db/ident       :progress/profile
    :db/valueType   :db.type/ref
    :db/cardinality :db.cardinality/one}
   {:db/ident       :progress/achievement
    :db/valueType   :db.type/ref
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
    :db/cardinality :db.cardinality/one}


   ; Achievement IDs
   ; ETS2
   {:db/ident :along-the-black-sea}
   {:db/ident :captain}
   {:db/ident :cattle-drive}
   {:db/ident :check-in-check-out}
   {:db/ident :concrete-jungle}
   {:db/ident :exclave-transit}
   {:db/ident :fleet-builder}
   {:db/ident :gas-must-flow}
   {:db/ident :go-nuclear}
   {:db/ident :iberian-pilgrimage}
   {:db/ident :industry-standard}
   {:db/ident :lets-get-shipping}
   {:db/ident :like-a-farmer}
   {:db/ident :michaelangelo}
   {:db/ident :miner}
   {:db/ident :orient-express}
   {:db/ident :sailor}
   {:db/ident :scania-trucks-lover}
   {:db/ident :taste-the-sun}
   {:db/ident :turkish-delight}
   {:db/ident :volvo-trucks-lover}
   {:db/ident :whatever-floats-your-boat}

   ; ATS
   {:db/ident :along-the-snake-river}
   {:db/ident :big-boy}
   {:db/ident :buffalo-bill}
   {:db/ident :cabbage-to-cabbage}
   {:db/ident :cheers}
   {:db/ident :energy-from-above}
   {:db/ident :gold-fever}
   {:db/ident :gold-rush}
   {:db/ident :grown-in-idaho}
   {:db/ident :keep-sailing}
   {:db/ident :lumberjack}
   {:db/ident :over-the-top}
   {:db/ident :pump-it-up}
   {:db/ident :sea-dog}
   {:db/ident :sky-delivery}
   {:db/ident :sky-harbor}
   {:db/ident :some-like-it-salty}
   {:db/ident :steel-wings}
   {:db/ident :terminal-terminus}
   {:db/ident :this-one-is-mine}
   {:db/ident :up-and-away}
   ])
