(ns zebra.payment-methods-test
  (:require [clojure.test :refer :all]
            [zebra.payment-methods :refer [create]]
            [zebra.helpers.constants :refer [api-key tokens]]))

(deftest create-payment-method
     (let [payment-method (create
                            {:type "card"
                             :card {:number "4242424242424242"
                                    :exp_month "7"
                                    :exp_year "2020"
                                    :cvc "314"}} api-key)]

       (testing "should create a valid payment method"
         (is (= (:object payment-method) "payment_method"))
         (is (some? (:id payment-method)))
         (is (= (keys (:card payment-method))
               [:brand :exp_month :exp_year])))))
