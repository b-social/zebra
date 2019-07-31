(ns zebra.customers
  (:refer-clojure :exclude [list update])
  (:require [zebra.sources :refer [source->map]])
  (:import [com.stripe.model Customer]
           [com.stripe.net RequestOptions]))

(defn customer->map [customer]
  {:id      (.getId customer)
   :sources (.getSources customer)})

(defn create
  [api-key]
  (customer->map
    (Customer/create {}
      (-> (RequestOptions/builder) (.setApiKey api-key) .build))))

(defn retrieve
  [id api-key]
  (customer->map
    (Customer/retrieve id
      (-> (RequestOptions/builder) (.setApiKey api-key) .build))))

(defn attach-source
  [customer-id source-id api-key]
  (let [opts (-> (RequestOptions/builder) (.setApiKey api-key) .build)
        customer (Customer/retrieve customer-id opts)
        sources (.getSources customer)]
    (source->map
      (.create sources {"source" source-id}
        (-> (RequestOptions/builder) (.setApiKey api-key) .build)))))
