(ns zebra.customers
  (:refer-clojure :exclude [list update])
  (:require [zebra.sources :refer [source->map]])
  (:import [com.stripe.model Customer]
           [com.stripe.net RequestOptions]))

(defn customer->map [customer]
  {:id       (.getId customer)
   :metadata (.getMetadata customer)
   :sources  (.getSources customer)})

(defn create
  ([api-key metadata]
   (customer->map
     (Customer/create {"metadata" metadata}
       (->
         (RequestOptions/builder)
         (.setApiKey api-key)
         .build))))
  ([api-key]
   (create api-key {})))

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
