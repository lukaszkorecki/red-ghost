(ns red-ghost.component.worker
  (:require [com.stuartsierra.component :as component]
            [taoensso.carmine :as car]
            [clojure.tools.logging :as log]
            [taoensso.carmine.message-queue :as car.mq]))

(defprotocol Work
  (clear! [this]
    "Clears the queue - DANGER, use with caution!")
  (status [this]
    "Checks the status of the queue to which the worker is bound"))

(defrecord Worker [conn config queue handler]
  component/Lifecycle
  (start [this]
    (log/infof "start queue=%s threads=%s" queue (:nthreads config))
    (let [handler-fn (fn wrap [args]
                       (log/debugf "queue=%s in=%s" queue args)
                       (let [payload (assoc args :component
                                            (dissoc this :conn :config :handler))
                             res (handler payload)]
                         (log/debugf "queue=%s out=%s" queue res)
                         res))
          worker (car.mq/worker conn queue
                                (assoc config
                                       :handler handler-fn))]
      (assoc this :worker worker)))
  (stop [this]
    (log/warnf "stop queue=%s" queue)
    (car.mq/stop (:worker this))
    (assoc this :worker nil))
  Work
  (status [this]
    (car.mq/queue-status conn queue))
  (clear! [this]
    (car.mq/clear-queues conn queue)))

(defn create
  "Creates a worker component. Options:
  {:handler function, must return {:satus [:success :error :retry / :backoff-ms]}
   :config { :queue :some.queue.name
             :redis { :host :port }
             :worker { :threads int|1 :throttle-ms int|200} }}"
  [{:keys [redis queue worker handler]}]
  {:pre [(fn? handler)
         (keyword? queue)
         (integer? (:port redis))
         (string? (:host redis))
         (let [threads (:threads worker)]
           (or (nil? threads) (int? threads)))
         (let [throttle (:throttle-ms worker)]
           (or (nil? throttle) (int? throttle)))]}
  (let [conn {:pool {} :spec redis}
        worker-config {:nthreads (or (:threads worker) 1)
                       :throttle-ms (or (:throttle-ms worker) 200)}]
    (->Worker conn worker-config queue handler)))
