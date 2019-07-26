(ns zebra.payment-intents
  (:require [zebra.utils :refer [transform-params
                                 transform-type-data]])
  (:import [com.stripe.model PaymentIntent]
           [com.stripe.net RequestOptions]))

(defn payment-intent->map [x]
  (merge
    {:id                   (.getId x)
     :object               (.getObject x)
     :status               (.getStatus x)
     ;; XXX Does not exist in our version
     ;; :description          (.getDescription x)
     :statement_descriptor (.getStatementDescriptor x)
     :confirmation_method  (.getConfirmationMethod x)
     :payment_method_types (into [] (.getPaymentMethodTypes x))
     :amount               (.getAmount x)
     :currency             (.getCurrency x)
     :payment_method       (.getPaymentMethod x)}
    (when-let [next-action (.getNextAction x)]
      {:next_action
       (merge
         {:type (.getType next-action)}
         ;; TODO handle :use_stripe_sdk
         (when-let [redirect-to-url (.getRedirectToUrl next-action)]
           {:redirect_to_url {:return_url (.getReturnUrl redirect-to-url)
                              :url (.getUrl redirect-to-url)}}))})))

(defn create
  [params api-key]
  (payment-intent->map
    (PaymentIntent/create (transform-params params)
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
      (.update payment-intent (transform-params params) opts))))

(defn capture
  [id api-key]
  (let [opts (-> (RequestOptions/builder) (.setApiKey api-key) .build)
        payment-intent
        (PaymentIntent/retrieve id opts)]
    (payment-intent->map (.capture payment-intent nil opts))))
