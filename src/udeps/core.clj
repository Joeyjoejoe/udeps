(ns udeps.core
  (:require [udeps.config :as config]
            [udeps.parser :as parser]
            [udeps.logs :as log]))

(defmacro inject!
  "Inject functions in the current namespace based on deps vector.

  ```clojure
  (ns my.ns
  (:require [udeps.core :as udeps]))

  ;; Inject `foo` and `bar` functions in `my.ns` namespace
  (udeps/inject! :udeps/foo :udeps/bar)

  ;; Which you can use as building blocks for your functions
  (defn my-fn []
    (let [x (foo)
          y (bar :whatever)]
      (+ x y)))
  ```
  "
  [& deps]
  (let [sources (config/init)]
    (log/inject-header (str *ns*))
    `(do
       ~@(map (fn [dep]
                (if-let [fdata (parser/read-dep! dep sources)]
                  (let [fname     (:name fdata)
                        body      (:body fdata)
                        ns-fname  (str *ns* "/" (name fname))]
                    (if (-> ns-fname symbol resolve)
                      (log/error dep (str "#'" ns-fname " already defined"))
                      (do
                        (log/success ns-fname)
                        `(def ~(symbol fname) ~(read-string body)))))))
              deps))))
