(ns zebra.payment-methods-test
  (:require [clojure.test :refer :all]
            [zebra.payment-methods :refer [create retrieve]]
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
               [:brand :exp_month :exp_year :funding :last4 :three_d_secure_usage])))))

(deftest retrieve-payment-method
  (let [payment-method (create
                         {:type "card"
                          :card {:number "4242424242424242"
                                 :exp_month "7"
                                 :exp_year "2020"
                                 :cvc "314"}} api-key)
        payment-method2 (retrieve (:id payment-method) api-key)]

    (testing "should retrieve payment-method"
      (is (= (:id payment-method2) (:id payment-method))))))

(deftest retrieve-payment-method-includes-card-details
  (let [payment-method (retrieve "pm_card_threeDSecure2Required" api-key)
        card (:card payment-method)]

    (testing "should retrieve payment-method"
      (is (= true (-> card :three_d_secure_usage :supported)))
      (is (= "visa" (-> card :brand)))
      (is (= "credit" (-> card :funding)))
      (is (= "3220" (-> card :last4))))))
