(ns ircparse.core-test
  (:require [clojure.test :refer :all]
            [ircparse.core :refer :all]
            [clojure.pprint :refer [pprint]]))

(deftest ircparse-test
  (testing "a malformed message"
    (is (contains? (message "HI") :failure)))
  (testing "can parse some messages"
    (let [parsed (message ":ryantm!~ryantm@ip98-167-44-17.lv.lv.cox.net QUIT :Quit: ryantm\r\n")]
      (is (= (:command parsed)
             "QUIT"))
      (is (= (:client-nickname parsed)
             "ryantm"))
      (is (= (:client-user parsed)
             "~ryantm"))
      (is (= (:client-host parsed)
             "ip98-167-44-17.lv.lv.cox.net")))
    (let [parsed (message ":barjavel.freenode.net NOTICE * :*** Found your hostname\r\n")]
      (is (= (:command parsed)
             "NOTICE"))
      (is (= (:server-hostname parsed)
             "barjavel.freenode.net"))
      (is (= (:trailing-params parsed)
             "*** Found your hostname")))
    (let [parsed (message ":hubbard.freenode.net NOTICE * :*** Looking up your hostname...\r\n")]
      (is (= (:command parsed)
             "NOTICE"))
      (is (= (:server-hostname parsed)
             "hubbard.freenode.net"))
      (is (= (:middle-params parsed)
             ["*"]))
      (is (= (:trailing-params parsed)
             "*** Looking up your hostname...")))
    (let [parsed (message ":cameron.freenode.net 433 * rtm :Nickname is already in use\r\n")]
      (is (= (:command parsed)
             "433"))
      (is (= (:server-hostname parsed)
             "cameron.freenode.net"))
      (is (= (:middle-params parsed)
             ["*" "rtm"]))
      (is (= (:trailing-params parsed)
             "Nickname is already in use"))
      (is (= (:parse-tree parsed)
             [:message [:prefix [:servername [:hostname "cameron.freenode.net"]]] [:command "433"] [:params [:middle "*"] [:middle "rtm"] [:trailing "Nickname is already in use"]]])))))




