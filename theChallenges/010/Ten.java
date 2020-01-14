// Unlike normal java programs, this has no package

import java.util.ArrayList;
import java.util.Arrays;
import  java.util.Comparator;

/**
 Euler task Ten

 The sum of the primes below 10 is 2 + 3 + 5 + 7 = 17.

 Find the sum of all the primes below two million.
 */


/* the Wheel helper lets us shorten the list of potential primes to check
The earliest wheel is when you check only the odd numbers.  The size is 2, and the offset is 1
When you reach 6, you can start skipping multiples of 3. The size becomes 6, and the offsets are 1 and 5
etc  Each time you pass the size of the wheel, times the size of the next prime in line, you can recalculate
a larger wheel, with a smaller proportion of eligible #s. Doesnt get rolling until about 50000 primes
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
        long currentPrime = Ten.primes.get(nextPrimeIndex);
        ++nextPrimeIndex;
        long nextPrime = Ten.primes.get(nextPrimeIndex);
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


public class Ten {
    static ArrayList<Long> primes = null;// static so the wheel can get to it
    Wheel wheel = null;



    long findPrimes(long limit,boolean rebuild) {
        primes = new ArrayList<Long>((int)limit/4); // /4 is just an estimate
        primes.add(2L);
        primes.add(3L);
        primes.add(5L); // wheel doesnt work for 2,3,or 5, so prime the list
        wheel = new Wheel(rebuild);// Wheel is built to start at size 2*3, and will first look at 7

        long sum = 2L+3L+5L;
        while(true) {
            long candidate = wheel.next();
            if (candidate>limit) break;
            for (int ix=wheel.nextPrimeIndex;;++ix) {
                long divisor = primes.get(ix);
                if (candidate%divisor==0)
                    break;
                if (candidate<divisor*divisor) {
                    primes.add(candidate);
                    sum += candidate;
                    break;
                }
            }
        }

        return sum;
    }


    public static void main(String[] args) {
        int limit = 2000000;


        Ten finder = new Ten();

        long start = System.currentTimeMillis();
        long sum = finder.findPrimes(limit,true);
        long end = System.currentTimeMillis();
        System.out.println(String.format("with wheel took %f seconds",(end-start)/1000.0));
        System.out.println(String.valueOf(sum));

        System.out.println(String.format("# primes %d %d",primes.size(), primes.get(primes.size()-1)));

    }
}
