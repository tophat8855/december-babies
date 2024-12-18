(ns lyrics.api
  (:require [camel-snake-kebab.core :as csk]
            [clj-http.client :as http]
            [clojure.data.csv :as csv]
            [clojure.java.io :as io]
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

(defn fetch-page
  "search endpoint for Genius API"
  [query page]
  (let [response (http/get (str genius-api-url "/search")
                           {:query-params {:q        query
                                           :per_page 50
                                           :page     page
                                           :type     "song"
                                           :verbose  true}
                            :headers      {"Authorization" (str "Bearer " api-token)}
                            :debug        true})
        body     (json/parse-string (:body response) csk/->kebab-case-keyword)]
    (get-in body [:response :hits])))

(defn fetch-pages
  "fetch all pages of search results"
  [query]
  (loop [page 1
         results []]
    (let [page-results (fetch-page query page)]
      (if (empty? page-results)
        results
        (recur (inc page)
               (concat results page-results))))))

(def sleigh-ride-songs
  (let [song-list (fetch-pages "Sleigh Ride")]
    (->> song-list
         (map :result)
         (map #(select-keys % [:title :url :id :primary-artist :primary-artists :release-date-components :irrelevant])))))

(comment
  (with-open [writer (io/writer "sleigh-rides.csv")]
           (csv/write-csv writer
                          [["id" "title" "year" "artist" "url" "party" "irrelevant"]])
           (csv/write-csv writer
                          (map (fn [song]
                                 [(song :id)
                                  (song :title)
                                  (->> (song :release-date-components)
                                       :year)
                                  (get-in (song :primary-artist) [:name])
                                  (song :url)])
                               sleigh-ride-songs))))
