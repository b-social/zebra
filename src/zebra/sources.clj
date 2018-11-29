(ns zebra.sources
  (:refer-clojure :exclude [list update])
  (:require [zebra.utils :refer [transform-params
                                 transform-type-data]])
  (:import [com.stripe.model Source]
           [com.stripe.net RequestOptions]))

(def three-d-secure-requirements {:required      "required"
                                  :recommended   "recommended"
                                  :optional      "optional"
                                  :not-supported "not_supported"})

(def status-codes {:pending    "pending"
                   :chargeable "chargeable"
                   :failed     "failed"})

(defn api-key->request-options
  [api-key]
  (-> (RequestOptions/builder)
    (.setApiKey api-key)
    .build))

(defn source->map [source]
  (merge
    {:id        (.getId source)
     :customer  (.getCustomer source)
     :status    (.getStatus source)
     :type      (.getType source)
     :type-data (transform-type-data (.getTypeData source))}
    (when-let [redirect (.getRedirect source)]
      {:redirect {:url            (.getUrl redirect)
                  :return-url     (.getReturnUrl redirect)
                  :status         (.getStatus redirect)
                  :failure-reason (.getFailureReason redirect)}})))

(defn create
  [params api-key]
  (try
    (source->map (Source/create
                   (transform-params params)
                   (api-key->request-options api-key)))
    (catch Exception e
      (throw (ex-info "Failed to create stripe source" {:error e})))))

(defn retrieve
  [id api-key]
  (source->map (Source/retrieve
                 id
                 (api-key->request-options api-key))))
