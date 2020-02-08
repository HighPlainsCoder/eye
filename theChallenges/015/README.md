# Euler challenge 15

``` 


Starting in the top left corner of a 2×2 grid, and only being able to move to the right and down, there are exactly 6 routes to the bottom right corner.
           
    ![hotlinked right from projecteuler.net; forgive me](https://projecteuler.net/project/images/p015.png)

How many such routes are there through a 20×20 grid?

```

## Lessons learned
* math can save you programming as much as programming can save you math
* sometimes even long isnt long enough (for 40!)
* java BigInteger is there, but its super awkward. WTF, guys?

### the secret mathing
I observed that to get from start to finish you would have to go down 20 times and right 20 times.
That means that the path is ddddddddddddddddddddrrrrrrrrrrrrrrrrrrrr in some permutation.
well permutations is factorial, and since 20 of the terms are the same, you divide by that, and then by the other 20
same terms.  So the number sought is just 40! / 20! / 20!

The only problem left was 40! is bigger than long.  I could have trickily calculated up the numbers without busting
through long, but I havent used BigInteger in a while (14 billion years), so I chose to use that instead.

## projecteuler.net says

```
Congratulations, the answer you gave to problem 15 is correct.

You are the 177137th person to have solved this problem.

This problem had a difficulty rating of 5%
```
