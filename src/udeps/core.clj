(ns udeps.core
  (:require [integrant.core :as ig]
            [udeps.cfg :as cfg]
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
  (let [conf (ig/init (cfg/build))]
    `(do
       ~@(map
           (fn [dep]
             (binding [udeps.logs/verbose? (:cfg/verbose conf)]
               (if-let [data (parser/read-dep! dep conf)]
                 (let [{:keys [body source]}  data
                       fn-name     (:name data)
                       fn-fullname (str *ns* "/" (name fn-name))
                       var-name    (str "#'" fn-fullname)
                       log-data    {:dep dep :src source}
                       defined?    (-> fn-fullname symbol resolve)]

                     (try
                       (if-let [f `(def ~(symbol fn-name) ~(read-string (str "(fn " body ")")))]
                          (do (if defined?
                                (log/warn var-name (assoc log-data :msg "function overridden"))
                                (log/success var-name log-data))
                              f))
                       (catch Exception e
                         (log/error (assoc log-data :msg (ex-message e)))))))))
           deps)
       nil)))


(defmacro defdep
  "Perform a usual defn but adds extra metadata to facilitate udeps export"
  [fname & fdecl]
  (let [source (clojure.string/join " " fdecl)]
    `(defn ~(vary-meta fname merge {:source-code  source
                                    :fname (name fname)})
       ~@fdecl)))


(derive :cfg/export-path :cfg/param)

(defn export!
  "Export function to udeps file"
  [fn-var]
  (let [{:keys [fname source-code arglists]} (meta fn-var)
        udeps-cfg (ig/init (cfg/build))
        fn-name   (or fname (name (symbol fn-var)))
        body      (or source-code (repl/source-fn (symbol fn-var)))
        file-path  (str (:cfg/export-path udeps-cfg) fn-name)]

    (println file-path)

    (with-open [wrtr (clojure.java.io/writer file-path)]
      (.write wrtr (str {:name     fn-name
                         :body     body
                         :arglists arglists})))
    file-path))



