(ns ircparse.core
  (:require [instaparse.core :as insta]
            [clojure.walk :as walk]))

(def parser
  (insta/parser (clojure.java.io/resource "irc.abnf") :input-format :abnf))

(defn rule? [rule subtree]
  (= rule (first subtree)))

(defn match-rule [rules rule]
  (let [matching-rule (first (filter (partial rule? rule) rules))]
    (rest matching-rule)))

(defn get-server-hostname [subtree]
  (apply str (-> (list subtree)
                 (match-rule :message)
                 (match-rule :prefix)
                 (match-rule :servername)
                 (match-rule :hostname))))

(defn get-trailing-params [subtree]
  (apply str (-> (list subtree)
                 (match-rule :message)
                 (match-rule :params)
                 (match-rule :trailing))))

(defn ircparse
  [message]
  (let [parsed (parser message)]
    {:command :notice
     :prefix (get-server-hostname parsed)
     :trailing-params (get-trailing-params parsed)}))
