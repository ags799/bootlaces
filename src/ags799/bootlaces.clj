(ns ags799.bootlaces
  {:boot/export-tasks true}
  (:gen-class)
  (:require [boot.core :as boot]
            [boot.task.built-in :refer [aot pom uber jar install]]
            [clojure.java.shell :refer [sh]]
            [tolitius.boot-check :as check]))

(defn- short-commit-hash []
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
  [v version VAL str "optional version string, short commit hash by default"
   p project VAL sym "Maven group and artifact, separated by a slash"
   n namespaces VAL edn "set of namespaces to be included in the uberjar"]
  (let [the-version (if version version (short-commit-hash))]
    (comp (aot :namespace namespaces)
          (pom :project project :version the-version)
          (uber)
          (jar))))

(boot/deftask publish-local
  "Publish uber jar to local Maven repository.

  An uber jar is created with the uberjar task, and is then installed to your
  local Maven repository."
  []
  (comp (uberjar) (install)))
