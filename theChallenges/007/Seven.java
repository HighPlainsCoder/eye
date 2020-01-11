// Unlike normal java programs, this has no package

import java.util.ArrayList;
import java.util.Arrays;
import  java.util.Comparator;

/**
 * Euler task Seven

 By listing the first six prime numbers: 2, 3, 5, 7, 11, and 13, we can see that the 6th prime is 13.

 What is the 10 001st prime number?
 */


/* the Wheel helper lets us shorten the list of potential primes to check
The earliest wheel is when you check only the odd numbers.  The size is 2, and the offset is 1
When you reach 6, you can start skipping multiples of 3. The size becomes 6, and the offsets are 1 and 5
etc  Each time you pass the size of the wheel, times the size of the next prime in line, you can recalculate
a larger wheel, with a smaller proportion of eligible #s
 */
class Wheel {
    long currentSize;
    long nextSize;
    int nextPrimeIndex;

    long currentBase;
    int currentOffset;

    ArrayList<Long> offsets;
    boolean rebuild;

    Wheel(boolean rebuild) {
        currentSize = 6L;
        nextSize = 2L*3L*5L;
        nextPrimeIndex = 2;
        currentBase = 6L;
        currentOffset=0;
        offsets = new ArrayList(Arrays.asList(1L,5L));
        this.rebuild = rebuild;
    }

    // Thats a lot of code to save a few divides.  If I didnt already have this hanging around, I wouldnt use it here
    void rebuildWheel() {
        long oldSize = currentSize;
        currentSize = nextSize;
        long currentPrime = Seven.primes.get(nextPrimeIndex);
        ++nextPrimeIndex;
        long nextPrime = Seven.primes.get(nextPrimeIndex);
        nextSize = nextSize*nextPrime;
        currentBase = currentSize;
        currentOffset = 0;


        long halfSize = currentSize/2;
        ArrayList<Long> old = new ArrayList(offsets);
        offsets.clear();

        int oo = 0;
        long oldbase = 0;
        while(true) {
            long offset = oldbase + old.get(oo);
            if (offset>halfSize)
                break;
            if (++oo>=old.size()) {
                oo=0;
                oldbase += oldSize;
            }
            if (offset%currentPrime!=0)
                offsets.add(offset);
        }


        int half = offsets.size()-1;  // must reify, cause offsets.size() will be changing
        for (int ix=half;ix>=0;--ix)
            offsets.add(currentSize-offsets.get(ix));

        //System.out.println(offsets.toString());
    }


    long next() {
        if (currentOffset>=offsets.size()) {
            currentBase += currentSize;
            currentOffset = 0;
            //note: comment out this if block to see without wheel timing
            if (rebuild && currentBase >= nextSize) {
                rebuildWheel();
            }
        }

        long result = currentBase + offsets.get(currentOffset);
        ++currentOffset;
        return result;
    }
}


public class Seven {
    static ArrayList<Long> primes = null;// static so the wheel can get to it
    Wheel wheel = null;



    void findPrimes(int target,boolean rebuild) {
        primes = new ArrayList<Long>(target);
        primes.add(2L);
        primes.add(3L);
        primes.add(5L); // wheel doesnt work for 2,3,or 5, so prime the list
        wheel = new Wheel(rebuild);// Wheel is built to start at size 2*3, and will first look at 7
        while(primes.size()<target) {
            long candidate = wheel.next();
            for (int ix=wheel.nextPrimeIndex;;++ix) {
                long divisor = primes.get(ix);
                if (candidate%divisor==0)
                    break;
                if (candidate<divisor*divisor) {
                    primes.add(candidate);
                    break;
                }
            }
        }
    }


    public static void main(String[] args) {
        int target = 50001;


        Seven seven = new Seven();


        long start = System.currentTimeMillis();
        seven.findPrimes(target,false);
        long end = System.currentTimeMillis();
        System.out.println(String.format("without wheel took %f seconds",(end-start)/1000.0));
        System.out.println(String.valueOf(seven.primes.get(target-1)));


        start = System.currentTimeMillis();
        seven.findPrimes(target,true);
        end = System.currentTimeMillis();
        System.out.println(String.format("with wheel took %f seconds",(end-start)/1000.0));
        System.out.println(String.valueOf(seven.primes.get(target-1)));
    }
}
