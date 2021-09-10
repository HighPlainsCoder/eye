/**
object qsort extends App {
	def sort(xs: Array[Int]) {
		def swap(i: Int, j: Int) {
		    if (xs(i)!=xs(j)) {
			    printar(xs)
			    printij(i,j)
				val t = xs(i); xs(i) = xs(j); xs(j) = t
		    }
		}

		def sort1(l: Int, r: Int) {
			val pivot = xs((l + r) / 2)
			var i = l
			var j = r
			while (i <= j) {
				while (xs(i) < pivot) i += 1
				while (xs(j) > pivot) j -= 1
				if (i<j)
				  swap(i, j)
				if (i <= j) {
					i += 1
					j -= 1
				}
			}
			if (l < j) sort1(l, j)
			if (j < r) sort1(i, r)
		}

		sort1(0, xs.length - 1)
		printar(xs)
	}


	def below ( xs : List[Int], first: Int ): List[Int] =
	{
		val isBelow  =  ( y : Int )  =>   y < first
		for(  x <- xs if isBelow( x )  )  yield x
	}

	def printar(ar: Array[Int]) {
      def print1 = {
        def iter(i: Int): String =
          ar(i) + (if (i < ar.length-1) "," + iter(i+1) else "")
        if (ar.length == 0) "" else iter(0)
      }
      println("[" + print1 + "]")
    }

	def printij(i:Int, j:Int) {
	    def spaces(x:Int): String =
	        if (x>0) " " + spaces(x-1) else ""

	    println( spaces ( 2*i + 1) + "-" + (if (j>i) spaces(2*(j-i)-1) + "-" else "") )
	}

    def even(from: Int, to: Int): List[Int] =
        for (i <- List.range(from, to) if i % 2 == 0) yield i


    println(even(0, 20))



	val q1 = List( 5, 1, 7, 4, 9, 11, 3 )
    val a1 = below( q1, 5 )      // => List( 1, 4, 3 )
	println( q1 )
	println( a1 )

	val q2 = Array(1,6,3,5,8,6,7,5,7,8,2,3,7,4,7,5,7,0,0,0,8,7,8,8,9,8,5,4,2,2,2,5,6,3,4)
	sort( q2 )


}



**/
