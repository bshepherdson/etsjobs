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

