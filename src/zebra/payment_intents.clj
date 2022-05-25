(ns zebra.payment-intents
  (:refer-clojure :exclude [update])
  (:require [zebra.utils :refer [transform-params]]
            [clojure.walk :refer [keywordize-keys]])
  (:import [com.stripe.model PaymentIntent PaymentIntent$NextAction]
           [com.stripe.net RequestOptions]
           [java.util Map]))


(defn next-action->map [^PaymentIntent$NextAction next-action]
  (merge
    {:type (.getType next-action)}
    (when-let [redirect-to-url (.getRedirectToUrl next-action)]
      {:redirect_to_url {:return_url (.getReturnUrl redirect-to-url)
                         :url        (.getUrl redirect-to-url)}})))

(defn payment-intent->map [^PaymentIntent x]
  (merge
    {:id                   (.getId x)
     :customer             (.getCustomer x)
     :object               (.getObject x)
     :status               (.getStatus x)
     :description          (.getDescription x)
     :statement_descriptor (.getStatementDescriptor x)
     :confirmation_method   (.getConfirmationMethod x)
     :payment_method_types (into [] (.getPaymentMethodTypes x))
     :amount               (.getAmount x)
     :amount_capturable    (.getAmountCapturable x)
     :amount_received      (.getAmountReceived x)
     :currency             (.getCurrency x)
     :payment_method       (.getPaymentMethod x)
     :client_secret        (.getClientSecret x)
     :capture_method       (.getCaptureMethod x)
     :metadata             (clojure.walk/keywordize-keys
                             (into {} (.getMetadata x)))}
    (when-let [next-action (.getNextAction x)]
      {:next_action
       (next-action->map next-action)})))

(defn create
  [params api-key]
  (payment-intent->map
    (PaymentIntent/create ^Map (transform-params params)
      (-> (RequestOptions/builder) (.setApiKey api-key) .build))))

(defn retrieve
  [id api-key]
  (payment-intent->map
    (PaymentIntent/retrieve id
      (-> (RequestOptions/builder) (.setApiKey api-key) .build))))

(defn update
  [id params api-key]
  (let [opts (-> (RequestOptions/builder) (.setApiKey api-key) .build)
        payment-intent (PaymentIntent/retrieve id opts)]
    (payment-intent->map
      (.update payment-intent ^Map (transform-params params) opts))))

(defn capture
  [id api-key]
  (let [opts (-> (RequestOptions/builder) (.setApiKey api-key) .build)
        payment-intent
        (PaymentIntent/retrieve id opts)]
    (payment-intent->map (.capture payment-intent opts))))

(defn confirm
  [id api-key]
  (let [opts (-> (RequestOptions/builder) (.setApiKey api-key) .build)
        payment-intent (PaymentIntent/retrieve id opts)]
    (payment-intent->map
      (.confirm payment-intent {} opts))))
