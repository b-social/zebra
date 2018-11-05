(ns zebra.charges
  (:refer-clojure :exclude [list update])
  (:require [zebra.utils :refer [transform-params]])
  (:import [com.stripe.model Charge]
           [com.stripe.net RequestOptions]))

(def status-codes {:succeeded "succeeded"
                   :pending   "pending"
                   :failed    "failed"})

(defn api-key->request-options
  [api-key]
  (-> (RequestOptions/builder)
    (.setApiKey api-key)
    .build))

(defn charge->map [charge]
  {:id     (.getId charge)
   :status (.getStatus charge)})

(defn create
  [params api-key]
  (charge->map (Charge/create
                 (transform-params params)
                 (api-key->request-options api-key))))

(defn retrieve
  [id api-key]
  (charge->map (Charge/retrieve
                 id
                 (api-key->request-options api-key))))
