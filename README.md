# Euler
This is the Project Euler challenge

https://projecteuler.net/

The challenge has 2 parts:
* Do the puzzles
* record the results, on Twitter, in Github

Actual rules at  https://www.freecodecamp.org/news/projecteuler100-coding-challenge-competitive-programming/

* Tweet out a photo of yourself giving a thumbs-up and announcing that you are committing to the #ProjectEuler100 challenge.
* Create a GitHub repository.
* Each time you complete a challenge, add your solution to your GitHub repository and tweet a link to it using the #ProjectEuler100 hashtag.
* Then scroll through the #ProjectEuler100 hashtag and give supportive feedback on at least 2 tweets from other developers.
* Move on to the next Project Euler challenge. You can't skip ahead. You have to complete all 100 problems in order. But you can use any programming language you want to solve these.
* Once you've finished all 100 of them, tweet out a celebration photo of yourself with your laptop open to your GitHub repo.



## process

I made(will make) a subproject for each challenge.

Since each one is a small program, I made a tiny gradle file for them to share. By rights I could use the java compiler & execute directly, but gradle is what I use in my day job and I like to keep fresh.

Execute each challenge like this (assumes you have gradle around)
    gradle assemble && java -cp build/libs/euler.jar One 
    
or, if you have Java 11 or later. just compile and go:
    java One.java
    
(After just seconds of reflection) Hell, lets make that the one true way.  Pruning all gradle now...

