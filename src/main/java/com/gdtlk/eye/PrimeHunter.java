package com.gdtlk.eye;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;


class PrimeHunter {
	private ArrayList<Integer> primes = new ArrayList(Arrays.asList(2,3,5));
    private Integer wheel = 6; //2*3
    private ArrayList<Integer> offsets = new ArrayList(Arrays.asList(1,5));
    private int firstPrimeIx = 2; // i.e. 5
    private int lastPrimeIx = 2;
    private Integer topPrimeSquared = 25;

    private Integer start = 1*wheel;


    void calculate() {
        while (true) {
            for (Integer o : offsets) {
                Integer candidate = start + o;
                while (candidate >= topPrimeSquared) {
                    lastPrimeIx++;
                    topPrimeSquared = primes.get(lastPrimeIx+1) * primes.get(lastPrimeIx+1);
                }
                boolean prime = check(candidate);
                if (prime)
                    primes.add(candidate);
            }

            start += wheel;

            if (start >= wheel * primes.get(firstPrimeIx)) {
                rebuildWheel();
            }
        }
    }

	boolean check(Integer candidate) {
        for(int ix = firstPrimeIx; ix<=lastPrimeIx;++ix) {
            Integer test = primes.get(ix);
            Integer remainder = candidate % test;
            if (remainder == 0)
                return false;
        }
        return true;
	}

	void rebuildWheel() {
        wheel *= primes.get(firstPrimeIx);
        firstPrimeIx++;
        offsets.clear();
        offsets.add(1);

        for(int pix=firstPrimeIx;pix<primes.size() && primes.get(pix)<wheel;++pix) {
            offsets.add(primes.get(pix));
        }

        int o1;
        int o2;
        for(o1=1;o1<offsets.size();++o1) {
            Integer oo = offsets.get(o1)*offsets.get(o1);
            if (oo>wheel)
                break;
            offsets.add(oo);
            for(o2=o1+1;o2<offsets.size();++o2) {
                oo = offsets.get(o1)*offsets.get(o2);
                if ((oo>wheel)) {
                    break;
                }
                offsets.add(oo);
            }
        }

        offsets.sort(new Comparator<Integer>() {
            public int compare(Integer a, Integer b) { return a-b; }
        });
	}
	
	void run() {
        calculate();
	}
}