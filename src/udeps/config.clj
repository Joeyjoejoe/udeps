(ns udeps.config
  (:require [integrant.core :as ig]
            [clojure.java.io :as io]
            [clojure.edn :as edn]))

(defn init []
  (let [default-config (-> "udeps-default.edn" io/resource slurp ig/read-string)]
    (if-let [user-config (io/resource "udeps.edn")]
      (ig/init (merge default-config (-> user-config slurp ig/read-string)))
      (ig/init default-config))))

(defmethod ig/init-key :folder [_ path]
  (fn [dep]
    (let [fname     (name dep)
          file-path (str path fname ".edn")
          ns-fname  (str *ns* "/" fname)]
    (try
      (if-let [fdata (-> file-path slurp edn/read-string)]
        fdata)
      (catch java.io.FileNotFoundException e
        (throw (ex-info (str "File not found at " file-path) {:dep dep})))))))
