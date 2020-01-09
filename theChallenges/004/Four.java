// Unlike normal java programs, this has no package



/**
 * Euler task Four

 A palindromic number reads the same both ways. The largest palindrome made from the product of two 2-digit numbers is 9009 = 91 × 99.

 Find the largest palindrome made from the product of two 3-digit numbers.
 */
public class Four {
    static boolean isPalindrome(int testee) {
        int[] digits = new int[12]; // 12 is plenty for the range of ints
        int ix=0;
        while (testee>0) {
            int digit = testee%10;
            testee/=10;
            digits[ix]=digit;
            ++ix;
        }

        int jj=0;
        --ix;
        for (;jj<ix;++jj , --ix) {
            if (digits[jj]!=digits[ix])
                return false;
        }
        return true;
    }

    static int largestPalindrome(int bottom, int top) {
        int A = top;
        int found=-1;
        while (A>=bottom) {
            if (A*A<found) // if you have found one, and A*A is < found, you will have the largest
                return found;
            int C=A;
            int D=A;
            while (C<=top) {

                int checkme=C*D;
                System.out.println(String.format("%d %d %d",C,D,checkme));
                if (isPalindrome(checkme)) {
                    if (found<checkme) {
                        found=checkme;
                        System.out.println("found!");
                    }
                }
                checkme=C*(D-1);
                System.out.println(String.format("%d %d %d",C,D-1,checkme));
                if (isPalindrome(checkme)) {
                    if (found<checkme) {
                        found=checkme;
                        System.out.println("found!");
                    }
                }
                C++;
                D--;
            }
            --A;
        }
        return found;
    }

    public static void main(String[] args) {
        int largest = largestPalindrome(100,999);
        System.out.println(String.format("Largest palindrome %d",largest));
    }
}
