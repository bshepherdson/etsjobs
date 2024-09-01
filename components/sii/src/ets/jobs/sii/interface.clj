(ns ets.jobs.sii.interface
  (:require
    [ets.jobs.sii.binary :as binary]
    [ets.jobs.sii.text :as text]))

;; TODO: Probably I can remove parse-sii and rename parse-sii-raw to that.
(defn parse-sii [buf]
  (binary/parse-sii buf))

(defn parse-sii-raw [buf]
  (binary/parse-sii-raw buf))

(defn parse-profile-basics [buf]
  (text/parse-profile-basics buf))

(defn parse-text
  "Given the contents of a text SII file, parse it into Clojure form."
  [s]
  (text/parse-text s))

(defn parse-text-file
  "Given the path of a text SII file, read it in and parse it into Clojure form."
  [s]
  (text/parse-text-file s))

(defn parse-with-includes
  "Given the path of a text SII file, read it in and parse it into Clojure form.
  
  Uses `include-fn` to resolve `@include` directives."
  [s include-fn]
  (text/parse-with-includes s include-fn))
