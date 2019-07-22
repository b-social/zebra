(ns zebra.core
  (:require [zebra.charges :as charges]
            [zebra.customers :as customers]
            [zebra.sources :as sources]
            [zebra.ephemeral-keys :as ephemeral-keys]
            [zebra.payment-methods :as payment-methods]
            [zebra.payment-intents :as payment-intents]))

;Charges

(defn create-charge [params api-key]
  (charges/create params api-key))

(defn retrieve-charge [id api-key]
  (charges/retrieve id api-key))

(def charge-status-codes charges/status-codes)

;Customers

(defn create-customer [api-key]
  (customers/create api-key))

(defn retrieve-customer [id api-key]
  (customers/retrieve id api-key))

(defn attach-source-to-customer [customer-id source-id api-key]
  (customers/attach-source customer-id source-id api-key))

;Sources

(defn create-source [params api-key]
  (sources/create params api-key))

(defn retrieve-source [id api-key]
  (sources/retrieve id api-key))

(def three-d-secure-requirements sources/three-d-secure-requirements)

(def source-status-codes sources/status-codes)

;Ephemeral Keys

(defn create-ephemeral-key [params api-version api-key]
  (ephemeral-keys/create params api-version api-key))

;Payment Methods

(defn create-payment-method [params api-key]
  (payment-methods/create params api-key))

;Payment Intents

(defn create-payment-intent [params api-key]
  (payment-intents/create params api-key))

(defn retrieve-payment-intent [id api-key]
  (payment-intents/retrieve id api-key))
