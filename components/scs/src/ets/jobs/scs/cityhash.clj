(ns ets.jobs.scs.cityhash
  (:import
   ets.jobs.scs.CityHash
   java.nio.ByteBuffer))

(defn cityhash64
  "Wrapper for calling cityhash64 in the Java version."
  [input]
  (cond
    (string? input)              (CityHash/cityHash64 (.getBytes input) 0 (count input))
    (instance? ByteBuffer input) (let [len (.remaining input)
                                       arr (byte-array len)]
                                   (.get input arr)
                                   (CityHash/cityHash64 arr 0 len))
    :else (throw (ex-info "Invalid type for cityhash64" {:type (type input)}))))

;; Original attempt at a Clojure implementation - slow and doesn't work.
;; Using the Java version I found instead.
;(def ^:private k0 (Long/parseUnsignedLong "c3a5c85c97cb3127" 16))
;(def ^:private k1 (Long/parseUnsignedLong "b492b66fbe98f273" 16))
;(def ^:private k2 (Long/parseUnsignedLong "9ae16a3b2f90404f" 16))
;(def ^:private k3 (Long/parseUnsignedLong "c949d7c7509e6557" 16))
;
;(comment
;  (Long/parseUnsignedLong "0xc3a5c85c97cb3127"  #_(- (long -0x3c5a37a36834ced9)))
;  (= 0xc3a5c85c97cb3127N (bit-or (bit-shift-left 0xc3a5c85c 32) 0x97cb3127))
;  )
;
;(def ^:private kmul (Long/parseUnsignedLong "9ddfea08eb382d69" 16))
;
;(defn- rotate-by-at-least-1 [^long val ^long shift]
;  (bit-or (bit-shift-right val shift)
;          (bit-shift-left  val (- 64 shift))))
;
;(defn- rotate [^long val ^long shift]
;  (if (zero? shift)
;    val
;    (bit-or (bit-shift-right val shift)
;            (bit-shift-left  val (- 64 shift)))))
;
;(defn- shift-mix [^long val]
;  (bit-xor val (bit-shift-right val 47)))
;
;(defn- hash-128-to-64 [^long u ^long v]
;  (->> (bit-xor u v)
;       (unchecked-multiply kmul)
;       (bit-shift-right 47)
;       (bit-xor v)
;       (unchecked-multiply kmul)
;       (bit-shift-right 47)
;       (unchecked-multiply kmul)))
;
;(defn- hash-len-16 [^long u ^long v]
;  (hash-128-to-64 u v))
;
;(defn- hash-len-0-to-16 [^ByteBuffer buf ^long pos ^long len]
;  (cond
;    (> len 8)  (let [a (.getLong buf pos)
;                     b (.getLong buf (+ pos len -8))]
;                 (->> (rotate-by-at-least-1 (+ b len) len)
;                      (hash-len-16 a)
;                      (bit-xor b)))
;    (>= len 4) (let [a (bit-and (long 0xffffffff)
;                                (long (.getInt buf pos)))
;                     b (bit-and (long 0xffffffff)
;                                (long (.getInt buf (+ pos len -4))))]
;                 (hash-len-16 (+ len (bit-shift-left a 3)) b))
;    (pos? len) (let [a (->> pos
;                            (.get buf)
;                            (bit-and 0xff))
;                     b (->> pos
;                            (+ (bit-shift-right len 1))
;                            (.get buf)
;                            (bit-and 0xff))
;                     c (->> pos
;                            (+ len -1)
;                            (.get buf)
;                            (bit-and 0xff))
;                     y (+ a   (bit-shift-left b 8))
;                     z (+ len (bit-shift-left c 2))]
;                 (->> (* z k3)
;                      (bit-xor (* y k2))
;                      shift-mix
;                      (* k2)))
;    :else k2))
;
;(defn- hash-len-17-to-32 [^ByteBuffer buf ^long pos ^long len]
;  (let [a (->> pos             (.getLong buf) (* k1))
;        b (->> (+ pos 8)       (.getLong buf))
;        c (->> (+ pos len -8)  (.getLong buf) (* k2))
;        d (->> (+ pos len -16) (.getLong buf) (* k0))]
;    (hash-len-16 (+ (rotate (- a b) 43)
;                    (rotate c 30)
;                    d)
;                 (+ a
;                    (rotate (bit-xor b k3) 20)
;                    (- len c)))))
;
;(defn- hash-len-33-to-64 [^ByteBuffer buf ^long pos ^long len]
;  (let [z (->> (+ pos 24) (.getLong buf))
;        a (->> (+ pos len -16) (+ len) (* k0) (+ (.getLong buf pos)))
;        b (rotate (+ a z) 52)
;        c (rotate a 37)
;        
;        a (+ a (.getLong buf (+ pos 8)))
;        c (+ c (rotate a 7))
;        a (+ a (.getLong buf (+ pos 16)))
;
;        vf (+ a z)
;        vs (+ b (rotate a 31) c)
;
;        a (+ (.getLong buf (+ pos 16))
;             (.getLong buf (+ pos len -32)))
;        z (.getLong buf (+ pos len -8))
;        b (rotate (+ a z) 52)
;        c (rotate a 37)
;        a (->> (+ pos len -24) (.getLong buf) (+ a))
;        c (+ c (rotate a 7))
;        a (->> (+ pos len -16) (.getLong buf) (+ a))
;
;        wf (+ a z)
;        ws (+ b (rotate a 31) c)
;        r  (shift-mix (+ (* k2 (+ vf ws))
;                         (* k0 (+ wf vs))))]
;    (->> (* r k0)
;         (+ vs)
;         shift-mix
;         (* k2))))
;
;(defn- weak-hash-len-32-with-seeds
;  [^ByteBuffer buf ^long pos ^long a ^long b]
;  (let [w   (.getLong buf pos)        
;        x   (.getLong buf (+ pos 8))  
;        y   (.getLong buf (+ pos 16)) 
;        z   (.getLong buf (+ pos 24)) 
;
;        a   (+ a w)
;        b   (rotate (+ b a z) 21)
;        c   a
;        a   (+ a x y)
;        b   (+ b (rotate a 44))
;        arr (long-array 2)]
;    (aset-long arr 0 (+ a z))
;    (aset-long arr 1 (+ b c))
;    arr))
;
;(defn- hash-65-plus [^ByteBuffer buf ^long pos ^long len]
;  (let [x (.getLong buf (+ pos len -40))
;        y (+ (.getLong buf (+ pos len -16))
;             (.getLong buf (+ pos len -56)))
;        z (hash-len-16 (+ len (.getLong buf (+ pos len -48)))
;                       (.getLong buf (+ pos len -24)))
;        v (weak-hash-len-32-with-seeds buf (+ pos len -64) len z)
;        w (weak-hash-len-32-with-seeds buf (+ pos len -32) (+ y k1) x)
;        x (+ (* x k1)
;             (.getLong buf pos))]
;    (loop [x   x
;           y   y
;           z   z
;           v   v
;           w   w
;           pos pos
;           len (bit-and (- len 1) (bit-not 63))]
;      (let [x   (* k1 (rotate (+ x y (aget v 0) (.getLong buf (+ pos 8)))
;                              37))
;            y   (* k1 (rotate (+   y (aget v 1) (.getLong buf (+ pos 48)))
;                              42))
;            x   (bit-xor x (aget w 1))
;            y   (+ y (aget v 0) (.getLong buf (+ pos 40)))
;            z   (* k1 (rotate (+ z (aget w 0)) 33))
;            v   (weak-hash-len-32-with-seeds
;                  buf pos (* k1 (aget v 1)) (+ x (aget w 0)))
;            w   (weak-hash-len-32-with-seeds
;                  buf (+ pos 32) (+ z (aget w 1)) (+ y (.getLong buf (+ pos 16))))
;            len (- len 64)]
;        (if (zero? len)
;          (hash-len-16 (+ (hash-len-16 (aget v 0) (aget w 0))
;                          (* k1 (shift-mix y))
;                          z)
;                       (+ (hash-len-16 (aget v 1) (aget w 1))
;                          x))
;          ;; Note that x and z are swapped!
;          (recur z y x v w (+ pos 64) len))))))
;
;(defn- cityhash64-buf [^ByteBuffer buf ^long pos ^long len]
;  (cond
;    (<= len 16) (hash-len-0-to-16  buf pos len)
;    (<= len 32) (hash-len-17-to-32 buf pos len)
;    (<= len 64) (hash-len-33-to-64 buf pos len)
;    :else       (hash-65-plus      buf pos len)))
;
;(defn cityhash64 [input]
;  (let [^ByteBuffer buf
;        (cond
;          (string? input)              (ByteBuffer/wrap (.getBytes input))
;          (instance? ByteBuffer input) input
;          :else (throw (ex-info "Unknown input type" {:input input})))]
;    (cityhash64-buf buf 0 (.capacity buf))))
