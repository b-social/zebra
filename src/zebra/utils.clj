(ns zebra.utils
  (:require [clojure.walk :refer [stringify-keys
                                  keywordize-keys]]))

(defn transform-params [params]
  (stringify-keys params))

(defn transform-type-data [type-data]
  (keywordize-keys (into {} type-data)))
