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
                (if-let [data (parser/read-dep! dep sources)]
                  (let [fn-name     (:name data)
                        body      (:body data)
                        fn-fullname  (str *ns* "/" (name fn-name))]
                    (if (-> fn-fullname symbol resolve)
                      (log/error dep (str "#'" fn-fullname " already defined"))
                      (do
                        (if (:cache data)
                          (log/cache fn-fullname)
                          (log/success fn-fullname))
                        `(def ~(symbol fn-name) ~(read-string body)))))))
              deps))))
