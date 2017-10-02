(set-env! :resource-paths #{"src"}
          :dependencies `[[org.clojure/clojure ~(clojure-version)]
                          [tolitius/boot-check "0.1.5"]])

(require '[ags799.bootlaces :refer :all])

(bootlaces! 'org.clojars.ags799/bootlaces)

(task-options!
  check {:exclude-linters [:unused-ret-vals]})
