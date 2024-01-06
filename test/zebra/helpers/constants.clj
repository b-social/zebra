(ns zebra.helpers.constants
  (:require
    [clojure.test :refer :all]))

(def api-key "sk_test_7GJV4OR48SPEoZgndbJjpU8s")

(def tokens
  {:valid-token                  "tok_visa"
   :three-d-secure-required      "tok_threeDSecureRequired"
   :three-d-secure-not-supported "tok_amex_threeDSecureNotSupported"})
