// Unlike normal java programs, this has no package

import java.util.TreeMap;
import java.util.Map;

/** Euler task five

 2520 is the smallest number that can be divided by each of the numbers from 1 to 10 without any remainder.

 What is the smallest positive number that is evenly divisible by all of the numbers from 1 to 20?

 */
public class Five {
    static int[] primes = {2,3,5};// to get to 20, only need these
    static TreeMap<Integer,Integer> primesNeeded = new TreeMap<>();

    // add this to primesNeeded, if its > whats in there
    static void countUp(int prime, int count) {
        if (!primesNeeded.containsKey(prime))
            primesNeeded.put(prime,count);
        else {
            if (primesNeeded.get(prime)<count)
                primesNeeded.put(prime, count);
        }
    }

    // find all the prime factors of this number
    static void factor(int number) {
        while (number>1) {
            for (int ix=0;ix<primes.length;++ix) {
                int p = primes[ix];
                int count=0;
                while (number%p==0) {
                    ++count;
                    number /= p;// at this point, I could take the reduced number and have stores its counts of primes, but that complicates the program too much for this small input
                }
                countUp(p,count);
            }
            if (number>1)  {
                countUp(number,1);// if there is a residue, its because this residue is prime
                number=1;
            }
        }
    }

    // for this one, I just hard coded the 20, and above, an array of primes that only goes far enough for 20
    public static void main(String[] args) {
        //it feels like cheating to just say 2^4 * 3^2 * 5 * 7 * 11 * 13 * 17 * 19

        for (int ix=2;ix<=20;++ix)
            factor(ix);

        int result = 1;

        for (Map.Entry e : primesNeeded.entrySet()) {
            for (int i=0;i<(Integer)e.getValue();++i)
                result *= (Integer)e.getKey();
        }

        System.out.println (String.valueOf(result));
    }
}
