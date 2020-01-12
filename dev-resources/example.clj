(require '[com.stuartsierra.component :as component]
         '[red-ghost.component.worker :as worker]
         '[red-ghost.component.publisher :as publisher])

(defn a-handler
  "Simple handler - will print the  `message` and exit depending on the value of `:to-reply`"
  [{:keys [message ; a clojure map, string, etc
                         component]}]
  (printf "got %s\n" message)
  (let [stat (:to-reply message)]
    {:status stat}))

(def redis-conf
  {:host (or (System/getenv "REDIS_HOST") "0.0.0.0")
   :port (if-let [redis-port (System/getenv "REDIS_PORT")]
           (Integer/parseInt redis-port)
           6379)})

(def system
  {:worker (worker/create {:redis redis-conf
                           :queue :red-ghost.queue
                           :handler a-handler})
   :publisher (publisher/create {:redis redis-conf})})

(def sys (atom nil))

(defn main []

  (reset! sys
          (component/start-system (component/map->SystemMap system)))
  (publisher/publish (:publisher @sys) :red-ghost.queue {:to-reply :success :message :one})
  (publisher/publish (:publisher @sys) :red-ghost.queue {:to-reply :retry :message :two})
  (publisher/publish (:publisher @sys) :red-ghost.queue {:to-reply :error :message :three}))

(main)
