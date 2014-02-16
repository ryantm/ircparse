(ns ircparse.core-test
  (:require [clojure.test :refer :all]
            [ircparse.core :refer :all]))

(deftest parse-test
  (testing "Can parse a notice."
    (let [parsed (ircparse ":barjavel.freenode.net NOTICE * :*** Found your hostname\r\n")]
      (is (= (:command parsed)
             :notice))
      (is (= (:prefix parsed)
             "barjavel.freenode.net"))
      (is (= (:trailing-params parsed)
             "*** Found your hostname")))
    (let [parsed (ircparse ":hubbard.freenode.net NOTICE * :*** Looking up your hostname...\r\n")]
      (is (= (:command parsed)
             :notice))
      (is (= (:prefix parsed)
             "hubbard.freenode.net"))
      (is (= (:trailing-params parsed)
             "*** Looking up your hostname...")))))

;:cameron.freenode.net 433 * rtm :Nickname is already in use


