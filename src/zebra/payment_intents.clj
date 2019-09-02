(ns zebra.payment-intents
  (:refer-clojure :exclude [update])
  (:require [zebra.utils :refer [transform-params
                                 transform-type-data]])
  (:import [com.stripe.model PaymentIntent
                             PaymentIntent$NextAction
                             PaymentIntent$PaymentMethodOptions]
           [com.stripe.net RequestOptions]
           [java.util Map]))

(defn next-action->map [^PaymentIntent$NextAction next-action]
  (merge
    {:type (.getType next-action)}
    (when-let [redirect-to-url (.getRedirectToUrl next-action)]
      {:redirect_to_url {:return_url (.getReturnUrl redirect-to-url)
                         :url        (.getUrl redirect-to-url)}})))

(defn payment-method-options->map [^PaymentIntent$PaymentMethodOptions options]
  (merge {}
    (when-let [card (.getCard options)]
      {:request_three_d_secure (.getRequestThreeDSecure card)})))

(defn payment-intent->map [x]
  (merge
    {:id                     (.getId x)
     :object                 (.getObject x)
     :amount                 (.getAmount x)
     :amount_capturable      (.getAmountCapturable x)
     :amount_received        (.getAmountReceived x)
     :application            (.getApplication x)
     :application_fee_amount (.getApplicationFeeAmount x)
     :canceled_at            (.getCanceledAt x)
     :cancellation_reason    (.getCancellationReason x)
     :capture_method         (.getCaptureMethod x)
     ;:charges
     :client_secret          (.getClientSecret x)
     :confirmation_method    (.getConfirmationMethod x)
     :created                (.getCreated x)
     :currency               (.getCurrency x)
     :description            (.getDescription x)
     :invoice                (.getInvoice x)
     :livemode               (.getLivemode x)
     :metadata               (.getMetadata x)
     :on_behalf_of           (.getOnBehalfOf x)
     :payment_method         (.getPaymentMethod x)
     :payment_method_options (payment-method-options->map
                               (.getPaymentMethodOptions x))
     :payment_method_types   (into [] (.getPaymentMethodTypes x))
     :receipt_email          (.getReceiptEmail x)
     :review                 (.getReview x)
     :setup_future_usage     (.getSetupFutureUsage x)
     :shipping               (when-let [shipping (.getShipping x)]
                               shipping)
     :statement_descriptor   (.getStatementDescriptor x)
     :status                 (.getStatus x)
     :transfer_data          (when-let [transfer-data (.getTransferData x)]
                               transfer-data)
     :transfer_group         (.getTransferGroup x)}
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
