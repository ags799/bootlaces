(set-env! :resource-paths #{"src"}
          :dependencies `[[org.clojure/clojure ~(clojure-version)]
                          [adzerk/boot-test "1.2.0"]
                          [org.clojure/tools.nrepl "0.2.13"] ; necessary for docker-image task to run
                          [tolitius/boot-check "0.1.5"]
                          [onetom/boot-lein-generate "0.1.3"]])

(require '[ags799.bootlaces :refer :all])

(task-options! check {:exclude-linters [:unused-ret-vals]})

(bootlaces! 'ags799/bootlaces)
