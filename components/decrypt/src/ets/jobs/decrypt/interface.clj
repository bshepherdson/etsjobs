(ns ets.jobs.decrypt.interface
  (:require
   [ets.jobs.decrypt.core :as core])
  (:import
   java.nio.ByteBuffer))

(defn decode-file
  "Given a filename, reads it in and decrypts the AES container.
  This works on both binary and text .sii files.
  Returns a java.nio.ByteBuffer for the resulting file."
  [filename]
  (core/decode-file filename))

(defn decode
  "Given a `ByteBuffer`, reads it in and decrypts the AES container.
  This works on both binary and text .sii files.
  Returns a java.nio.ByteBuffer for the resulting file."
  [^ByteBuffer buf]
  (core/decode buf))

