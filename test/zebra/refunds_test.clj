(ns zebra.refunds-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [zebra.customers :as customers]
    [zebra.helpers.constants :refer [api-key]]
    [zebra.payment-intents :as payment-intents]
    [zebra.payment-methods :as payment-methods]
    [zebra.refunds :as refunds]))

(deftest create-refund
  (let [key "some-field"
        value "some value"
        customer (customers/create api-key)
        payment-method (payment-methods/create
                         {:type "card"
                          :card {:number    "4242424242424242"
                                 :exp_month "7"
                                 :exp_year  "2026"
                                 :cvc       "314"}} api-key)
        payment-intent (payment-intents/create
                         {:amount               2000
                          :currency             "usd"
                          :payment_method_types ["card"]
                          :payment_method       (:id payment-method)
                          :customer (:id customer)}
                         api-key)
        confirmed-intent (payment-intents/confirm (:id payment-intent) api-key)
        refund (refunds/create
                 {"payment_intent" (:id confirmed-intent)
                  "metadata" {key value}}
                 api-key)]

    (testing "should be a valid refund"
      (is (some? (:id refund)))
      (testing "with valid metadata"
        (is (= value (get-in refund [:metadata key])))))

    (testing "can then retrieve the refund"
      (let [retrieved-refund (refunds/retrieve (:id refund) api-key)]
        (testing "should still be a valid refund"
          (is (some? (:id retrieved-refund))))
        (testing "with valid metadata"
          (is (= value (get-in refund [:metadata key]))))))

    (testing "charge on payment intent shows as refunded"
      (let [refunded-payment-intent (payment-intents/retrieve (:id payment-intent) api-key)]
        (testing "charge on payment intent should have status of refunded"
          (is (:refunded (first (:charges refunded-payment-intent)))))))))
