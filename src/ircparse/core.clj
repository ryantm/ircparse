(ns ircparse.core
  (:require [instaparse.core :as insta]))

(def parser (insta/parser (clojure.java.io/resource "irc.abnf") :input-format :abnf))

(defn debug-parse [string]
  ((insta/parser (clojure.java.io/resource "irc.abnf") :input-format :abnf) string))

(defn rule? [rule subtree]
  (= rule (first subtree)))

(defn match-rule [subtree rule]
  (let [matching-rule (first (filter (partial rule? rule) subtree))]
    (rest matching-rule)))

(defn get-client-user [tree]
  (-> (list tree)
      (match-rule :message)
      (match-rule :prefix)
      (match-rule :user)
      first))

(defn get-client-nickname [tree]
  (-> (list tree)
      (match-rule :message)
      (match-rule :prefix)
      (match-rule :nickname)
      first))

(defn get-client-host [tree]
  (-> (list tree)
      (match-rule :message)
      (match-rule :prefix)
      (match-rule :host)
      (match-rule :hostname)
      first))

(defn get-server-hostname [tree]
  (-> (list tree)
      (match-rule :message)
      (match-rule :prefix)
      (match-rule :servername)
      (match-rule :hostname)
      first))

(defn get-command [tree]
  (-> (list tree)
      (match-rule :message)
      (match-rule :command)
      first))

(defn get-trailing-params [tree]
  (-> (list tree)
      (match-rule :message)
      (match-rule :params)
      (match-rule :trailing)
      first))

(defn- no-nil-map [kv-list]
  (into {} (remove (comp nil? second) kv-list)))

(defn message
  "Takes a full IRC message, including the CR LF characters and returns
a map with extracted information with some of these keys:

* command
* server-hostname
* trailing-params
* client-user
* client-nickname
* client-host
* parse-tree

If it fails to parse the message, it returns a map with a single key,
:failure, which is an Instaparse failure object."
  [message]
  (let [instaparse-return (parser message)]
    (if (insta/failure? instaparse-return)
      {:failure (insta/get-failure instaparse-return)}
      (no-nil-map
       [[:command (get-command instaparse-return)]
        [:server-hostname (get-server-hostname instaparse-return)]
        [:trailing-params (get-trailing-params instaparse-return)]
        [:client-user (get-client-user instaparse-return)]
        [:client-nickname (get-client-nickname instaparse-return)]
        [:client-host (get-client-host instaparse-return)]
        [:parse-tree instaparse-return]]))))
