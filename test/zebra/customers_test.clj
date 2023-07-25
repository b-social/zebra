(ns zebra.customers-test
  (:require
    [clojure.test :refer :all]
    [zebra.customers :as customers]
    [zebra.helpers.constants :refer [api-key tokens]]
    [zebra.payment-methods :as payment-methods]
    [zebra.sources :as sources])
  (:import
    (com.stripe.model
      PaymentMethod)))

(deftest create-customer
  (let [customer (customers/create api-key)]
    (testing "should be a valid customer"
      (is (some? (:id customer))))))

(deftest create-customer-with-metadata
  (let [key "some-field"
        value "some value"
        customer (customers/create {"metadata" {key value}} api-key)]
    (testing "should be a valid customer"
      (is (some? (:id customer)))
      (is (= value (get-in customer [:metadata key]))))))

(deftest retrieve-customer
  (let [customer (customers/create api-key)
        retrieved-customer (customers/retrieve (:id customer) api-key)]
    (testing "should retrieve a created customer"
      (is (some? (:id retrieved-customer))))))

(deftest attach-source
  (let [customer (customers/create api-key)
        source (sources/create {:type  "card"
                                :token (:valid-token tokens)} api-key)
        attached-source (customers/attach-source
                          (:id customer)
                          (:id source)
                          api-key)]
    (testing "should attach a source to a customer"
      (is (= (:id customer) (:customer attached-source))))))

(deftest attach-payment-method
  (let [customer (customers/create api-key)
        payment-method (payment-methods/create {:type "card"
                                                :card {:number    "4242424242424242"
                                                       :exp_month "7"
                                                       :exp_year  "2026"
                                                       :cvc       "314"}} api-key)
        attached-payment-method (customers/attach-payment-method
                                  (:id customer)
                                  (:id payment-method)
                                  api-key)
        ^PaymentMethod pm (-> attached-payment-method
                              meta
                              :original)]
    (testing "should attach a payment method to a customer"
      (is (= (:id customer) (.getCustomer pm))))))
