(ns user
  (:require [integrant.core :as ig]
            [integrant.repl :refer [clear go halt prep init reset reset-all]]
            [integrant.repl.state :refer [system config]]
            [clojure.java.io :as io]
            [udeps.cfg :as cfg]
            [udeps.core :as udeps]))

;; https://github.com/weavejester/integrant-repl
;; Provides worflow function (prep) (init) (go) (reset) (halt)
(integrant.repl/set-prep! #(ig/prep (cfg/build)))
