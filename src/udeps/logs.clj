(ns udeps.logs)

;; See color ant text formating ref:
;;https://stackoverflow.com/questions/4842424/list-of-ansi-color-escape-sequences

(def ^:dynamic verbose? true)

(def head   "\u001B[0;38;5;241m(µdeps)")
(def reset  "\u001B[0m")
(def yellow "\u001B[0;38;5;250m")

(defn error
  [dep msg source]
  (let [icon      "\u2718"
        red-bold  "\u001B[0;38;5;9;1m"
        pink      "\u001B[0;38;5;222;1m"
        wolfblue   "\u001B[0;38;5;69;1m"]

    (println head
             (str pink *ns*)
             (str red-bold icon)
             (str red-bold dep)
             (str yellow msg)
             (str wolfblue source)
             reset)))


(defn success
  [dep msg source]
  (if verbose?
    (let [icon       "\u2714"
          green-bold "\u001B[0;32;1m"
          pink       "\u001B[0;38;5;222;1m"
          wolfblue   "\u001B[0;38;5;69;1m"]

      (println head
               (str pink *ns*)
               (str green-bold icon)
               (str green-bold dep)
               (str yellow msg)
               (str wolfblue source)
               reset))))
