(ns zebra.invoices
  (:import
    (com.stripe.model
      Invoice)
    (com.stripe.net
      RequestOptions)
    (java.util
      Map)))

(defn invoice->map
  [^Invoice invoice]
  {:id       (.getId invoice)
   :customer (.getCustomer invoice)
   :metadata (.getMetadata invoice)})

(defn create
  ([^Map params api-key]
   (invoice->map
     (Invoice/create
       params
       (-> (RequestOptions/builder)
           (.setApiKey api-key)
           .build))))
  ([api-key]
   (create {} api-key)))

(defn retrieve
  [id api-key]
  (invoice->map
    (Invoice/retrieve
      id
      (-> (RequestOptions/builder)
          (.setApiKey api-key)
          .build))))
