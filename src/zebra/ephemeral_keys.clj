(ns zebra.ephemeral-keys
  (:refer-clojure :exclude [list update])
  (:require [zebra.utils :refer [transform-params
                                 transform-type-data]])
  (:import [com.stripe.model EphemeralKey]
           [com.stripe.net RequestOptions]))

(defn api-key->request-options
  [api-key api-version]
  (-> (RequestOptions/builder)
    (.setApiKey api-key)
    (.setStripeVersion api-version)
    .build))

(defn ephemeral-key->map [source]
  {:id       (.getId source)
   :expires  (.getExpires source)
   :raw-json (.getRawJson source)})

(defn create
  [params api-version api-key]
  (ephemeral-key->map (EphemeralKey/create
                        (transform-params params)
                        (api-key->request-options api-key api-version))))
