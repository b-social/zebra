(ns zebra.ephemeral-keys-test
  (:require
    [clojure.test :refer :all]
    [zebra.customers :as customers]
    [zebra.ephemeral-keys :as ephemeral-keys]
    [zebra.helpers.constants :refer [api-key]]))

(deftest create-source
  (let [customer (customers/create api-key)
        api-version "2018-05-21"
        ephemeral-key (ephemeral-keys/create
                        {:customer (:id customer)}
                        api-version
                        api-key)]
    (testing "should be a valid ephemeral key"
      (is (some? (:id ephemeral-key)))
      (is (= "ephemeral_key" (:object ephemeral-key)))
      (is (some
            #(and (= "customer" (:type %)) (= (:id customer) (:id %)))
            (:associated-objects ephemeral-key)))
      (is (some? (:created ephemeral-key)))
      (is (some? (:expires ephemeral-key)))
      (is (false? (:livemode ephemeral-key)))
      (is (some? (:secret ephemeral-key))))))
