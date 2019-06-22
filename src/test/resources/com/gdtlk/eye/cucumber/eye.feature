Feature: Test the integrated program

  @help
  Scenario: I am testing the code in a cucumber step, and I worked.
    When we call bias main with params "-h"
    Then nothing

  @happypath
  Scenario: I have dockers composed elsewhere.  I set properties to point to them. a small test runs.
    When we have system properties
    | aero.host      | localhost        |
    | aero.port      | 3000             |
    | hive.host      | localhost:10000  |
    | ias.url        | http://localhost:4567/db2/client/12113/absit?adsafe_url=   |

    And we call bias main with params "-st 2019030507"
    Then the data matches for src/test/resources/itest_domains.txt

    ## java -jar -Daero.host=localhost -Daero.port=3000 -Dhive.host=localhost:10000 -Dias.url=http://localhost:4567/db2/client/12113/absit?adsafe_url= target/bias.jar -st 2019030507 -rt=20 -wt=8

#  @sadias
#  Scenario: With the dockers composed, I set up particular failing domains and test responses.
#    When we have system properties
#      | aero.host      | localhost        |
#      | aero.port      | 3000             |
#      | hive.host      | localhost:10000  |
#      | ias.url        | http://localhost:4567/db2/client/12113/absit?adsafe_url=   |
#      | hive.query     | select domain from test.test where dt='2019030507'
#
#### this replaces any earlier test data
#    And we fill in test data in hive partition "2019030507"
#    |  fancy.com                           |
#    |  1000pointsoflight.cn                |
#    |  I-built-a-pyramid-of-skulls.mn.null |
#    |  top-o-the-morning-to-ya.ie.error    |
#
#    And we fill in aerospike data
#    |  fancy.com | 3600  | {"2":{"200":1000}} |
#    |  1000pointsoflight.cn | 86400 | {"2":{"200":1000}} |
#
#    And we call bias main with params "-st 2019030507"
#    Then the data matches this
#      |  fancy.com                            | {"2":{"217":875,  "218":901,  "221":1000,  "220":875,  "219":1000,  "222":1000,  "223":1000}} |
#      |  1000pointsoflight.cn                 | {"2":{"217":875,  "218":901,  "221":1000,  "220":875,  "219":1000,  "222":1000,  "223":1000}} |
#      |  I-built-a-pyramid-of-skulls.mn.null  | {"2":{"217":875,  "218":901,  "221":1000,  "220":875,  "219":1000,  "222":1000,  "223":1000}} |
#      |  top-o-the-morning-to-ya.ie.error     | {"2":{"217":875,  "218":901,  "221":1000,  "220":875,  "219":1000,  "222":1000,  "223":1000}} |
