(ns zebra.core-test
  (:require
    [clojure.test :refer :all]
    [zebra.core :as core]))

(deftest get-api-base
  (let [expected-api-base "https://api.stripe.com"]
    (is (= (core/get-api-base) expected-api-base))))
