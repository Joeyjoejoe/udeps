(ns udeps.logs)

(defn error
  [title msg]
  (let [icon     "\u2718"
        red      "\u001B[31m"
        dark-red "\u001B[38;5;124m"
        grey     "\u001B[38;5;243m"
        tab   " "
        reset    "\u001B[0m"]
    (println tab red icon grey title dark-red msg reset)))

(defn success
  [fname]
  (let [icon  "\u2714"
        green "\u001B[32m"
        grey  "\u001B[38;5;243m"
        tab   " "
        reset "\u001B[0m"]
   (println tab green icon grey (str "#'" fname) reset)))


(defn inject-header
  [nsname]
  (let [icon   "\u2605"
        gold   "\u001B[38;5;229m"
        orange "\u001B[38;5;214;1m"
        grey  "\u001B[38;5;243m"
        reset "\u001B[0m"
        nsname (str "(ns " nsname ")")]
  (println)
  (println gold icon (str orange " µdeps") "\u27A5" (str grey nsname reset))
  (println)))

(defn cache
  [fname]
  (let [icon  "\u2714"
        blue  "\u001B[38;5;27m"
        grey  "\u001B[38;5;243m"
        tab   " "
        reset "\u001B[0m"]
   (println tab blue icon grey (str "#'" fname) reset)))
