(ns ags799.bootlaces
  {:boot/export-tasks true}
  (:gen-class)
  (:require [adzerk.boot-test :as boot-test]
            [boot.core :as boot]
            [boot.task.built-in :refer [aot pom uber jar install]]
            [clojure.java.shell :refer [sh]]
            [tolitius.boot-check :as check]))

(defn- short-commit-hash []
  (clojure.string/trim (:out (sh "git" "rev-parse" "--short" "HEAD"))))

(boot/deftask check
  "Checks code for style errors.

  Runs several linters to analyze code for style errors and common mistakes."
  ; linters named per https://github.com/jonase/eastwood#whats-there"
  [e exclude-linters VAL edn "linters to exclude from eastwood"]
  (comp
    (check/with-eastwood "-t" :options {:exclude-linters exclude-linters})
    (check/with-kibit "-t")
    (check/with-bikeshed "-t")))

(boot/deftask verify
  "One stop shop for all automated code critique: tests, linters, the works."
  []
  (comp (boot-test/test) (check)))

(boot/deftask uberjar
  "Create an uber jar.

  Your Clojure source files are compiled ahead-of-time, a POM is generated,
  and an uber jar with your dependencies is created. This jar is installed to
  your local Maven repository."
  [v version VAL str "optional version string, short commit hash by default"
   p project VAL sym "Maven group and artifact, separated by a slash"
   n namespaces VAL edn "set of namespaces to be included in the uberjar"]
  (let [the-version (or version (short-commit-hash))]
    (comp (aot :namespace namespaces)
          (pom :project project :version the-version)
          (uber)
          (jar))))

(boot/deftask publish-local
  "Publish uber jar to local Maven repository.

  An uber jar is created with the uberjar task, and is then installed to your
  local Maven repository."
  [p project VAL str "Maven group and artifact, separated by a slash"]
  (comp (uberjar) (install :pom project)))
