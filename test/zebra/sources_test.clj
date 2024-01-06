(ns zebra.sources-test
  (:require
    [clojure.test :refer :all]
    [zebra.helpers.constants :refer [api-key tokens]]
    [zebra.sources :as sources])
  (:import
    (com.stripe.exception
      InvalidRequestException)))

(deftest create-source
  (let [source (sources/create {:type  "card"
                                :token (:valid-token tokens)} api-key)]
    (testing "should be a valid source"
      (is (some? (:id source)))
      (is (map? (:type-data source))))))

(deftest create-three-d-secure-source
  (let [card-source (sources/create
                      {:type  "card"
                       :token (:three-d-secure-required tokens)}
                      api-key)

        three-d-secure-source
        (sources/create
          {:type           "three_d_secure"
           :three_d_secure {:card (:id card-source)}
           :redirect       {:return_url "https://some-url.com"}
           :amount         100
           :currency       "gbp"}
          api-key)]
    (testing "should be a valid source"
      (is (some? (:id three-d-secure-source)))
      (is (= (:status three-d-secure-source)
             (:pending sources/status-codes))))))

(deftest error-creating-three-d-secure-source
  (let [card-source (sources/create
                      {:type  "card"
                       :token (:three-d-secure-required tokens)}
                      api-key)]
    (testing "should raise a zebra exception"
      (is (thrown-with-msg?
            InvalidRequestException
            #"Missing required param: redirect.; code: parameter_missing"
            (sources/create
              {:type           "three_d_secure"
               :three_d_secure {:card (:id card-source)}
               :amount         100
               :currency       "gbp"}
              api-key))))))

(deftest retrieve-source
  (let [source (sources/create {:type  "card"
                                :token (:valid-token tokens)} api-key)
        retrieved-source (sources/retrieve (:id source) api-key)]
    (testing "should retrieve a created source"
      (is (some? (:id retrieved-source))))))
