/* functional set of primes
 set is a class with a 'contains' method
 and a structure of all the primes found to date
*/

import java.util.Iterator;



class functionalPrimeSet {

    public boolean contains(int i) {
        return contains(Long.valueOf(i));
    }

    public boolean contains(Long c) {
        return false;
    }

    public Iterator<Long> all() {
        throw new UnsupportedOperationException("yet");
    }






    public static void main(String[] args) {
        functionalPrimeSet ff = new functionalPrimeSet();

        System.out.println(ff.contains(-11));

        System.out.println(ff.contains(2));

        System.out.println(ff.contains(3));
    }

}


