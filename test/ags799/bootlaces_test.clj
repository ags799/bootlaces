(ns ags799.bootlaces-test
  (:require [ags799.bootlaces :as b]
            [clojure.test :refer :all]))

(deftest greeting-tests
  (is (= "Hello, world!" (b/greeting))))
