(ns zebra.ephemeral-keys
  (:refer-clojure :exclude [list update])
  (:require [zebra.utils :refer [transform-params
                                 transform-type-data]])
  (:import [com.stripe.model EphemeralKey EphemeralKey$AssociatedObject]
           [com.stripe.net RequestOptions]))

(defn api-key->request-options
  [api-key api-version]
  (-> (RequestOptions/builder)
    (.setApiKey api-key)
    (.setStripeVersion api-version)
    .build))

(defn associated-object->map [^EphemeralKey$AssociatedObject object]
  {:id   (.getId object)
   :type (.getType object)})

(defn ephemeral-key->map [^EphemeralKey source]
  {:id                 (.getId source)
   :object             (.getObject source)
   :associated-objects (map
                         associated-object->map (.getAssociatedObjects source))
   :created            (.getCreated source)
   :expires            (.getExpires source)
   :livemode           (.getLivemode source)
   :secret             (.getSecret source)})

(defn create
  [params api-version api-key]
  (ephemeral-key->map (EphemeralKey/create
                        (transform-params params)
                        (api-key->request-options api-key api-version))))
