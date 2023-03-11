(ns udeps.core-test
  (:require [clojure.test :refer [deftest testing is]]
            [udeps.core :as udeps]
            [udeps.tools :as utool]))

(defn classic-fn [] true)
(defn arity-fn ([] true) ([x] x))

(deftest tools-test

  (testing  "Testing function export/import"
    (let [_           (utool/export! #'classic-fn)
          _           (udeps/inject! [:local/classic-fn :as :aliased])
          aliased-var (ns-resolve 'udeps.core-test (symbol :aliased))]

      (is (= false (nil? aliased-var))
          "inject! defines a function")

      (is (= (classic-fn) (aliased-var))
          "export! & inject! functions are identical")))

  (testing  "Testing multi arity function export/import"
    (let [_           (utool/export! #'arity-fn)
          _           (udeps/inject! [:local/arity-fn :as :arity-fn])
          arity-var (ns-resolve 'udeps.core-test (symbol :arity-fn))]

      (is (= false (nil? arity-var))
          "inject! defines a function")

      (is (= (arity-fn) (arity-var))
          "export! & inject! functions are identical")

      (is (= (arity-fn 1) (arity-var 1))
          "Multi arity is preserved"))))
