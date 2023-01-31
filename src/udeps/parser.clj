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
    (f dep)
    (catch clojure.lang.ExceptionInfo e
      (let [msg (ex-message e)
            data (ex-data e)]
          (log/error (:dep data) msg)))))

(defmulti read-dep!
  (fn [dep sources]
    (class dep)))

(defmethod read-dep!
  clojure.lang.Keyword
  [dep sources]
  (let [src-k   (src-key dep)]
    (if-let [f (src-k sources)]
      (if-let [data (c/get-cache dep)]
        data
        (->> dep
             (query-dep f)
             (c/save-cache! dep)))
      (log/error dep (str "Unknown source " src-k)))))

(defmethod read-dep!
  clojure.lang.PersistentVector
  [dep sources]
  (let [[fkey _ fname] dep]
    (if-let [fdata (read-dep! fkey sources)]
      (merge fdata {:name fname}))))
