(ns zebra.invoices-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [zebra.customers :as customers]
    [zebra.helpers.constants :refer [api-key]]
    [zebra.invoices :as invoices]))

(deftest create-invoice
  (let [key "some-field"
        value "some value"
        customer (customers/create api-key)
        invoice (invoices/create
                  {:customer (:id customer)
                   :pending_invoice_items_behavior "exclude"
                   :metadata {key value}}
                  api-key)]

    (testing "should be a valid invoice"
      (is (some? (:id invoice)))
      (testing "with a valid customer"
        (is (= (:id customer) (:customer invoice)))
        (testing "with valid metadata"
          (is (= value (get-in invoice [:metadata key]))))))

    (testing "can then retrieve the invoice"
      (let [retrieved-invoice (invoices/retrieve (:id invoice) api-key)]
        (testing "should still be a valid invoice"
          (is (some? (:id retrieved-invoice)))
          (testing "with a valid customer"
            (is (= (:id customer) (:customer retrieved-invoice)))))
        (testing "with valid metadata"
          (is (= value (get-in invoice [:metadata key]))))))))
