(ns zebra.test-utils-test
  (:require
   [clojure.test :refer :all]
   [zebra.test-utils :as test-utils]
   [zebra.core :as core]))

(deftest with-api-base
  (let [original-api-base "https://api.stripe.com"
        overridden-api-base "https://example.com"]
    (testing "should have original api-base"
      (is (= (core/get-api-base) original-api-base)))
    (test-utils/with-api-base overridden-api-base
      (testing "should have overridden api-base"
        (is (= (core/get-api-base) overridden-api-base))))
    (testing "should again have original api-base"
      (is (= (core/get-api-base) original-api-base)))))

(deftest with-api-base-on-exception
  (let [original-api-base "https://api.stripe.com"
        overridden-api-base "https://example.com"]
    (testing "should have original api-base"
      (is (= (core/get-api-base) original-api-base)))
    (test-utils/with-api-base overridden-api-base
      (throw (ex-info "Oh no!" {})))
    (testing "should again have original api-base"
      (is (= (core/get-api-base) original-api-base)))))
