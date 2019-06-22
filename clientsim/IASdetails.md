#IAS interface

this is the detailed information about how the api for IAS updated data works

the url is

    http://api.adsafeprotected.com/db2/client/12113/absit?adsafe_url=<target url>

Possibly, that 12113 is our account# with IAS; must research 

bsc stands for Brand-Safety-X, is the scores used by blackbird

Brand-Safety data comes back from IAS in this format:

    {
      "bsc": {
        "drg": 1000,
        "alc": 1000,
        "off": 1000,
        "dlm": 1000,
        "adt": 1000,
        "sam": 1000,
        "hat": 1000
      }
    }

Blackbird is expecting the Brand-Safety data in this format:

    {
      "2": {
        "220": 1000,
        "221": 1000,
        "222": 1000,
        "223": 1000,
        "219": 1000,
        "217": 1000,
        "218": 1000
      }
    }

output from absit endpoint

    {"action":"passed",
    "bsc":{"adt":1000,"dlm":1000,"drg":1000,"alc":1000,"hat":1000,"off":1000,
    "sam":1000},"iab1":["iab_travel"],"iab2":["iab_t2_tourism"],"traq":700,
    "ttl":"2019-03-11T13:09-0400"
    }

for absuit, much other data comes as well:

    {"action":"passed",
    "adsizeUEMScores":{"ivl_300x250":15,"iviab_160x600":75,"ivl_160x600":15,"iviab_300x250":75,"ivp_300x250":55,"ivp_160x600":65,"ivp_728x90":45,"iviab_728x90":65,"ivl_728x90":15},
    "bsc":{"adt":1000,"dlm":1000,"drg":1000,"alc":1000,"hat":1000,"off":1000,"sam":1000},
    "iab1":["iab_sports"],
    "iab2":[],
    "traq":1000,
    "ttl":"2019-03-11T14:48-0400",
    "uem":{"ivp":55,"iviab":75,"top":75000,"ivt":15000,"ivu":15,"niv":25,"ivl":25},
    "uesm":[{"size":"160x600","ivl":15,"iviab":75,"ivp":65},{"size":"300x250","iviab":75,"ivp":55,"ivl":15},{"size":"728x90","iviab":65,"ivp":45,"ivl":15}]
     }
