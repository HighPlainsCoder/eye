// Unlike normal java programs, this has no package

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Three, the third Euler task

 The prime factors of 13195 are 5, 7, 13 and 29.

 What is the largest prime factor of the number 600851475143 ?
 */

class Wheel {
    int offset=0;
    long wheelbase=30;
    long current;
    ArrayList<Long> offsets = new ArrayList(Arrays.asList(1L,7L,11L,13L,17L,19L,23L,29L));

    public long next() {
        current = wheelbase + offsets.get(offset);
        offset++;
        if (offset>=offsets.size()) {
            offset=0;
            wheelbase+=30;
        }
        return current;
    }
}




public class Three {
    ArrayList<Long> primes = new ArrayList(Arrays.asList(2L,3L,5L,7L,9L,11L,13L,17L,19L,23L,29L));
    Wheel wheel = new Wheel();

    // find the next prime #, and add it to the array of primes
    long findAnotherPrime() {
        while(true) {
            long candidate =  wheel.next();
            for (int ix=3;ix<primes.size();++ix) {
                long divisor = primes.get(ix);
                if (candidate%divisor==0)
                    break;
                if (candidate<divisor*divisor) {
                    primes.add(candidate);
                    System.out.println(candidate);
                    return candidate;
                }
            }
        }
    }

    // I can see with my eyes that 2 wont divide this, but for generality, I check it anyhow
    int primeIndex = -1;
    long getNextPrime() {
        primeIndex++;
        if (primeIndex>=primes.size())
            return findAnotherPrime();
        return primes.get(primeIndex);
    }

    void findLargestFactor(long target) {
        long largest=0;
        boolean done=false;
        primeIndex=-1;//restart the search every time
        while(!done) {
            long prime = getNextPrime();
            while (target % prime == 0)  {
                target = target / prime;
                System.out.println(" / " + String.valueOf(prime));
            }

            if (target == 1) {
                largest = prime;
                break;
            } else if (prime*prime>target) {
                largest = target;
                break;
            }
        }

        System.out.println ("Largest factor = " + String.valueOf(largest));
    }


    public static void main(String[] args) {

        Three three = new Three();

        //three.findLargestFactor(31L);

        //three.findLargestFactor(31L*31L*37L);

        long target = 600851475143l;

        three.findLargestFactor(target);

    }
}
