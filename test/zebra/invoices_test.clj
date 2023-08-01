(ns zebra.invoices-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [zebra.customers :as customers]
    [zebra.helpers.constants :refer [api-key]]
    [zebra.invoices :as invoices])
  (:import
    (com.stripe.model
      InvoiceItem)
    (com.stripe.net
      RequestOptions)))

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

(deftest finalise-invoice
  (let [customer (customers/create api-key)
        opts (-> (RequestOptions/builder) (.setApiKey api-key) .build)
        _ (InvoiceItem/create {"customer" (:id customer) "amount" 1000 "currency" "GBP"} opts)
        invoice (invoices/create
                  {:customer (:id customer)}
                  api-key)
        finalised-invoice (invoices/finalise (:id invoice) api-key)]

    (testing "should contain a payment intent"
      (is (some? (:payment_intent finalised-invoice))))))
