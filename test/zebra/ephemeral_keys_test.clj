(ns zebra.ephemeral-keys-test
  (:require [clojure.test :refer :all]
            [zebra.ephemeral-keys :as ephemeral-keys]
            [zebra.customers :as customers]
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
      (is (some? (:raw-json ephemeral-key))))))
