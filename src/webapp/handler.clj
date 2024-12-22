(ns webapp.handler
  (:require [compojure.core :refer [defroutes GET]]
            [compojure.route :as route]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [webapp.views :as views]))

(defroutes app-routes
  (GET "/" [] (views/home-page))
  (GET "/love-december-babies" [] (views/love-december-babies-page))
  (GET "/hate-december-babies" [] (views/hate-december-babies-page))
  (GET "/neutral-december-babies" [] (views/neutral-december-babies-page))
  (route/not-found "Not Found"))

(def app
  ;; use #' prefix for REPL-friendly code -- see note below
  (wrap-defaults #'app-routes site-defaults))

(defn -main []
  (jetty/run-jetty #'app {:port 3000}))
