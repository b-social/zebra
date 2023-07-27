(ns zebra.core
  (:require
    [zebra.charges :as charges]
    [zebra.customers :as customers]
    [zebra.ephemeral-keys :as ephemeral-keys]
    [zebra.invoices :as invoices]
    [zebra.payment-intents :as payment-intents]
    [zebra.payment-methods :as payment-methods]
    [zebra.sources :as sources])
  (:import
    (com.stripe
      Stripe)))

;; Stripe

(defn get-api-base
  []
  (Stripe/getApiBase))

;; Charges

(defn create-charge
  [params api-key]
  (charges/create params api-key))

(defn retrieve-charge
  [id api-key]
  (charges/retrieve id api-key))

(def charge-status-codes charges/status-codes)

;; Customers

(defn create-customer
  ([params api-key]
   (customers/create params api-key))
  ([api-key]
   (create-customer {} api-key)))

(defn retrieve-customer
  [id api-key]
  (customers/retrieve id api-key))

(defn attach-source-to-customer
  [customer-id source-id api-key]
  (customers/attach-source customer-id source-id api-key))

(defn attach-payment-method-to-customer
  [customer-id payment-method-id api-key]
  (customers/attach-payment-method customer-id payment-method-id api-key))

;; Sources

(defn create-source
  [params api-key]
  (sources/create params api-key))

(defn retrieve-source
  [id api-key]
  (sources/retrieve id api-key))

(def three-d-secure-requirements sources/three-d-secure-requirements)

(def source-status-codes sources/status-codes)

;; Ephemeral Keys

(defn create-ephemeral-key
  [params api-version api-key]
  (ephemeral-keys/create params api-version api-key))

;; Payment Methods

(defn create-payment-method
  [params api-key]
  (payment-methods/create params api-key))

(defn retrieve-payment-method
  [id api-key]
  (payment-methods/retrieve id api-key))

;; Payment Intents

(defn create-payment-intent
  [params api-key]
  (payment-intents/create params api-key))

(defn retrieve-payment-intent
  [id api-key]
  (payment-intents/retrieve id api-key))

(defn update-payment-intent
  [id params api-key]
  (payment-intents/update id params api-key))

(defn capture-payment-intent
  [id api-key]
  (payment-intents/capture id api-key))

(defn confirm-payment-intent
  [id api-key]
  (payment-intents/confirm id api-key))

;; Invoices

(defn create-invoice
  ([params api-key]
   (invoices/create params api-key))
  ([api-key]
   (create-invoice {} api-key)))

(defn retrieve-invoice
  [id api-key]
  (invoices/retrieve id api-key))
