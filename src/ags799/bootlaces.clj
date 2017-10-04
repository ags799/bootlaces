(ns ags799.bootlaces
  {:boot/export-tasks true}
  (:require [adzerk.boot-test :as boot-test]
            [ags799.bootlaces.docker :as docker]
            [boot.core :as boot]
            [boot.task.built-in :refer [aot pom uber jar install push]]
            [tolitius.boot-check :as check]
            [clojure.string]))

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
   p project VAL sym "Maven group and artifact, separated by a slash"]
  (comp (aot :all true)
        (pom :project project :version the-version)
        (uber)
        (jar)))

(boot/deftask publish
  "Publish uber jar to remote Maven repository."
  [p project VAL str "Maven group and artifact, separated by a slash"]
  (comp (uberjar) (push :pom project :repo "clojars")))

(boot/deftask publish-local
  "Publish uber jar to local Maven repository."
  [p project VAL str "Maven group and artifact, separated by a slash"]
  (comp (uberjar) (install :pom project)))

(defn bootlaces!
  "A setup function that must be called before any bootlaces tasks are called.

  The only parameter is the project's group ID and artifact ID separated by a
  slash, given as a symbol. For example, 'org.clojure/clojure.

  The short commit hash of the HEAD commit is used as the version. This is
  done as a fool-proof way of supporting continuous deployment."
  [project]
  (boot/set-env! :repositories #(conj %
                 ["clojars" {:url "https://clojars.org/repo/"
                             :username (System/getenv "CLOJARS_USERNAME")
                             :password (System/getenv "CLOJARS_PASSWORD")}]))
  let [project-str (str project)
       maven-coordinates (clojure.string/split project-str #"/")
       group (first maven-coordinates)
       artifact (second maven-coordinates)
       version (short-commit-hash)]
  (boot/task-options!
    uberjar {:project project
             :version version}
    publish {:project project-str}
    publish-local {:project project-str}
    docker/docker-image {:image-name artifact}
    docker/docker-tag {:group-name group
                :image-name artifact
                :tag version}
    docker/docker-push {:group-name group
                 :image-name artifact}))
