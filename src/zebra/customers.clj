(ns zebra.customers
  (:refer-clojure :exclude [list update])
  (:require [zebra.sources :as sources])
  (:import [com.stripe.model Customer]
           [com.stripe.net RequestOptions]))

(defn api-key->request-options
  [api-key]
  (-> (RequestOptions/builder)
    (.setApiKey api-key)
    .build))

(defn customer->map [customer]
  {:id      (.getId customer)
   :sources (.getSources customer)})

(defn create
  [api-key]
  (customer->map (Customer/create {} (api-key->request-options api-key))))

(defn retrieve
  [id api-key]
  (customer->map (Customer/retrieve id (api-key->request-options api-key))))

(defn attach-source
  [customer-id source-id api-key]
  (-> customer-id
    (retrieve api-key)
    :sources
    (.create {"source" source-id} (api-key->request-options api-key))
    sources/source->map))
