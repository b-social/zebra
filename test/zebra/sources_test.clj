(ns zebra.sources-test
  (:require [clojure.test :refer :all]
            [zebra.sources :as sources]
            [zebra.helpers.constants :refer [api-key]]))

(deftest create-source
  (let [source (sources/create {"type"  "card"
                                "token" "tok_visa"} api-key)]
    (testing "should be a valid source"
      (is (some? (:id source))))))

(deftest retrieve-source
  (let [source (sources/create {"type"  "card"
                                "token" "tok_visa"} api-key)
        retrieved-source (sources/retrieve (:id source) api-key)]
    (testing "should retrieve a created source"
      (is (some? (:id retrieved-source))))))
