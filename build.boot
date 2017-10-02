(set-env! :resource-paths #{"src"}
          :dependencies `[[org.clojure/clojure ~(clojure-version)]
                          [tolitius/boot-check "0.1.5"]])

(require '[ags799.bootlaces :refer [check uberjar publish-local]])

(task-options!
  uberjar {:namespaces #{'ags799.bootlaces}
           :project 'org.clojars.ags799/bootlaces}
  publish-local {:project "org.clojars.ags799/bootlaces"})
