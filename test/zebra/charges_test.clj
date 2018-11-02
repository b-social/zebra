(ns zebra.charges-test
  (:require [clojure.test :refer :all])
  (:require [zebra.charges :as charges]
            [zebra.customers :as customers]
            [zebra.sources :as sources]
            [zebra.helpers.constants :refer [api-key]]))

(deftest create-charge
  (let [customer (customers/create api-key)
        source (sources/create {"type" "card" "token" "tok_visa"} api-key)
        attached-source (customers/attach-source
                          (:id customer)
                          (:id source) api-key)
        charge (charges/create {"amount"      100
                                "currency"    "gbp"
                                "description" "test charge"
                                "customer"    (:id customer)
                                "source"      (:id attached-source)} api-key)]
    (testing "should create a valid charge"
      (is (some? (:id charge)))
      (is (= (:status charge) (:succeeded charges/status-codes))))))

(deftest retrieve-charge
  (let [customer (customers/create api-key)
        source (sources/create {"type" "card" "token" "tok_visa"} api-key)
        attached-source (customers/attach-source
                          (:id customer)
                          (:id source) api-key)
        charge (charges/create {"amount"      100
                                "currency"    "gbp"
                                "description" "test charge"
                                "customer"    (:id customer)
                                "source"      (:id attached-source)} api-key)
        retrieved-charge (charges/retrieve (:id charge) api-key)]
    (testing "should retrieve a created charge"
      (is (some? (:id retrieved-charge)))
      (is (= (:status retrieved-charge) (:succeeded charges/status-codes))))))
