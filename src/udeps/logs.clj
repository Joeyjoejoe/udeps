(ns udeps.logs)

;; See color ant text formating ref:
;;https://stackoverflow.com/questions/4842424/list-of-ansi-color-escape-sequences

(def ^:dynamic verbose? true)
(def head "\u001B[38;5;238m(µdeps)")

(defn error
  [title msg]
  (let [icon      "\u2718"
        red       "\u001B[38;5;9m"
        red-bold  "\u001B[38;5;9;1m"
        dark-red  "\u001B[38;5;214m"
        grey      "\u001B[38;5;243m"
        dark-grey "\u001B[38;5;238m"
        reset     "\u001B[0m"]

    (println head (str red icon) (str grey *ns*) (str red-bold title reset) (str dark-red msg reset))))


(defn success
  [fname source]
  (if verbose?
    (let [icon       "\u2714"
          green      "\u001B[32m"
          green-bold "\u001B[32;1m"
          grey       "\u001B[38;5;243m"
          dark-grey  "\u001B[38;5;238m"
          blue-bold  "\u001B[38;5;34;1m"
          reset      "\u001B[0m"]

        (println head (str green icon) (str grey *ns*) (str green-bold "#'" fname reset) (str dark-grey source reset)))))
