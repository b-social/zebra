(ns zebra.sources
  (:refer-clojure :exclude [list update])

  (:import [com.stripe.model Source]
           [com.stripe.net RequestOptions]))

(defn api-key->request-options
  [api-key]
  (-> (RequestOptions/builder)
    (.setApiKey api-key)
    .build))

(defn source->map [source]
  {:id       (.getId source)
   :customer (.getCustomer source)
   :status   (.getStatus source)})

(defn create
  [params api-key]
  (source->map (Source/create params (api-key->request-options api-key))))

(defn retrieve
  [id api-key]
  (source->map (Source/retrieve id (api-key->request-options api-key))))
