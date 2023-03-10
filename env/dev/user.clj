(ns user
  (:require [integrant.core :as ig]
            [clojure.repl :as repl]
            [integrant.repl :refer [clear go halt prep init reset reset-all]]
            [integrant.repl.state :refer [system config]]
            [udeps.cfg :as cfg]
            [udeps.core :as udeps]
            [udeps.tools :as utools]))

;; https://github.com/weavejester/integrant-repl
;; Provides worflow function (prep) (init) (go) (reset) (halt)
(integrant.repl/set-prep! #(ig/prep (cfg/build)))

;; Use defdep instead of defn enables users to export deps from
;; functions defined in the repl. #'udeps/defdep is identical to
;; #'clojure.core/defn except it add extra meta data to defined
;; function var which are used to generate deps files.
(def ^:macro defn #'utools/defdep)

