(ns zebra.payment-methods
  (:require [zebra.utils :refer [transform-params
                                 transform-type-data]])
  (:import [com.stripe.model PaymentMethod]
           [com.stripe.net RequestOptions]
           [java.util Map]))

(defn payment-method->map [^PaymentMethod x]
  (merge
    {:id     (.getId x)
     :object (.getObject x)}
    (when-let [card (.getCard x)]
      {:card {:brand     (.getBrand card)
              :exp_month (.getExpMonth card)
              :exp_year  (.getExpYear card)
              :funding   (.getFunding card)
              :last4     (.getLast4 card)
              :three_d_secure_usage
                         {:supported
                          (-> card .getThreeDSecureUsage .getSupported)}}})))

(defn create
  [params api-key]
  (payment-method->map
    (PaymentMethod/create ^Map (transform-params params)
      (-> (RequestOptions/builder) (.setApiKey api-key) .build))))

(defn retrieve
  [id api-key]
  (payment-method->map
    (PaymentMethod/retrieve id
      (-> (RequestOptions/builder) (.setApiKey api-key) .build))))
