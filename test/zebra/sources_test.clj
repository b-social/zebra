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

(deftest create-three-d-secure-source
  (let [card-source (sources/create
                      {:type  "card"
                       :token (:valid-token tokens)}
                      api-key)
        three-d-secure-source (sources/create
                                {:type           "three_d_secure"
                                 :three_d_secure {:card (:id card-source)}
                                 :redirect {:return_url "https://shop.example.com/crtA6B28E1"}
                                 :amount         100
                                 :currency       "gbp"}
                                api-key)]
    (testing "should be a valid source"
      (is (some? (:id three-d-secure-source)))
      (is (map? (:type-data three-d-secure-source))))))

(deftest retrieve-source
  (let [source (sources/create {:type  "card"
                                :token (:valid-token tokens)} api-key)
        retrieved-source (sources/retrieve (:id source) api-key)]
    (testing "should retrieve a created source"
      (is (some? (:id retrieved-source))))))
