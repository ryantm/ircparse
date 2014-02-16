(ns ircparse.core-test
  (:require [clojure.test :refer :all]
            [ircparse.core :refer :all]))

(deftest parser-test
  (is (= 
       [:message [:prefix [:servername [:hostname "cameron.freenode.net"]]] [:command "433"] [:params [:middle "*"] [:middle "rtm"] [:trailing "Nickname is already in use"]]]
       (parser ":cameron.freenode.net 433 * rtm :Nickname is already in use\r\n"))))

(deftest ircparse-test
  (testing "Can parse a notice."
    (let [parsed (ircparse ":barjavel.freenode.net NOTICE * :*** Found your hostname\r\n")]
      (is (= (:command parsed)
             "NOTICE"))
      (is (= (:prefix parsed)
             "barjavel.freenode.net"))
      (is (= (:trailing-params parsed)
             "*** Found your hostname")))
    (let [parsed (ircparse ":hubbard.freenode.net NOTICE * :*** Looking up your hostname...\r\n")]
      (is (= (:command parsed)
             "NOTICE"))
      (is (= (:prefix parsed)
             "hubbard.freenode.net"))
      (is (= (:trailing-params parsed)
             "*** Looking up your hostname...")))
    (let [parsed (ircparse ":cameron.freenode.net 433 * rtm :Nickname is already in use\r\n")]
      (is (= (:command parsed)
             "433"))
      (is (= (:prefix parsed)
             "cameron.freenode.net"))
      (is (= (:trailing-params parsed)
             "Nickname is already in use")))))




