(ns zebra.payment-methods
  (:require [zebra.utils :refer [transform-params
                                 transform-type-data]])
  (:import [com.stripe.model PaymentMethod]
           [com.stripe.net RequestOptions]))

(defn payment-method->map [x]
  (merge
    {:id     (.getId x)
     :object (.getObject x)}
    (when-let [card (.getCard x)]
      {:card {:brand (.getBrand card)
              :exp_month (.getExpMonth card)
              :exp_year (.getExpYear card)}})))

(defn create
  [params api-key]
  (payment-method->map
    (PaymentMethod/create (transform-params params)
      (-> (RequestOptions/builder) (.setApiKey api-key) .build))))

(defn retrieve
  [id api-key]
  (payment-method->map
    (PaymentMethod/retrieve id nil
      (-> (RequestOptions/builder) (.setApiKey api-key) .build))))
