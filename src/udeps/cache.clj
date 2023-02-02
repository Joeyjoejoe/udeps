(ns udeps.cache
  (:require [clojure.java.io :as io]
            [clojure.edn :as edn]))


(defn save-cache! [dep data]
  (let [h  (hash dep)
        filename (str ".udeps-cache/" h ".edn")]
    (do
      (-> ".udeps-cache/" java.io.File. .mkdir)
      (spit filename (pr-str data))
      data)))


(defn get-cache
  [dep]
  (let [h    (hash dep)
        path (str ".udeps-cache/" h ".edn")]
    (try
      (if-let [data (-> path slurp edn/read-string)]
        (assoc data :source :src/cache))
      (catch java.io.FileNotFoundException e))))
