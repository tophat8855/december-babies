(ns lyrics.api-test
  (:require [clojure.test :refer :all]
            [lyrics.api :as api]))

(deftest categorize-song
  (testing "instrumental songs"
    (let [hickory-lyrics nil]
      (is (= "instrumental"
             (api/categorize-song hickory-lyrics)))))

  (testing "irrelevant songs"
    (let [hickory-lyrics ["The quick brown fox"
                          {:type :element, :attrs nil, :tag :br, :content nil}
                          "jumped over the"
                          {:type :element, :attrs nil, :tag :br, :content nil}
                          "lazy dog"]]
      (is (= "irrelevant"
             (api/categorize-song hickory-lyrics)))))

  (testing "birthday songs"
    (let [hickory-lyrics ["Just hear those sleigh bells jingling"
                          {:type :element, :attrs nil, :tag :br, :content nil}
                          "Ring-ting-tingling too"
                          {:type :element, :attrs nil, :tag :br, :content nil}
                          "Come on, it's lovely weather"
                          {:type :element, :attrs nil, :tag :br, :content nil}
                          "There's a Birthday party"
                          {:type :element, :attrs nil, :tag :br, :content nil}
                          "At the home of Farmer Gray"]]
      (is (= "birthday"
             (api/categorize-song hickory-lyrics)))))

  (testing "christmas songs"
    (let [hickory-lyrics ["Just hear those sleigh bells jingling"
                          {:type :element, :attrs nil, :tag :br, :content nil}
                          "Ring-ting-tingling too"
                          {:type :element, :attrs nil, :tag :br, :content nil}
                          "Come on, it's lovely weather"
                          {:type :element, :attrs nil, :tag :br, :content nil}
                          "There's a Christmas party"
                          {:type :element, :attrs nil, :tag :br, :content nil}
                          "At the home of Farmer Gray"]]
      (is (= "christmas"
             (api/categorize-song hickory-lyrics)))))

  (testing "new years songs"
    (let [hickory-lyrics ["Just hear those sleigh bells jingling"
                          {:type :element, :attrs nil, :tag :br, :content nil}
                          "Ring-ting-tingling too"
                          {:type :element, :attrs nil, :tag :br, :content nil}
                          "Come on, it's lovely weather"
                          {:type :element, :attrs nil, :tag :br, :content nil}
                          "There's a new years party"
                          {:type :element, :attrs nil, :tag :br, :content nil}
                          "At the home of Farmer Gray"]]
      (is (= "new years"
             (api/categorize-song hickory-lyrics)))))

  (testing "songs without the party verse"
    (let [hickory-lyrics ["Just hear those sleigh bells jingling"
                          {:type :element, :attrs nil, :tag :br, :content nil}
                          "Ring-ting-tingling too"
                          {:type :element, :attrs nil, :tag :br, :content nil}
                          "Come on, it's lovely weather"
                          {:type :element, :attrs nil, :tag :br, :content nil}]]
      (is (= "none"
             (api/categorize-song hickory-lyrics))))))
