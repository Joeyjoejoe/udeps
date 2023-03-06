(ns udeps.tools
  (:require [integrant.core :as ig]
            [clojure.repl :as repl]
            [udeps.cfg :as cfg]))

(defn export!
  "Export function bound to fn-var var as a udeps file"
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

(defmacro defdep
  "Perform a usual defn but adds extra metadata to facilitate
   udeps export from repl defined functions"
  [fname & fdecl]
  (let [source (clojure.string/join " " fdecl)]
    `(defn ~(vary-meta fname merge {:source-code  source
                                    :fname (name fname)})
       ~@fdecl)))
