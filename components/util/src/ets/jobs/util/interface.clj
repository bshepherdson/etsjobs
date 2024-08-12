(ns ets.jobs.util.interface
  (:require
   [com.wsscode.pathom3.connect.built-in.resolvers :as pbir]
   [com.wsscode.pathom3.connect.operation :as pco]))

(defn achievement [label spec f]
  (let [flag   (keyword "job.cheevo" (name label))
        output (keyword "jobs" (name label))] ; eg. :jobs/cattle-drive
    [(pco/resolver label (merge {::pco/output [flag]} spec) f)
     (pco/resolver (symbol (namespace label) (str (name label) "-jobs"))
                   {::pco/input   [{:jobs/all-available [:job/id flag]}]
                    ::pco/output  [{output              [:job/id]}]}
                   (fn [_env {jobs :jobs/all-available}]
                     {output (filter flag jobs)}))]))

(defmacro defachievement
  "Creates two resolvers for an achievement.
  
  Call it like
  
    (defachievement some-cheevo \"docstring\"
       pathom-spec
       (fn [env input] output...))
 
  and it generates two resolvers:
  - One `some-cheevo` that puts a boolean on each job
  - One called `some-cheevo-jobs` that is a `[{:job/id 123} ...]` of applicable jobs."
  ([sym spec f] `(defachievement ~sym "(no docs)" ~spec ~f))
  ([sym docstring spec f]
   `(def ~(symbol sym) ~docstring
      (achievement '~(symbol (str *ns*) (name sym)) ~spec ~f))))

(defn table-indexed-by
  "[[pbir/static-table-resolver]] for everything in a list of entities."
  ([source key-fn] (table-indexed-by source key-fn key-fn))
  ([source key-fn prop]
   (pbir/static-table-resolver
     prop
     (into {} (map (juxt key-fn identity) source)))))

(defn table-grouped-by
  "[[pbir/static-table-resolver]] from `key-fn` to `val-fn`, using `prop` as
  the key attribute."
  [source prop key-fn val-fn]
  (as-> source <>
       (group-by key-fn <>)
       (update-vals <> val-fn)
       (pbir/static-table-resolver prop <>)))
