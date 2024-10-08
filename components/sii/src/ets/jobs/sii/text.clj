(ns ets.jobs.sii.text
  (:require
   [clojure.string :as str]
   [instaparse.core :as insta])
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
    eol1           = <wsline> Comment? <'\\n'>
    <ws>           = <wsline> eol1* <wsline>
    <wsline>       = (#'[ \\t\\r]*' | BlockComment)*
    eol            = eol1 <ws>
    <BlockComment> = <'/*'> ((!'*/') (#'.' | '\\n'))* <'*/'>
    <Comment>      = (<'#'> | <'//'>) <#'[^\\n]*'>

    Defs           = Unit*
    Unit           = Header <LB> Attributes <RB>
    Header         = TOKEN <ws> <':'> <ws> (LocalPointer | Pointer)
    TOKEN          = #'[a-z0-9_]+'
    LocalPointer   = <'.'> Pointer
    Pointer        = TOKEN (<'.'> TOKEN)*

    Attributes     = Attribute (<eol> Attribute)*
    Attribute      = TOKEN ArrayElement? <wsline> <':'> <wsline> Value
    ArrayElement   = <'[]'>
    <Value>        = String | Float | Placement | Floats | Signed | Unsigned | Fixeds | Bool | LocalPointer | Pointer

    String         = <'\"'> Char*  <'\"'>
    <Char>         = Escaped | #'[^\"\\\\]*'
    Escaped        = #'\\\\.'
    Float          = #'-?\\d+\\.\\d+'
    <Number>       = Float | Signed
    Floats         = <LP> Number (<COMMA> Number)+ <RP>
    Placement      = Fixeds <wsline> PlaceRight
    PlaceRight     = <LP> Unsigned <SEMI> Unsigned <COMMA> Unsigned <COMMA> Unsigned <COMMA> Unsigned <RP>
    Signed         = '-'? Unsigned
    Unsigned       = #'\\d+'
    Fixeds         = <LP> Signed (<COMMA> Signed)+ <RP>
    Bool           = 'true' | 'false'"))

(defmulti ^:private ->clojure
  (fn [x]
    (cond
      (vector? x) (first x)
      (string? x) ::string
      (number? x) ::number
      :else (throw (ex-info "->clojure got an unexpected value" {:expr x})))))

(defmethod ->clojure :default [expr]
  (throw (ex-info "->clojure has no implementation for this" {:expr expr})))

(defmethod ->clojure ::string [s]
  s)
(defmethod ->clojure ::number [n]
  n)

(defmethod ->clojure :Unsigned [[_unsigned s]]
  (parse-long s))

(defmethod ->clojure :Signed [[_signed x y]]
  (if-let [[_unsigned s] y]
    (cond-> (parse-long s)
      (= x "-") -)
    (->clojure x)))

(defmethod ->clojure :Float [[_float s]]
  (parse-double s))

(defmethod ->clojure :String [s]
  (str/join (map ->clojure (rest s))))

(defmethod ->clojure :Escaped [[_escaped s]]
  (subs s 1))

(doseq [tag [:File :LocalPointer :TOKEN]]
  (defmethod ->clojure tag [[_tag wrapped]]
    (->clojure wrapped)))

(doseq [tag [:Attributes :Defs :Fixeds :Floats :Pointer]]
  (defmethod ->clojure tag [expr]
    (into [] (map ->clojure) (rest expr))))

(defmethod ->clojure :Header [[_header type-name id]]
  {:type         (->clojure type-name)
   :sii/block-id (->clojure id)})

(defn- +attr [unit {:keys [array? kw value]}]
  (if array?
    (if (contains? unit kw)
      (update unit kw conj value)
      (assoc unit kw [value]))
    (assoc unit kw value)))

(defmethod ->clojure :Unit [[_unit header attrs]]
  (let [base (->clojure header)]
    (reduce +attr base (->clojure attrs))))

(defmethod ->clojure :Attribute [[_tag a b c]]
  (let [label  (->clojure a)
        array? (= b [:ArrayElement])]
    {:kw     (keyword label)
     :label  label
     :value  (->clojure (if array? c b))
     :array? array?}))

;; TODO: Only some structures are getting properly unwrapped, currently.

(defn- strip-bom [s]
  (str/replace s "\ufeff" ""))

(defn parse-text [sii-string]
  (->> sii-string
       strip-bom
       (insta/parse sii-parser)
       ->clojure))

(defn parse-text-file [path]
  (parse-text (slurp path)))

;; TODO: Support nested includes?
(defn- resolve-includes [s include-fn]
  (loop [s s]
    (let [s' (str/replace s #"\n@include \"([^\"]*)\"\s*\n"
                          (fn [[_whole filename]]
                            (str "\n" (include-fn filename) "\n")))]
      (if (= s s')
        s'
        (recur s')))))

(defn parse-with-includes [sii-string include-fn]
  (->> (resolve-includes sii-string include-fn)
       parse-text))

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
