(ns red-ghost.component.publisher
  (:require [com.stuartsierra.component :as component]
            [taoensso.carmine :as car]
            [taoensso.carmine.message-queue :as car.mq]))

(defprotocol Publish
  (publish [this queue message]
    "Enqueues a message"))

(defn publish* [conn {:keys [queue message]}]
  (car/wcar conn
            (car.mq/enqueue queue message)))

(defrecord Publisher [config]
  component/Lifecycle
  (start [this]
    (assoc this :conn {:pool {} :spec (select-keys config [:host :port])}))
  (stop [this]
    (assoc this :conn nil))
  Publish
  (publish [this queue message]
    (publish* (:conn this) {:queue queue
                            :message message})))

(defn create [{:keys [redis]}]
  (->Publisher {:host (:host redis) :port (:port redis)}))
