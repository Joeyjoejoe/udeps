(ns udeps.logs)

;; See color ant text formating ref:
;;https://stackoverflow.com/questions/4842424/list-of-ansi-color-escape-sequences

(def ^:dynamic verbose? true)

(def head   "\u001B[0;38;5;241m(µdeps)")
(def reset  "\u001B[0m")

(defn error
  [data]
  (let [{:keys [dep src msg]} data
        icon      "\u2718"
        red       "\u001B[0;38;5;196m"
        dgray     "\u001B[0;38;5;241m"
        lgray     "\u001B[0;38;5;244m"
        pink      "\u001B[0;38;5;222;1m"
        purple    "\u001B[0;38;5;161;1m"
        wolfblue  "\u001B[0;38;5;99m"]

    (println head
             (str pink *ns*)
             (str red icon)
             (str dgray "{" purple ":msg " dgray "\"" lgray msg dgray "\"")
             (str purple ":dep " wolfblue dep)
             (str purple ":src " wolfblue src dgray "}")
             reset)))


(defn success
  [title data]
  (if verbose?
    (let [{:keys [dep src]} data
          icon     "\u2714"
          green    "\u001B[0;32m"
          purple   "\u001B[0;38;5;161;1m"
          pink     "\u001B[0;38;5;222;1m"
          wolfblue "\u001B[0;38;5;99m"
          dgray    "\u001B[0;38;5;241m"]

      (println head
               (str pink *ns*)
               (str green icon)
               (str green title reset)
               (str dgray "{" purple ":dep " wolfblue dep reset)
               (str purple ":src " wolfblue src dgray "}" reset)
               reset))))

(defn warn
  [title data]
  (if verbose?
    (let [{:keys [dep src msg]} data
          icon       "\u2714"
          orange     "\u001B[0;38;5;202m"
          purple     "\u001B[0;38;5;161;1m"
          lgray     "\u001B[0;38;5;230m"
          pink       "\u001B[0;38;5;222;1m"
          wolfblue   "\u001B[0;38;5;99m"
          dgray      "\u001B[0;38;5;241m"]

      (println head
               (str pink *ns*)
               (str orange icon)
               (str orange title reset)
               (str dgray "{"
                    purple (if msg
                             (str ":msg " dgray "\"" wolfblue msg dgray "\""))
                    purple ":dep " wolfblue dep)
               (str purple ":src " wolfblue src dgray "}")
               reset))))
