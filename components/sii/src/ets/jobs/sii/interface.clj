(ns ets.jobs.sii.interface
  (:require
    [ets.jobs.sii.binary :as binary]
    [ets.jobs.sii.text :as text]))

(defn parse-sii [buf]
  (binary/parse-sii buf))

(defn parse-profile-basics [buf]
  (text/parse-profile-basics buf))

