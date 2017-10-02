(ns ags799.bootlaces
  {:boot/export-tasks true}
  (:gen-class)
  (:require [boot.core :as boot]
            [boot.task.built-in :refer [aot pom uber jar install]]
            [clojure.java.shell :refer [sh]]
            [tolitius.boot-check :as check]))

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

(boot/deftask uberjar
  "Create an uber jar.

  Your Clojure source files are compiled ahead-of-time, a POM is generated,
  and an uber jar with your dependencies is created. This jar is installed to
  your local Maven repository."
  []
  (comp (aot) (pom) (uber) (jar)))

(boot/deftask publish-local
  "Publish uber jar to local Maven repository.

  An uber jar is created with the uberjar task, and is then installed to your
  local Maven repository."
  []
  (comp (uberjar) (install)))
