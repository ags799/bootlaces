(set-env! :resource-paths #{"src", "test"}
          :dependencies `[[adzerk/boot-test "1.2.0"]
                          [org.clojure/clojure ~(clojure-version)]
                          [tolitius/boot-check "0.1.4"]])

(require '[clojure.java.shell]
         '[tolitius.boot-check :as check]
         '[adzerk.boot-test :refer :all])

(defn git-version []
  (clojure.string/trim
    (:out (clojure.java.shell/sh "git" "rev-parse" "--short" "HEAD"))))

(task-options!
  pom {:project 'ags799/bootlaces
       :version (git-version)}
  aot {:namespace #{'ags799.bootlaces}}
  jar {:main 'ags799.bootlaces}
  install {:pom "ags799/bootlaces"})

(deftask check "Check your code" []
  (comp
    (check/with-eastwood "-t")
    (check/with-kibit "-t")
    (check/with-bikeshed "-t")))

(deftask publish-local "Publish to local Maven repository" []
  (comp (aot) (pom) (uber) (jar) (install)))
