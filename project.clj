(def version
  (or (System/getenv "VERSION")
      "0.0.0"))

(defproject ai.mypulse/zebra version
  :description "A clojure wrapper for Stripe"
  :url "https://github.com/mypulse-uk/zebra"

  :license {:name "The MIT License"
            :url  "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [com.stripe/stripe-java "19.12.0"]]
  :plugins [[lein-cloverage "1.2.3"]
            [lein-shell "0.5.0"]
            [lein-ancient "0.7.0"]
            [lein-changelog "0.3.2"]
            [lein-eftest "0.5.9"]
            [lein-codox "0.10.8"]
            [lein-kibit "0.1.8"]
            [lein-bikeshed "0.5.2"]]
  :profiles
  {:shared {:dependencies [[eftest "0.5.9"]]}
   :dev    [:shared]
   :test   [:shared]

   :release
   {:release-tasks
    [["deploy"]]}}

  :eftest {:multithread? false}
  :deploy-repositories
  {"releases" {:url "https://repo.clojars.org"
               :username :env/clojars_deploy_username
               :password :env/clojars_deploy_token
               :sign-releases false}})
