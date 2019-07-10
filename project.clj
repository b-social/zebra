(defproject b-social/zebra "0.1.5"
  :description "A clojure wrapper for Stripe"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [com.stripe/stripe-java "7.29.0"]]
  :plugins [[jonase/eastwood "0.2.5"]]
  :eastwood {:config-files ["config/linter.clj"]})
