(ns build
  (:require
   [clojure.tools.build.api :as b]))

(defn compile-java [_]
  (b/javac {:src-dirs   ["java"]
            :class-dir  "target/classes"
            :javac-opts ["-source" "11" "-target" "11"]}))
