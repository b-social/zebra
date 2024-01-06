(ns zebra.payment-intents
  (:refer-clojure :exclude [update])
  (:require
    [clojure.walk :refer [keywordize-keys]]
    [zebra.charges :as charges]
    [zebra.utils :refer [transform-params]])
  (:import
    (com.stripe.model
      PaymentIntent
      PaymentIntent$NextAction)
    (com.stripe.net
      RequestOptions)
    (java.util
      Map)))

(defn next-action->map
  [^PaymentIntent$NextAction next-action]
  (merge
    {:type (.getType next-action)}
    (when-let [redirect-to-url (.getRedirectToUrl next-action)]
      {:redirect_to_url {:return_url (.getReturnUrl redirect-to-url)
                         :url        (.getUrl redirect-to-url)}})))

(defn payment-intent->map
  [^PaymentIntent intent]
  (merge
    {:id (.getId intent)
     :customer (.getCustomer intent)
     :object (.getObject intent)
     :status (.getStatus intent)
     :charges (map charges/charge->map
                   (when
                     (.getCharges intent)
                     (.getData (.getCharges intent))))
     :description (.getDescription intent)
     :statement_descriptor (.getStatementDescriptor intent)
     :confirmation_method (.getConfirmationMethod intent)
     :payment_method_types (into [] (.getPaymentMethodTypes intent))
     :amount (.getAmount intent)
     :amount_capturable (.getAmountCapturable intent)
     :amount_received (.getAmountReceived intent)
     :currency (.getCurrency intent)
     :payment_method (.getPaymentMethod intent)
     :client_secret (.getClientSecret intent)
     :capture_method (.getCaptureMethod intent)
     :metadata (keywordize-keys
                 (into {} (.getMetadata intent)))}
    (when-let [next-action (.getNextAction intent)]
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
