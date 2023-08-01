(ns zebra.refunds
  (:import
    (com.stripe.model
      Refund)
    (com.stripe.net
      RequestOptions)
    (java.util
      Map)))

(defn refund->map
  [^Refund refund]
  {:id             (.getId refund)
   :payment_intent (.getPaymentIntent refund)
   :metadata       (.getMetadata refund)
   :amount         (.getAmount refund)
   :status         (.getStatus refund)
   :reason         (.getReason refund)})

(defn create
  ([^Map params api-key]
   (refund->map
     (Refund/create
       params
       (-> (RequestOptions/builder)
           (.setApiKey api-key)
           .build))))
  ([api-key]
   (create {} api-key)))

(defn retrieve
  [id api-key]
  (refund->map
    (Refund/retrieve
      id
      (-> (RequestOptions/builder)
          (.setApiKey api-key)
          .build))))
