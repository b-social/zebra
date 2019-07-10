(ns zebra.payment-intents-test
  (:require [clojure.test :refer :all]
            [clojure.string :as str]
            [zebra.payment-methods :as payment-methods]
            [zebra.payment-intents :as payment-intent]
            [zebra.helpers.constants :refer [api-key tokens]]))

(deftest create-payment-intent
  (let [payment-intent (payment-intent/create
                         {:amount               2000
                          :currency             "usd"
                          :payment_method_types ["card"]}
                         api-key)]

    (testing "should create a valid payment intent"
      (is (str/starts-with? (:id payment-intent) "pi_"))
      (is (= (:object payment-intent) "payment_intent")))))

(deftest create-and-confirm-payment-intent
  (let [payment-method (payment-methods/create
                         {:type "card"
                          :card {:number    "4242424242424242"
                                 :exp_month "7"
                                 :exp_year  "2020"
                                 :cvc       "314"}} api-key)
        confirmed-payment-intent (payment-intent/create
                                   {:amount               2000
                                    :currency             "usd"
                                    :payment_method_types ["card"]
                                    :confirm              true
                                    :confirmation_method  "manual"
                                    :payment_method       (:id payment-method)}
                                   api-key)]

    (testing "should create a valid payment intent"
      (is (str/starts-with? (:id confirmed-payment-intent) "pi_"))
      (is (= (:object confirmed-payment-intent) "payment_intent"))
      (is (= (:status confirmed-payment-intent) "succeeded"))
      (is (= (:confirmation_method confirmed-payment-intent) "manual"))
      (is (= (:payment_method_types confirmed-payment-intent) ["card"]))
      (is (vector? (:payment_method_types confirmed-payment-intent)))
      (is (= (:amount confirmed-payment-intent) 2000))
      (is (= (:currency confirmed-payment-intent) "usd"))
      (is (= (:payment_method confirmed-payment-intent)
            (:id payment-method))))))

(deftest create-and-confirm-payment-intent-3d-secure
  (let [payment-method (payment-methods/create
                         {:type "card"
                          :card {;; A 3D Secure 2 card
                                 :number    "4242424242424242"
                                 :exp_month "7"
                                 :exp_year  "2020"
                                 :cvc       "314"}} api-key)
        payment-intent (payment-intent/create
                                   {:amount               1234
                                    :currency             "gbp"
                                    :payment_method_types ["card"]
                                    :confirm              true
                                    :confirmation_method  "manual"
                                    :payment_method       (:id payment-method)}
                                   api-key)]

    (testing "should create a valid payment intent"
      (is (str/starts-with? (:id payment-intent) "pi_"))
      (is (= (:object payment-intent) "payment_intent"))
      ;(is (= (:status payment-intent) "requires_source_action"))
      (is (= (:confirmation_method payment-intent) "manual"))
      (is (= (:payment_method_types payment-intent) ["card"]))
      (is (vector? (:payment_method_types payment-intent)))
      (is (= (:amount payment-intent) 1234))
      (is (= (:currency payment-intent) "gbp"))
      (is (= (:payment_method payment-intent) (:id payment-method)))
      #_
      (let [next-action (:next_action payment-intent)]
        (is (= [:redirect_url :...] (keys next-action)))))))

