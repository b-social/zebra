(defproject b-social/zebra "0.1.7-SNAPSHOT"
  :description "A clojure wrapper for Stripe"
  :license {:name "The MIT License"
            :url  "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [com.stripe/stripe-java "10.12.1"]]
  :plugins [[jonase/eastwood "0.2.5"]]
  :eastwood {:config-files ["config/linter.clj"]})
