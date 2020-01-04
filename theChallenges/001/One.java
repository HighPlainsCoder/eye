// Unlike normal java programs, this has no package



/**
 * One, the first Euler task

If we list all the natural numbers below 10 that are multiples of 3 or 5,
we get 3, 5, 6 and 9. The sum of these multiples is 23.

Find the sum of all the multiples of 3 or 5 below 1000.


 */
public class One {
    public static void main(String[] args) {

        //first, just count them up. For 1000, this wont take long.
        int sum=0;
        for (int ii=1;ii<1000;++ii) {
            if (ii%3 == 0 || ii%5 == 0)
                sum += ii;
        }
        System.out.println("sum of multiples of 3 or 5 below 1000=" + String.valueOf(sum) );


        // second, mostly for double-check, count just through the multiples of 3 and 5
        Integer[] offsets = {0,3,5,6,9,10,12};
        int fifteener=0;
        sum=0;
        boolean done=false;
        while(!done) {
            for(Integer off : offsets) {
                if (fifteener+off>=1000) {
                    done=true;
                    break;
                }
                sum += fifteener+off;
            }
            fifteener+=15;
        }
        System.out.println("2nd sum of multiples of 3 or 5 below 1000=" + String.valueOf(sum) );

    }
}
