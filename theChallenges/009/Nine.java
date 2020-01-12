// Unlike normal java programs, this has no package



/** Euler task nine




 A Pythagorean triplet is a set of three natural numbers, a < b < c, for which,
 a^2 + b^2 = c^2

 For example, 3^2 + 4^2 = 9 + 16 = 25 = 5^2.

 There exists exactly one Pythagorean triplet for which a + b + c = 1000.
 Find the product abc.
 */

public class Nine {


    public static void main(String[] args) {

        int a;

        for(a=1;a<334;++a) {
            // 1000(500-a)/(1000-a)
            int num = 1000*(500-a);
            int denom = 1000-a;
            if (num%denom!=0) continue;

            int b = num / denom;
            int c = 1000 - a - b;

            System.out.println(String.format("%d %d %d %d %d %d %d %d", a, b, c, a*a, b*b, a*a+b*b, c*c, a*b*c));
            break;
        }
    }
}
