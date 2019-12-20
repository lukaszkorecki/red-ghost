# Red Ghost 🔴👻

<img src="https://www.writeups.org/wp-content/uploads/Red-Ghost-Apes-Fantastic-Four-Marvel-Comics-Kragoff.jpg" align=right heigth="200px" >

(Still early days, watch out!)

See [dev-resources/example.clj](dev-resources/example.clj)

Run with `lein run -m clojure.main dev-resources/example.clj`

Output:

```

☭ 0 # master - red-ghost : lein run -m clojure.main dev-resources/example.clj
Dec 20, 2019 10:26:31 PM clojure.tools.logging$eval6642$fn__6645 invoke
INFO: start queue=:red-ghost.queue threads=1
19-12-20 22:26:31 vagrant INFO [taoensso.carmine.message-queue:245] - Message queue worker starting: :red-ghost.queue
got {:to-reply :success, :message :one}
got {:to-reply :retry, :message :two}
got {:to-reply :error, :message :three}
19-12-20 22:26:55 vagrant ERROR [taoensso.carmine.message-queue:201] - Error handling :red-ghost.queue queue message:
6d3eb4e0-4a02-4413-8eb9-9b35a99fb15e
                                        java.lang.Thread.run              Thread.java:  834
          java.util.concurrent.ThreadPoolExecutor$Worker.run  ThreadPoolExecutor.java:  628
           java.util.concurrent.ThreadPoolExecutor.runWorker  ThreadPoolExecutor.java: 1128
                         java.util.concurrent.FutureTask.run          FutureTask.java:  264
                                                         ...
                         clojure.core/binding-conveyor-fn/fn                 core.clj: 2030
                 taoensso.carmine.message-queue.Worker/fn/fn        message_queue.clj:  287
   taoensso.carmine.message-queue.Worker/start-polling-loop!        message_queue.clj:  253
taoensso.carmine.message-queue.Worker/start-polling-loop!/fn        message_queue.clj:  273
                      taoensso.carmine.message-queue/handle1        message_queue.clj:  223
                taoensso.carmine.message-queue/handle1/error        message_queue.clj:  201
                                       taoensso.timbre/-log!               timbre.clj:  358
                                          clojure.core/deref                 core.clj: 2320
                                                         ...
             taoensso.carmine.message-queue/handle1/error/fn        message_queue.clj:  202
clojure.lang.ExceptionInfo: :error handler response
     attempt: 1
    mcontent: {:to-reply :error, :message :three}
         mid: "6d3eb4e0-4a02-4413-8eb9-9b35a99fb15e"
       qname: :red-ghost.queue


> W:3 *ansi-term*  L:558 C:0 | Term  *

```
