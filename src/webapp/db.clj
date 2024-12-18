(ns webapp.db
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))


(def sleigh-ride-data
  (with-open [reader (io/reader "sleigh-rides.csv")]
    (doall
     (csv/read-csv reader))))

(defn get-all-songs
  []
  (->> sleigh-ride-data
       rest
       (map zipmap
            (->> (first sleigh-ride-data) ;; First row is the header
                 (map keyword)
                 repeat))
       (sort-by :year)))
