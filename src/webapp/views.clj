(ns webapp.views
 (:require [hiccup.page :as page]
           [webapp.db :as db]))

(def header-links
  [:div#header-links
   "[ "
   [:a {:href "/"} "Home"]
   " | "
   [:a {:href "/love-december-babies"} "Who loves us?"]
   " | "
   [:a {:href "/hate-december-babies"} "Who hates us?"]
   " ]"])

(def sleigh-ride-explanation
  [:div
    [:h1 "Who loves December Babies?"]
    [:h2 "It's hard to have a birthday in December"]
    [:div "If you're not sharing your birthday with a holiday, it's finals week, there is inclement weather, or all your friends are out of town or sick."]

    [:h2 "But there was one shining piece of hope for December babies."]
    [:div "There was one song; one song that acknowledged that people are born in December:"]

    [:h2 "Sleigh Ride"]

    [:div "Everyone enjoys it as a holiday song, but it's also the one song that acknowledges that people have birthdays in December."]
    [:h3 "🎶 There's a birthday party at the home of Farmer Gray. It'll be the perfect ending to the perfect day. 🎶"]

    [:div "Farmer Gray was throwing a party for you, December Baby."]

    [:h3 "However"]

    [:div "Some artists have changed the lyrics to \"Christmas Party at the home of Farmer Gray\" or have altogether removed that verse!"]

    [:div "Who are these artists who hate December babies? Who can't give us 10 seconds of their 3-minute song?"]])

(defn love-december-babies-page
  []
  (page/html5
   header-links
   [:div
    [:h1 "Artists who love December babies"]
    "These artist acknowledge and see you, December Baby"
    (for [song db/get-birthday-songs]
      [:div (:artist song)])]))

(defn hate-december-babies-page
  []
  (page/html5
   header-links
   [:div
    [:h3 "Artists who hate December babies"]
    (let [all-songs (group-by :party db/get-all-songs)
          hate-songs (sort-by :artist (concat (get all-songs "christmas")
                                              (get all-songs "new years")
                                              (get all-songs "none")))]
      (for [song hate-songs]
        [:div (:artist song)]))]))

(defn neutral-december-babies-page
  []
  (page/html5
   header-links
   [:div
    [:h3 "Artists who made instrumental versions of Sleigh Ride"]
    [:p "In your head, you can pretend they are performing for you, preformaing for everyone."]
    (for [song db/get-instrumental-songs]
      [:div (:artist song)])]))

(defn home-page
  []
  (page/html5
   header-links
   sleigh-ride-explanation))
