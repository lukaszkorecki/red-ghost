(defproject red-ghost "0.1.0-SNAPSHOT"
  :description "Component for message queue implenentation, powered by Carmine"
  :url "https://github.com/lukaszkorecki/red-ghost"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [com.stuartsierra/component "0.4.0"]
                 [com.taoensso/carmine "2.19.1"]
                 [org.clojure/tools.logging "0.5.0"]]

  :deploy-repositories {"clojars" {:sign-releases false
                                   :username :env/clojars_username
                                   :password :env/clojars_password}}

  :min-lein-version "2.9.0"
  :license "MIT"
  :profile {:dev {:resource-paths ["dev-resources"]
                  :dependencies [[ch.qos.logback/logback-classic "1.2.3"]]}})
