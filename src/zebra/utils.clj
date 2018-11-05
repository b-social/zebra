(ns zebra.utils
  (:require [clojure.walk :refer [stringify-keys]]))

(defn transform-params [params]
  (stringify-keys params))
