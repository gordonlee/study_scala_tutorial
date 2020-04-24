// 2.8 리터럴 값

  // 2.8.7 함수 리터럴
val f1: (Int, String) => String         = (i, s) => i + s
val f2: Function2[Int, String, String]  = (i, s) => i + s

  // 2.8.8 튜플 리터럴
val t1: (Int, String)  = (1, "two")
val t2: Tuple2[Int, String] = (1, "two")

// 사용 예
val t = ("Hello", 1, 2.3)
println("Print whole tuple: " + t)
println("Print 1: " + t._1)
println("Print 2: " + t._2)
println("Print 3: " + t._3)

val (tv1, tv2, tv3) = ("World", '!', 0x22)
println(tv1 + ", " + tv2 + ", " + tv3)

val (tv4, tv5, tv6) = Tuple3("World", '!', 0x22)
println(tv4 + ", " + tv5 + ", " + tv6)

// pair로 원소가 둘인 튜플을 정의하는 방법은 여러 가지가 있다.
// 화살표 연산자는 원소가 둘인 튜플에서만 사용할 수 있다.
(1, "one")
1 -> "one"
Tuple2(1, "one")
