(ns ets.jobs.decrypt
  (:require
    [clojure.java.io :as io])
  (:import
    [java.io ByteArrayInputStream ByteArrayOutputStream]
    [java.nio ByteBuffer ByteOrder]
    [javax.crypto Cipher]
    [javax.crypto.spec IvParameterSpec SecretKeySpec]))

(def ^:private sii-key-bytes
  "This key was taken from a 'Savegame Decrypter' utility made by user
  JohnnyGuitar. You can find more info here:
  http://forum.scssoft.com/viewtopic.php?f=34&t=164103#p280580"
  (byte-array
    [0x2a 0x5f 0xcb 0x17 0x91 0xd2 0x2f 0xb6
     0x02 0x45 0xb3 0xd8 0x36 0x9e 0xd0 0xb2
     0xc2 0x73 0x71 0x56 0x3f 0xbf 0x1f 0x3c
     0x9e 0xdf 0x6b 0x11 0x82 0x5a 0x5d 0x0a]))

(def ^:private sii-key (SecretKeySpec. sii-key-bytes "AES"))

(def ^:private sii-encrypted-signature
  "ScsC"
  0x43736353)

(def ^:private sii-normal-signature
  "SiiN"
  0x4e696953)

; The SIIHeader is a struct like this:
; signature  u32, one of the above values.
; HMAC       byte[32]
; InitVector byte[16]
; DataSize   u32
;
; This is AES with a 256-bit key and using CBC mode with the initial vector
; stored in the header.
;
; TODO I'm not sure what the padding type is, nor the actual inner format.

(defn- signature [buf]
  (.getInt buf 0))

(defn encrypted? [buf]
  (= sii-encrypted-signature (signature buf)))

(defn- read-header [buf]
  (let [sig (signature buf)
        iv-bytes (byte-array 16)]
    (.get buf 36 iv-bytes)
    [sig (IvParameterSpec. iv-bytes) (.getInt buf 52)]))

(defn- read-body [buf]
  (let [out (byte-array (- (.limit buf) 56))]
    (.get buf 56 out)
    out))

(defn binary-slurp [path]
  (with-open [in   (io/input-stream (io/file path))]
    (let [out (ByteArrayOutputStream.)]
      (io/copy in out)
      (-> (.toByteArray out)
          (ByteBuffer/wrap)
          (.order ByteOrder/LITTLE_ENDIAN)))))

(defn binary-spit [path content]
  (with-open [out (io/output-stream (io/file path))]
    (let [in (ByteArrayInputStream. (.array content))]
      (io/copy in out))))

(defn decrypt [buf]
  (let [[sig iv size] (read-header buf)
        body          (read-body buf)
        cipher (Cipher/getInstance "AES/CBC/PKCS5Padding")]
    (.init cipher Cipher/DECRYPT_MODE sii-key iv)
    (.doFinal cipher body)))

(defn inflate [ba]
  (let [is (java.util.zip.InflaterInputStream. (ByteArrayInputStream. ba))
        os (ByteArrayOutputStream.)]
    (io/copy is os)
    (.toByteArray os)))

(defn decode [filename]
  (-> filename
      binary-slurp
      decrypt
      inflate
      (ByteBuffer/wrap)
      (.order ByteOrder/LITTLE_ENDIAN)))


; Header: 
; signature  u32, one of the above values.
; HMAC       byte[32]
; InitVector byte[16]
; DataSize   u32
;
; In the below that's:
; Signature: 53637343 = ScsC
; HMAC:      b404 953d 51ac 2745 ddad c203 8670 9f0a
;            7a0d 0f5e 7ce4 3d05 6bbd 4dde f133 85d5
; IV:        afdd 0318 fac8 95aa 8997 6a64 5caf 2b7c
; Datasize:  51c3 2100
; 
; 0x21c351 = 2,212,689  - this is the unzipped size I guess.
