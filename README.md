#ircparse [![Build Status](https://travis-ci.org/talklibre/ircparse.png?branch=master)](https://travis-ci.org/talklibre/ircparse)
An IRC message parser written in Clojure.

#Quickstart
Add the following line to your [Leiningen](https://github.com/technomancy/leiningen) project dependencies:

````clojure
[ircparse "0.3.0-SNAPSHOT"]
````

Require ircparse in your namespace header:

````clojure
(ns example.core
  (:require [ircparse.core]))
````

##Parsing messages

Pass the full message including the CR LF characters to `ircparse.core/message`.

Here is an example of trying it in `lein repl` inside a project depending on ircparse:
````clojure
$lein repl
user=> (require 'ircparse.core)
nil
user=> (require 'clojure.pprint)
nil
user=> (pprint (ircparse.core/message ":cameron.freenode.net 433 * rtm :Nickname is already in use\r\n"))
{:command "433",
 :server-hostname "cameron.freenode.net",
 :trailing-params "Nickname is already in use",
 :middle-params ("*" "rtm"),
 :parse-tree
 [:message
  [:prefix [:servername [:hostname "cameron.freenode.net"]]]
  [:command "433"]
  [:params
   [:middle "*"]
   [:middle "rtm"]
   [:trailing "Nickname is already in use"]]]}
````
