(ns zebra.charges
  (:refer-clojure :exclude [list update])
  (:require [zebra.utils :refer [transform-params]])
  (:import [com.stripe.model Charge]
           [com.stripe.net RequestOptions]
           [java.util Map]))

(def status-codes {:succeeded "succeeded"
                   :pending   "pending"
                   :failed    "failed"})

(defn charge->map [charge]
  {:id     (.getId charge)
   :status (.getStatus charge)})

(defn create
  [params api-key]
  (charge->map
    (Charge/create
      ^Map (transform-params params)
      (-> (RequestOptions/builder) (.setApiKey api-key) .build))))

(defn retrieve
  [id api-key]
  (charge->map
    (Charge/retrieve id
      (-> (RequestOptions/builder) (.setApiKey api-key) .build))))
