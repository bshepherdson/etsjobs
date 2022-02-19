(ns ets.jobs.decrypt.interface
  (:require
    [ets.jobs.decrypt.core :as core]))

(defn decode
  "Given a filename, reads it in and decrypts the AES container.
  This works on both binary and text .sii files.
  Returns a java.nio.ByteBuffer for the resulting file."
  [filename]
  (core/decode filename))

