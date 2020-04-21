// Unlike normal java programs, this has no package

import java.math.BigInteger;

/**
 * Euler task sixteen

    2^15 = 32768 and the sum of its digits is 3 + 2 + 7 + 6 + 8 = 26.

    What is the sum of the digits of the number 2^1000?

 */
public class Sixteen {
    static int[] digits = new int[302]; // 1000*log(2) says 301+
    static int arraysize=1;

    static void powerup(int pow) {
        arraysize=1;
        digits[0]=2;
        int power=1;

        while (power<pow) {
            int carry=0;
            for (int ix=0;ix<arraysize;++ix) {
                digits[ix] = digits[ix]*2 + carry;
                carry = 0;
                if (digits[ix]>9) {
                    carry = digits[ix]/10;
                    digits[ix]=digits[ix]%10;
                }
            }
            if (carry>0) {
                digits[arraysize] = carry;
                arraysize++;
            }
            ++power;
        }
    }

    static long sumdigits() {
        long result = 0;
        for (int ix=0;ix<arraysize;++ix) {
            result += digits[ix];
        }
        return result;
    }

    public static void main(String[] args) {

        long start = System.currentTimeMillis();

        powerup (1000);

        long result = sumdigits();

        long end = System.currentTimeMillis();

        System.out.println(String.valueOf(result));

        System.out.println(String.format("took %f seconds",(end-start)/1000.0));
    }
}
