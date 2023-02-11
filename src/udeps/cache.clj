(ns udeps.cache
  (:require [clojure.edn :as edn]))

(derive :cfg/cache?     :cfg/param)
(derive :cfg/cache-path :cfg/param)

(defn save-cache! [cfg dep data]
  (let [{:cfg/keys [cache? cache-path]} cfg]
    (if (and cache? data)
      (let [h  (hash dep)
            fullpath (str cache-path h ".edn")]
        (-> cache-path java.io.File. .mkdir)
        (spit fullpath (pr-str data))))
    data))


(defn get-cache
  [cfg dep]
  (let [{:cfg/keys [cache? cache-path]} cfg
        h        (hash dep)
        fullpath (str cache-path h ".edn")]
    (if cache?
      (try
        (if-let [data (-> fullpath slurp edn/read-string)]
          (assoc data :source :src/cache))
        (catch java.io.FileNotFoundException e)))))
