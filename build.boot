(set-env! :resource-paths #{"src", "test"}
          :dependencies `[[adzerk/boot-test "1.2.0"]
                          [org.clojure/clojure ~(clojure-version)]
                          [tolitius/boot-check "0.1.4"]])

(require '[adzerk.boot-test :refer :all]
         '[ags799.bootlaces :refer [check version]])

(task-options!
  pom {:project 'org.clojars.ags799/bootlaces
       :version (version)}
  aot {:namespace #{'ags799.bootlaces}}
  jar {:main 'ags799.bootlaces}
  install {:pom "org.clojars.ags799/bootlaces"})

(deftask publish-local "Publish to local Maven repository" []
  (comp (aot) (pom) (uber) (jar) (install)))
