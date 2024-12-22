(ns lyrics.api
  (:require [camel-snake-kebab.core :as csk]
            [cheshire.core :as json]
            [clj-http.client :as http]
            [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clojure.pprint :refer [pprint]]
            [clojure.string :as string]
            [hickory.core :refer [as-hickory parse]]
            [hickory.select :as hs]))

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

(defn to-hickory [url]
  (-> (http/get url)
      :body
      parse
      as-hickory))

(defn categorize-song
  [lyrics]
  (cond
    (nil? lyrics) "instrumental"

    (empty? (filter #(string/includes? (string/lower-case %) "lovely weather") lyrics))
    "irrelevant"

    (seq (filter #(string/includes? (string/lower-case %) "birthday party") lyrics))
    "birthday"

    (seq (filter #(string/includes? (string/lower-case %) "christmas party") lyrics))
    "christmas"

    (seq (filter #(string/includes? (string/lower-case %) "new years party") lyrics))
    "new years"

    :else "none"))

(defn scrape-lyrics-for-party
  [url]
  (->> url
       to-hickory
       (hs/select (hs/attr :data-lyrics-container))
       first
       :content
       categorize-song))

(comment
  (with-open [writer (io/writer "sleigh-rides.csv")]
    (csv/write-csv writer
                   [["id" "title" "year" "artist" "party"]])
    (csv/write-csv writer
                   (map (fn [song]
                          (when (and (string/includes? (string/lower-case (song :title)) "sleigh")
                                     (string/includes? (string/lower-case (song :title)) "ride"))
                            [(song :id)
                             (song :title)
                             (->> (song :release-date-components)
                                  :year)
                             (get-in (song :primary-artist) [:name])
                             (scrape-lyrics-for-party (song :url))]))
                        sleigh-ride-songs))))
