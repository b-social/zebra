(ns zebra.test-utils
  (:require [zebra.core :as core])
  (:import [com.stripe Stripe]))

(defn override-api-base [api-base]
  (Stripe/overrideApiBase api-base))

(defmacro with-api-base
  [api-base & body]
  `(let [original-api-base# (core/get-api-base)]
     (try
       (override-api-base ~api-base)
       ~@body
       (finally
         (override-api-base original-api-base#)))))
