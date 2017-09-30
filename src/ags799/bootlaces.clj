(ns ags799.bootlaces
  (:gen-class)
  (:require [clojure.java.shell :refer [sh]]))

(defn greeting [] "Hello, world!")

(defn version
  "Returns the project's version.

  The version is the HEAD commit's short hash. This is done to support
  continuous deployment: if we deploy every commit on the deployed branch,
  then we won't necessarily tag every commit that is deployed. However, those
  commits will have a hash. That's what we use as our version."
  []
  (clojure.string/trim (:out (sh "git" "rev-parse" "--short" "HEAD"))))

(defn -main [] (println (greeting)))
