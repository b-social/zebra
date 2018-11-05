(ns zebra.core
  (:require [zebra.charges :as charges]
            [zebra.customers :as customers]
            [zebra.sources :as sources]))

;Charges

(defn create-charge [params api-key]
  (charges/create params api-key))

(defn retrieve-charge [id api-key]
  (charges/retrieve id api-key))

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
