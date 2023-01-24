(ns udeps.parser
  (:require [udeps.logs :as log]))

(defmulti read-dep!
  (fn [dep sources]
    (class dep)))

(defmethod read-dep!
  clojure.lang.Keyword
  [dep sources]
  (let [src-key (keyword (namespace dep))]
    (if-let [f (src-key sources)]
      (try
        (f dep)
        (catch clojure.lang.ExceptionInfo e
          (let [msg (ex-message e)
                data (ex-data e)]
            (log/error (:dep data) msg))))
      (log/error dep (str "Unknown source " (keyword (namespace dep)))))))

(defmethod read-dep!
  clojure.lang.PersistentVector
  [dep sources]
    (let [[fkey _ fname] dep]
      (if-let [fdata (read-dep! fkey sources)]
        (merge fdata {:name fname}))))
