(ns ircparse.core
  (:require [instaparse.core :as insta]))

(def parser (insta/parser (clojure.java.io/resource "irc.abnf") :input-format :abnf))

(defn debug-parse [string]
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

(defn message
  "Takes a full IRC message, including the CR LF characters and returns
a hash with extracted information with these keys:

* command
* prefix
* trailing-params
* parse-tree"
  [message]
  (let [parse-tree (parser message)]
    {:command (get-command parse-tree)
     :prefix (get-server-hostname parse-tree)
     :trailing-params (get-trailing-params parse-tree)
     :parse-tree parse-tree}))
