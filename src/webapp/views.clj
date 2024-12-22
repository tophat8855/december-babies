(ns webapp.views
 (:require [hiccup.page :as page]
           [ring.util.anti-forgery :as util]
           [webapp.db :as db]))

(defn home-page
  []
  (page/html5
   [:h1 "Who loves December Babies?"]
   [:h2 "It's hard to have a birthday in December"]
   [:div "If your birthday is in the second half of the month, your birthday conflicts with the big holidays."
    "People are out of town, spending time with their families, and not attending your party."]
   [:div "If your birthday is in the first half of the month, your birthday falls on the days that everyone throws their"
    "holiday parties before people travel."]
   [:div "And if it's not holidays, it's finals week, there is inclement weather, or all your friends are sick."]

   [:h2 "But there was one shining piece of hope for December babies."]
   [:div "There was one song; one song that acknowledged that December isn't just Christmas, Kwanzaa, or Hanukkah."]

   [:h2 "Sleigh Ride"]

   [:div "Everyone enjoys it as a holiday song, but it's also the one song that acknowledges that people have birthdays in December."]
   [:h3 "🎶 There's a birthday party at the home of Farmer Gray. It'll be the perfect ending to the perfect day. 🎶"]

   [:div "Farmer Gray was throwing a party for you, December Baby."]

   [:h2 "However"]

   [:div "Some artists have changed the lyrics to \"Christmas Party at the home of Farmer Gray\" or have altogether removed that verse!"]

   [:div "Who are these artists who hate December babies? Who can't give us 10 seconds of their 3-minute song?"]

   [:h2 "Let's find out!"]
   [:div "You can search artists"]

   (let [all-songs     (remove
                        (fn [song]
                          (= "irrelevant" (:party song)))
                        (db/get-all-songs))
         grouped-songs (group-by :party all-songs)]

     [:div
      [:h3 "Artists who love December babies"]
      "aka, those who kept the 'birthday' verse in their version"
      (for [song (get grouped-songs "birthday")]
        [:div (:artist song)])

      [:h3 "Artists who hate December babies"]
      "aka, those who changed 'birthday' to 'christmas' in their version"
      (for [song (get grouped-songs "christmas")]
        [:div (:artist song)])

      [:h3 "The one group who hates December babies, but likes the new year?"]
      "aka, those who changed 'birthday' to 'new years' in their version"
      (for [song (get grouped-songs "new years")]
        [:div (:artist song)])

      [:h3 "Artists who dislike parties"]
      "aka, those who removed any reference to a party at all in their version"
      (for [song (get grouped-songs "none")]
        [:div (:artist song)])

      [:h3 "Instrumental versions"]
      (for [song (get grouped-songs "instrumental")]
        [:div (:artist song)])]
     #_[:table
        [:tr [:th "artist"] [:th "year"] [:th "party?"]]
        (for [song all-songs]
          [:tr
           [:td (:artist song)]
           [:td (:year song)]
           [:td (:party song)]])])))

