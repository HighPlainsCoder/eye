// Sieve of Eratosthenes
package com.gdtlk.eye;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;


// for each prime:

class Sieve {
    private Integer prime;
    private Integer multiple;
    public boolean composite(Integer candidate) {
        assert((candidate-multiple)/prime < 5);// check if we get way far behind (I bet we do, eventually)
        // alt: multiple += prime * (int)((candidate-multiple)/prime);
        while (candidate > multiple)  // expect 0 or 1 times
            multiple += prime;
        return multiple == candidate;
    }

    public Sieve(Integer p) {
        prime=p;
        multiple=p;
    }


    public static void main(String[] args) {
        Map<Integer,Sieve> sieves = new TreeMap<Integer,Sieve>();
        sieves.put(2,new Sieve(2));

        //instead of +=2, use the wheel from other
        // and then prune wheel components from the sieves
        for (Integer candidate=3;candidate<1000000;candidate+=2) {
            for (Entry<Integer, Sieve> ss : sieves.entrySet()) {
                if (ss.getValue().composite(candidate))
                    continue;

                sieves.put(candidate,new Sieve(candidate));
                System.out.println(candidate.toString());
            }
        }
    }




}


