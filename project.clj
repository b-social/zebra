(defproject b-social/zebra "0.1.13-SNAPSHOT"
  :description "A clojure wrapper for Stripe"
  :url "https://github.com/b-social/zebra"
  :license {:name "The MIT License"
            :url  "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [com.stripe/stripe-java "19.12.0"]
                 [webdriver "0.15.0"]
                 [org.seleniumhq.selenium/htmlunit-driver "2.41.0"]]

  :plugins [[jonase/eastwood "0.2.5"]
            [lein-eftest "0.5.3"]
            [lein-changelog "0.3.2"]
            [lein-shell "0.5.0"]
            [lein-codox "0.10.7"]]
  :profiles {:shared {:dependencies [[eftest "0.5.3"]]}
             :dev    [:shared]
             :test   [:shared]}

  :codox
  {:namespaces  [#"^zebra\."]
   :output-path "docs"
   :source-uri  "https://github.com/b-social/zebra/blob/{version}/{filepath}#L{line}"}

  :eftest {:multithread? false}
  :deploy-repositories {"releases" {:url "https://repo.clojars.org"
                                    :creds :gpg}}
  :eastwood {:config-files ["config/linter.clj"]}
  :release-tasks
  [["shell" "git" "diff" "--exit-code"]
   ["change" "version" "leiningen.release/bump-version" "release"]
   ["codox"]
   ["changelog" "release"]
   ["shell" "sed" "-E" "-i" "" "s/\"[0-9]+\\.[0-9]+\\.[0-9]+\"/\"${:version}\"/g" "README.md"]
   ["shell" "git" "add" "."]
   ["vcs" "commit"]
   ["vcs" "tag"]
   ["deploy"]
   ["change" "version" "leiningen.release/bump-version"]
   ["vcs" "commit"]
   ["vcs" "tag"]
   ["vcs" "push"]])
