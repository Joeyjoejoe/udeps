(ns udeps.core-test
  (:require [clojure.test :as t]
            [udeps.core :as core]))

(t/deftest run
  (t/testing "Test run function"
    (t/is (= true (core/run)) "runs with default conf ?")
    (t/is (boolean? (core/run)))) "output a boolean ?")
