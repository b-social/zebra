(ns zebra.customers
  (:refer-clojure :exclude [list update])
  (:require
    [zebra.payment-methods :refer [payment-method->map]]
    [zebra.sources :refer [source->map]])
  (:import
    (com.stripe.model
      Customer
      PaymentMethod)
    (com.stripe.net
      RequestOptions)
    (java.util
      Map)))

(defn customer->map
  [^Customer customer]
  {:id       (.getId customer)
   :metadata (.getMetadata customer)
   :sources  (.getSources customer)})

(defn create
  ([^Map params api-key]
   (customer->map
     (Customer/create params
                      (->
                        (RequestOptions/builder)
                        (.setApiKey api-key)
                        .build))))
  ([api-key]
   (create {} api-key)))

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

(defn attach-payment-method
  [customer-id payment-method-id api-key]
  (let [^RequestOptions request (-> (RequestOptions/builder) (.setApiKey api-key) .build)
        pm (PaymentMethod/retrieve payment-method-id request)]
    (payment-method->map
      (.attach pm {"customer" customer-id} request))))
