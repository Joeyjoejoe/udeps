(ns udeps.core-test
  (:require [clojure.test :refer [deftest testing is]]
            [udeps.core :as udeps]
            [udeps.tools :as utool]))

;; (deftest
;;
;;   (def data {:output      (utool/export! #'cat)
;;              :hello-world (-> 'hello-world resolve nil? not)})
;;
;;   (is (nil?      (:output data))
;;         "inject! macro must return nil")
;;
;;   (is (not (nil? (:hello-world data)))
;;         "hello-world function must be defined"))

(defn classic-fn [] true)
(defn arity-fn ([] true) ([x] x))

(deftest tools-test
  (testing  "Testing (export! #'classic-fn)"
    (let [dep-path (utool/export! #'classic-fn)
          _        (udeps/inject! [:local/classic-fn :as :inject])]
      (is (= ".udeps/classic-fn" dep-path))))

  )

