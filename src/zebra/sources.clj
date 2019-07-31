(ns zebra.sources
  (:refer-clojure :exclude [update])
  (:require [zebra.utils :refer [transform-params
                                 transform-type-data]])
  (:import [com.stripe.model Source Source$Card]
           [com.stripe.net RequestOptions]
           [java.util Map]))

(def three-d-secure-requirements
  {:required      "required"
   :recommended   "recommended"
   :optional      "optional"
   :not-supported "not_supported"})

(def status-codes
  {:pending    "pending"
   :chargeable "chargeable"
   :failed     "failed"})

(defn- card-source->map [^Source$Card card]
  {:address_line1_check (.getAddressLine1Check card)
   :address_zip_check   (.getAddressZipCheck card)
   :brand               (.getBrand card)
   :country             (.getCountry card)
   :cvc_check           (.getCvcCheck card)
   :dynamic_last4       (.getDynamicLast4 card)
   :exp_month           (.getExpMonth card)
   :exp_year            (.getExpYear card)
   :fingerprint         (.getFingerprint card)
   :funding             (.getFunding card)
   :last4               (.getLast4 card)
   :name                (.getName card)
   :three_d_secure      (.getThreeDSecure card)
   :tokenization_method (.getTokenizationMethod card)})

(defn source->map [source]
  (merge
    {:id       (.getId source)
     :customer (.getCustomer source)
     :status   (.getStatus source)
     :type     (.getType source)}
    (when (= "card" (.getType source))
      (let [card (card-source->map (.getCard source))]
        ;; TODO: deprecate `type-data`, it does not match API.
        {:type-data card
         :card      card}))
    (when-let [redirect (.getRedirect source)]
      {:redirect {:url            (.getUrl redirect)
                  :return-url     (.getReturnUrl redirect)
                  :status         (.getStatus redirect)
                  :failure-reason (.getFailureReason redirect)}})))

(defn create
  [params api-key]
  (source->map
    (Source/create ^Map (transform-params params)
      (-> (RequestOptions/builder) (.setApiKey api-key) .build))))

(defn create-old
  [params api-key]
  (try
    ;; TODO: Why does this catch the exception?
    (source->map
      (Source/create ^Map (transform-params params)
        (-> (RequestOptions/builder) (.setApiKey api-key) .build)))
    (catch Exception e
      (throw (ex-info "Failed to create stripe source" {:error e})))))

(defn retrieve
  [id api-key]
  (source->map
    (Source/retrieve id
      (-> (RequestOptions/builder) (.setApiKey api-key) .build))))
