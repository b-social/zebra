(ns zebra.ephemeral-keys
  (:refer-clojure :exclude [list update])
  (:require [zebra.utils :refer [transform-params
                                 transform-type-data]])
  (:import [com.stripe.model EphemeralKey EphemeralKey$AssociatedObject]
           [com.stripe.net RequestOptions]
           [java.util Map]))

(defn associated-object->map [^EphemeralKey$AssociatedObject object]
  {:id   (.getId object)
   :type (.getType object)})

(defn ephemeral-key->map [^EphemeralKey source]
  {:id                 (.getId source)
   :object             (.getObject source)
   :associated-objects (map associated-object->map
                         (.getAssociatedObjects source))
   :created            (.getCreated source)
   :expires            (.getExpires source)
   :livemode           (.getLivemode source)
   :secret             (.getSecret source)})

(defn create
  [params api-version api-key]
  (ephemeral-key->map
    (EphemeralKey/create ^Map (transform-params params)
      (-> (RequestOptions/builder)
        (.setApiKey api-key)
        (.setStripeVersionOverride api-version)
        .build))))
