(ns udeps.parser
  (:require [udeps.logs :as log]
            [udeps.cache :as c]))

(defn- src-key [dep]
  (->> dep
       namespace
       (str "src/")
       keyword))

(defn- query-dep [f dep]
  (try
    (assoc (f dep) :source (src-key dep))
    (catch clojure.lang.ExceptionInfo e
      (let [log-data {:msg (ex-message e)
                      :dep dep
                      :src (src-key dep)}]
          (log/error (merge (ex-data e) log-data ))))))

(defmulti read-dep!
  (fn [dep cfg]
    (class dep)))

(defmethod read-dep!
  clojure.lang.Keyword
  [dep cfg]
  (let [src-k    (src-key dep)
        log-data {:dep dep
                  :src src-k}]
    (if-let [f (src-k cfg)]
      (if-let [data (c/get-cache cfg dep)]
        data
        (->> dep
             (query-dep f)
             (c/save-cache! cfg dep)))
      (log/error (assoc log-data :msg "Unknown source")))))

(defmethod read-dep!
  clojure.lang.PersistentVector
  [dep cfg]
  (let [[fkey _ fname] dep]
    (if-let [fdata (read-dep! fkey cfg)]
      (merge fdata {:name fname}))))
