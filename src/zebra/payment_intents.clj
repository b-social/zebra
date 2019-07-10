(ns zebra.payment-intents
  (:require [zebra.utils :refer [transform-params
                                 transform-type-data]])
  (:import [com.stripe.model PaymentIntent]
           [com.stripe.net RequestOptions]))

(defn payment-intent->map [x]
  {:id                   (.getId x)
   :object               (.getObject x)
   :status               (.getStatus x)
   :confirmation_method  (.getConfirmationMethod x)
   :payment_method_types (into [] (.getPaymentMethodTypes x))
   :amount               (.getAmount x)
   :currency (.getCurrency x)
   :payment_method (.getPaymentMethod x)})

(defn create
  [params api-key]
  (payment-intent->map
    (PaymentIntent/create (transform-params params)
      (-> (RequestOptions/builder) (.setApiKey api-key) .build))))