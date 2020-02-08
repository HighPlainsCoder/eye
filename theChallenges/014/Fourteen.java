// Unlike normal java programs, this has no package



/**
 * Euler task fourteen



 The following iterative sequence is defined for the set of positive integers:

 n → n/2 (n is even)
 n → 3n + 1 (n is odd)

 Using the rule above and starting with 13, we generate the following sequence:
 13 → 40 → 20 → 10 → 5 → 16 → 8 → 4 → 2 → 1

 It can be seen that this sequence (starting at 13 and finishing at 1) contains 10 terms. Although it has not been proved yet (Collatz Problem), it is thought that all starting numbers finish at 1.

 Which starting number, under one million, produces the longest chain?

 NOTE: Once the chain starts the terms are allowed to go above one million.

 */
public class Fourteen {
    static long longestStarter = 0L;
    static long longestLength = 0L;
    static long[] lengths = new long[1000000];

    static long collatz(long candidate) {
        if (candidate<0L)
            throw new RuntimeException("Drat! rolled over longs");

        if (candidate<1000000L && lengths[(int)candidate]>0L)
            return lengths[(int)candidate];

        long result;

        if (candidate%2L == 0L)
            result = 1L+collatz(candidate/2L);
        else
            result = 1L+collatz(3L*candidate+1L);

        if (candidate<1000000L)  {
            lengths[(int)candidate]=result;
            if (result>longestLength) {
                longestLength=result;
                longestStarter=candidate;
            }
        }

        return result;
    }




    public static void main(String[] args) {

        long candidate = 999999L;
        lengths[1]=1L;
        long start = System.currentTimeMillis();

        for (;candidate>1L;--candidate)
            collatz(candidate);

        long end = System.currentTimeMillis();

        System.out.println(String.valueOf(longestStarter));
        System.out.println(String.valueOf(longestLength));

        System.out.println(String.format("took %f seconds",(end-start)/1000.0));
    }
}
