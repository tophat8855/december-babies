(ns lyrics.api
  (:require [camel-snake-kebab.core :as csk]
            [clj-http.client :as http]
            [clojure.pprint :refer [pprint]]
            [cheshire.core :as json]))

(defn spy
  [x]
  (pprint x)
  x)

(def genius-api-url "https://api.genius.com")
(def api-token
  ;; NOTE: do not commit token
  (or (System/getenv "API_TOKEN")
      (throw (ex-info "API token not found." {}))))

(defn search
  "search endpoint for Genius API"
  [query]
  (let [response (http/get (str genius-api-url "/search")
                           {:query-params {:q        query
                                           :per_page 50
                                           :type     "song"
                                           :verbose  true}
                            :headers      {"Authorization" (str "Bearer " api-token)}
                            :debug        true})
        body (json/parse-string (:body response) csk/->kebab-case-keyword)]
    (get-in body [:response :hits])))

(def sleigh-ride-songs
  (let [song-list (search "Sleigh Ride")]
    (->> song-list
         (map :result)
         (map #(select-keys % [:title :url :id :primary-artist])))))

(comment (spy sleigh-ride-songs)


         )
