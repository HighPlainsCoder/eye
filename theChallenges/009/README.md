# Euler challenge 8

```
A Pythagorean triplet is a set of three natural numbers, a < b < c, for which,
a^2 + b^2 = c^2

For example, 3^2 + 4^2 = 9 + 16 = 25 = 5^2.

There exists exactly one Pythagorean triplet for which a + b + c = 1000.
Find the product abc.
```    

## Lets math that up

    a + b + c = 1000
    c = 1000 - a - b
    c^2 = (1000 - a - b)*(1000 - a - b) = 1000000 - 1000*a - 1000*b - 1000*a + a*a + a*b - 1000*b + a*b + b*b
        = 1000000 - 2000a - 2000b + a^2 + b^2 + 2ab
        
    a^a + b^a = c^2 = 1000000 - 2000a - 2000b + a^2 + b^2 + 2ab
    0 = 1000000 -2000a -2000b + 2ab
    0 = 500000 -1000a -1000b + ab
    1000a-ab = 500000 - 1000b
    a*(1000 - b) = 500000 - 1000b
    a = 1000*(500-b)/(1000-b)
    or
    b = 1000(500-a)/(1000-a)


## Lessons learned

* My math is rusty.  I had to do that on my fingers and toes.  I was great at this stuff. 40 YEARS AGO.
* Once the math is in place, brute is fast enough
* check your work for all the constraints (with eyeballs, partly (in a production program, with asserts/exceptions)) 
* I took the problems word that there was only one, so I stopped when I found one.  In the real world, thats a mistake.

## projecteuler.net says

```
Congratulations, the answer you gave to problem 9 is correct.

You are the 337992nd person to have solved this problem.

This problem had a difficulty rating of 5%.
```
