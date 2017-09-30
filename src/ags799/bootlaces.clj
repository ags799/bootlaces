(ns ags799.bootlaces
  {:boot/export-tasks true}
  (:gen-class)
  (:require [boot.core :as boot]
            [clojure.java.shell :refer [sh]]
            [tolitius.boot-check :as check]))

(defn greeting [] "Hello, world!")

(defn version
  "Returns the project's version.

  The version is the HEAD commit's short hash. This is done to support
  continuous deployment: if we deploy every commit on the deployed branch,
  then we won't necessarily tag every commit that is deployed. However, those
  commits will have a hash. That's what we use as our version."
  []
  (clojure.string/trim (:out (sh "git" "rev-parse" "--short" "HEAD"))))

(boot/deftask check
  "Checks code for style errors.

  TODO(asharp): document check task (#15)."
  []
  (comp
    (check/with-kibit "-t")
    (check/with-bikeshed "-t")))

(defn -main [] (println (greeting)))
