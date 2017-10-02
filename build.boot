(set-env! :resource-paths #{"src"}
          :dependencies `[[org.clojure/clojure ~(clojure-version)]
                          [adzerk/boot-test "1.2.0"]
                          [tolitius/boot-check "0.1.5"]])

(require
  '[ags799.bootlaces :refer [check verify uberjar publish-local publish]])

(task-options!
  check {:exclude-linters [:unused-ret-vals]}
  uberjar {:namespaces #{'ags799.bootlaces}
           :project 'org.clojars.ags799/bootlaces}
  publish-local {:project "org.clojars.ags799/bootlaces"})
