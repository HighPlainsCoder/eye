Euler challenge 4

```
A palindromic number reads the same both ways. The largest palindrome made from the product of two 2-digit numbers is 9009 = 91 × 99.

Find the largest palindrome made from the product of two 3-digit numbers.
```

Lessons learned:
* math helps.
  * I mathed that I could best search from the top, diagonally
  * then that I only had to search the top 1/2 of each diagonal
  * then that the numbers in a diagonal descended from the center to the edge
  * it wasnt match, but algorithm that decided that even and odd diagonals were different enough to handle differently, that is why each inner loop looks at C*D and also C*(D-1)
* Java is not C++.  I first made my loops in the C++ syntax (even though that is 10 years behind me)
* math isnt fully needed.  This algorithm continues for a bit after it finds a palindrome, in case it finds another, larger one. Because Im didnt list all the candidates in 100% descending order, just mostly.  

* projecteuler.net says:

```
Congratulations, the answer you gave to problem 4 is correct.

You are the 457721st person to have solved this problem.

This problem had a difficulty rating of 5%.
```
