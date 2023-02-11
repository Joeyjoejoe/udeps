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
      (let [msg (ex-message e)
            data (ex-data e)]
          (log/error (:dep data) msg (src-key dep))))))

(defmulti read-dep!
  (fn [dep cfg]
    (class dep)))

(defmethod read-dep!
  clojure.lang.Keyword
  [dep cfg]
  (let [src-k   (src-key dep)]
    (if-let [f (src-k cfg)]
      (if-let [data (c/get-cache cfg dep)]
        data
        (->> dep
             (query-dep f)
             (c/save-cache! cfg dep)))
      (log/error dep "Unknown source" src-k))))

(defmethod read-dep!
  clojure.lang.PersistentVector
  [dep cfg]
  (let [[fkey _ fname] dep]
    (if-let [fdata (read-dep! fkey cfg)]
      (merge fdata {:name fname}))))
