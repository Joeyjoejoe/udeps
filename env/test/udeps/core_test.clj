(ns udeps.core-test
  (:require [clojure.test :as t]
            [udeps.core :as udeps]))

(t/with-test

  (def data {:output      (udeps/inject! :http/hello-world.edn)
             :hello-world (-> 'hello-world resolve nil? not)})

  (t/is (nil?      (:output data))
        "inject! macro must return nil")

  (t/is (not (nil? (:hello-world data)))
        "hello-world function must be defined"))
