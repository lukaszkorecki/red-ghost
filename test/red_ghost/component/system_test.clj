(ns red-ghost.component.system-test
  (:require [com.stuartsierra.component :as component]
            [clojure.tools.logging :as log]
            [red-ghost.component.publisher :as publisher]
            [red-ghost.component.worker :as worker]
            [clojure.test :refer :all]))

(def redis-conf
  {:host (or (System/getenv "REDIS_HOST") "0.0.0.0")
   :port (or (System/getenv "REDIS_PORT") 6379)})

(deftest worker-pub-system
  (let [queue :queue-test
        queue-vals (atom [])
        wrap-dep (fn [i] {:input i})
        sys {:worker (component/using
                      (worker/create {:redis redis-conf
                                      :queue queue
                                      :throttle-ms 0
                                      :handler (fn [{:keys [message component]}]
                                                 (swap! queue-vals
                                                        (fn [v]
                                                          (conj v ((:wrap-dep component) message))))
                                                 {:status :success})})
                      [:wrap-dep])
             :wrap-dep wrap-dep
             :publisher (publisher/create {:redis redis-conf})}
        system (-> sys component/map->SystemMap component/start-system)]
    (try
      (is (publisher/publish (:publisher system) queue "message 1"))
      (is (publisher/publish (:publisher system) queue {:rich  "message 2"}))
      (loop [i 0]
        (log/info "waiting for result")
        (when (and
               (< i 100) ; bail after 100 iterations
               (not= 2 (count @queue-vals)))
          (Thread/sleep 100)
          (recur (inc i))))
      (is (= 2 (count @queue-vals)))
      (is (= (set [{:input "message 1"}
                   {:input {:rich "message 2"}}])
             (set @queue-vals)))
      (finally
        (component/stop-system system)))))
