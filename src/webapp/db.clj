(ns webapp.db
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clojure.string :as string]))

(def sleigh-ride-data
  (with-open [reader (io/reader "sleigh-rides.csv")]
    (doall
     (csv/read-csv reader))))

(defn merge-tracks [track1 track2]
  (let [year (if (string/blank? (:year track1)) (:year track2) (:year track1))]
    (assoc track1 :year year)))

(def get-all-songs
  (->> sleigh-ride-data
       (remove (fn [song] (= [""] song)))
       rest
       (map zipmap
            (->> (first sleigh-ride-data) ;; First row is the header
                 (map keyword)
                 repeat))
       (group-by :artist)
       (map (fn [[artist trks]]
              (reduce merge-tracks trks)))
       (into [])
       (remove
        (fn [song]
          (= "irrelevant" (:party song))))))

(def get-birthday-songs
  (sort-by :artist (get (group-by :party get-all-songs) "birthday")))

(def get-instrumental-songs
  (sort-by :artist (get (group-by :party get-all-songs) "instrumental")))
