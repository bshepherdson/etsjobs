(ns ets.jobs.sii.text
  (:require
   [instaparse.core :as insta]
   #_[parka.api :as p])
  (:import
   [java.time Instant]))

(def ^:private sii-parser
  (insta/parser
    "File          = <'SiiNunit'> <LB> Defs <RB>
    LB             = <ws> '{' <ws>
    RB             = <ws> '}' <ws>
    LP             = <wsline> '(' <wsline>
    RP             = <wsline> ')' <wsline>
    COMMA          = <wsline> ',' <wsline>
    SEMI           = <wsline> ';' <wsline>
    <wsline>       = (#'[ \\t\\r]*' | BlockComment)*
    <ws>           = (<wsline> Comment? <'\n'>)*
    eol            = <wsline> Comment? <'\n'> <ws>
    <BlockComment> = <'/*'> ((!'*/') #'.')* <'*/'>
    <Comment>      = (<'#'> | <'//'>) <#'[^\\n]*'>

    Defs           = Unit*
    Unit           = Header <LB> Attributes <RB>
    Header         = TOKEN <ws> <':'> <ws> LocalPointer
    TOKEN          = #'[a-z_]+'
    LocalPointer   = <'.'> Pointer
    Pointer        = TOKEN (<'.'> TOKEN)*

    Attributes     = Attribute (<eol> Attribute)*
    Attribute      = TOKEN <wsline> <':'> <wsline> Value
    <Value>        = String | Float | Placement | Floats | Signed | Unsigned | Fixeds | Bool | LocalPointer | Pointer

    String         = <'\"'> #'[^\"]*' <'\"'>
    Float          = #'-?\\d+\\.\\d+'
    Floats         = <LP> Float (<COMMA> Float)+ <RP>
    Placement      = Fixeds <wsline> PlaceRight
    PlaceRight     = <LP> Unsigned <SEMI> Unsigned <COMMA> Unsigned <COMMA> Unsigned <COMMA> Unsigned <RP>
    Signed         = '-'? Unsigned
    Unsigned       = #'\\d+'
    Fixeds         = <LP> Signed (<COMMA> Signed)+ <RP>
    Bool           = 'true' | 'false'"))

(comment
  (def data-dir (clojure.java.io/file "/Users/braden/"))
  (slurp)
  )

(defn parse-profile-basics
  "Expects a ByteBuffer for the parsed profile.sii. Uses a hacky regex
  to winkle out some useful bits."
  [buf]
  (let [in (String. (.array buf))
        [_ name]     (re-find #"profile_name: (.*)" in)
        [_ map-path] (re-find #"map_path: \"([^\"]*)\"" in)
        [_ saved-at] (re-find #"save_time: (\d+)"   in)]
    {:profile/name     name
     :profile/map      (case map-path
                         "/map/europe.mbd" :map/europe
                         "/map/usa.mbd"    :map/usa
                         map-path)
     :profile/saved-at (Instant/ofEpochSecond (Integer/parseInt saved-at))}))
