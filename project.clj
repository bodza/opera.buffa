(defproject koraujkor "x.y.z"
 ;; :min-lein-version "2.5.3"
    :dependencies [[org.clojure/clojure "1.8.0"]
                   [org.clojure/clojurescript "1.7.228"]
                   [reagent "0.5.1"]
                 #_[cljsjs/react-dom "0.14.3-1"]
                 #_[cljsjs/react-dom-server "0.14.3-0"]
                 #_[cljsjs/highcharts "4.2.2-2"]
                   [datascript "0.15.0"]
                   [posh "0.3.4.1"]]
    :plugins [[lein-cljsbuild "1.1.2" :exclusions [[org.clojure/clojure]]]
              [lein-figwheel "0.5.0-6"]]
    :source-paths ["src"]
 ;; :clean-targets ^{:protect false} [:compile-path :target-path]
    :cljsbuild {:builds {:client {:source-paths ["src"]
                                  :figwheel true
                                  :compiler {:main "koraujkor.index"
                                             :asset-path "js/out"
                                             :output-to "resources/public/js/koraujkor.js"
                                             :source-map true
                                             :source-map-timestamp true
                                             :optimizations :none}}}}
    :figwheel {:css-dirs ["resources/public/css"]
               :server-logfile "log/figwheel_server.log"})
