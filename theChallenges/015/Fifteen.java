// Unlike normal java programs, this has no package

import java.math.BigInteger;

/**
 * Euler task fifteen



 Starting in the top left corner of a 2×2 grid, and only being able to move to the right and down, there are exactly 6 routes to the bottom right corner.

 <Lovely diagram didnt translate>

 How many such routes are there through a 20×20 grid?

 */
public class Fifteen {

    static BigInteger factorial(BigInteger n) {

        if (n.equals(BigInteger.ONE))
            return BigInteger.ONE;

        return n .multiply( factorial(n.subtract(BigInteger.ONE)) );
    }

    public static void main(String[] args) {

        long start = System.currentTimeMillis();

        BigInteger fac40 = factorial(BigInteger.valueOf(40));
        BigInteger fac20 = factorial(BigInteger.valueOf(20));

        BigInteger result = fac40.divide(fac20).divide(fac20);

        long end = System.currentTimeMillis();

        System.out.println(String.valueOf(result));

        System.out.println(String.format("took %f seconds",(end-start)/1000.0));
    }
}
