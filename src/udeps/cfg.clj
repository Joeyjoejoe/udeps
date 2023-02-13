(ns udeps.cfg
  (:require [integrant.core :as ig]
            [clojure.java.io :as io]
            [clojure.edn :as edn]
            [org.httpkit.client :as http]))

(derive :cfg/verbose :cfg/param)

(defn- read-cfg [path]
  (if-let [cfg (io/resource path)]
    (-> cfg
        slurp
        ig/read-string)
    {}))

(defn build []
  (let [default-cfg (read-cfg "udeps-default.edn")
        user-cfg    (read-cfg "udeps.edn")]
    (merge default-cfg user-cfg)))

(defmethod ig/init-key :cfg/param [_ data] data)

(defmethod ig/init-key :src/local [_ path]
  (fn [dep]
    (let [fname     (name dep)
          file-path (str path fname)]
      (try
        (if-let [fdata (-> file-path slurp edn/read-string)]
          fdata)
        (catch java.io.FileNotFoundException e
          (throw (ex-info (str "File not found at " file-path) {:dep dep})))))))

(defmethod ig/init-key :src/remote [_ query-params]
  (let [{:keys [url params]} query-params]
    (fn [dep]
      (let [fn-url   (str url (name dep))
            response (http/get fn-url params)]

        (-> @response :body edn/read-string)))))
