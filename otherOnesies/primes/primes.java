/** groovy primes

List primes = [2,3,5,7,11,13,17,19,23,29]
List offsets = [1,7,11,13,17,19,23,29]
def wheel = 30

while ( true  ) {
	for (offset in offsets) {
        def candidate = wheel + offset
        print candidate
        for(p in primes) {
            if (candidate % p == 0) {
                println " is divisible by " + p
                break;
            }
            if (p*p > candidate) {
            	println " is prime"
            	primes.add(candidate)
            }
        }
    }
    wheel += 30
}


**/
