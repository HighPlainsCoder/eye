# Euler challenge 7

``` 
By listing the first six prime numbers: 2, 3, 5, 7, 11, and 13, we can see that the 6th prime is 13.

What is the 10 001st prime number?
```

## Lessons learned:
* Dont throw away code! I happened to have prime finding programs from way back; they were easy to adapt to this (although the program itself is too heavy).
* Dont get too fancy.  The fancy "Wheel" class designed to save a few divides saves (for this example) about 6/1000ths seconds, and has a BUG in it.  (Less a bug, more a fatal design flaw). Undeterred, I blocked it out, and will return to rewrite it later on.
* For small values of time, timing means nothing.  The difference between one run another of the same code is more than the difference between 2 versions. 

## projecteuler.net says:

```
Congratulations, the answer you gave to problem 7 is correct.

You are the 398110th person to have solved this problem.

This problem had a difficulty rating of 5%.
``` 

## Round 2

I couldnt leave that wrong `rebuildWheel` function as a blot on my escutcheon, so I rethought it.  Now, a simpler function
exists, which is correct, AND simpler, AND possibly more efficient. Certainly it needs less input from the outerworld.
So I have a different blot on my escutcheon, one that I can never erase.

By running with and without, I am able to verify the function, and get timings.  At about 70,000 the thing becomes
undeniably faster with.
