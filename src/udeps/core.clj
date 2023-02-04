(ns udeps.core
  (:require [integrant.core :as ig]
            [udeps.config :as config]
            [udeps.parser :as parser]
            [udeps.logs :as log]))

(defmacro inject!
  "Inject functions in the current namespace based on deps vector.

  ```clojure
  (ns my.ns
  (:require [udeps.core :as udeps]))

  (udeps/inject!
    ;; Inject `foo` and `bar` functions in `my.ns` namespace
    :udeps/foo :udeps/bar

    ;; Inject `doo` aliased as `bop` in `my.ns` namespace
    [:udesp/doo :as :bop]
  )

  ;; Which you can use as building blocks for your functions
  (defn my-fn [n]
    (let [x (-> n foo bop)
          y (bar :y)]
      (+ x y)))
  ```
  "
  [& deps]
  (let [conf (ig/init (config/build))]
    `(do
       ~@(map
           (fn [dep]
             (binding [udeps.logs/verbose? (:cfg/verbose conf)]
               (if-let [data (parser/read-dep! dep conf)]
                 (let [fn-name     (:name data)
                       body      (:body data)
                       fn-fullname  (str *ns* "/" (name fn-name))]

                   (if (-> fn-fullname symbol resolve)
                     (log/error dep (str "#'" fn-fullname " already defined"))
                     (do
                       (log/success fn-fullname (:source data))
                       `(def ~(symbol fn-name) ~(read-string body))))))))
           deps))))
