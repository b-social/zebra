(ns zebra.sources-test
  (:require [clojure.test :refer :all]
            [zebra.sources :as sources]
            [zebra.helpers.constants :refer [api-key tokens]]))

(deftest create-source
  (let [source (sources/create {:type  "card"
                                :token (:valid-token tokens)} api-key)]
    (testing "should be a valid source"
      (is (some? (:id source)))
      (is (map? (:type-data source))))))

(deftest retrieve-source
  (let [source (sources/create {:type  "card"
                                :token (:valid-token tokens)} api-key)
        retrieved-source (sources/retrieve (:id source) api-key)]
    (testing "should retrieve a created source"
      (is (some? (:id retrieved-source))))))
