(ns ircparse.core
  (:require [instaparse.core :as insta]))

(defn parser [string]
  ((insta/parser (clojure.java.io/resource "irc.abnf") :input-format :abnf) string))

(defn rule? [rule subtree]
  (= rule (first subtree)))

(defn match-rule [rules rule]
  (let [matching-rule (first (filter (partial rule? rule) rules))]
    (rest matching-rule)))

(defn get-server-hostname [subtree]
  (-> (list subtree)
      (match-rule :message)
      (match-rule :prefix)
      (match-rule :servername)
      (match-rule :hostname)
      first))

(defn get-command [subtree]
  (-> (list subtree)
      (match-rule :message)
      (match-rule :command)
      first))

(defn get-trailing-params [subtree]
  (-> (list subtree)
      (match-rule :message)
      (match-rule :params)
      (match-rule :trailing)
      first))

(defn ircparse
  [message]
  (let [parsed (parser message)]
    {:command (get-command parsed)
     :prefix (get-server-hostname parsed)
     :trailing-params (get-trailing-params parsed)}))
