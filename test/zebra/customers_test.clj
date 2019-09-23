(ns zebra.customers-test
  (:require [clojure.test :refer :all]
            [zebra.customers :as customers]
            [zebra.sources :as sources]
            [zebra.helpers.constants :refer [api-key tokens]]))


(deftest create-customer
  (let [customer (customers/create api-key)]
    (testing "should be a valid customer"
      (is (some? (:id customer))))))

(deftest create-customer-with-metadata
  (let [key "some-field"
        value "some value"
        customer (customers/create api-key {key value})]
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
