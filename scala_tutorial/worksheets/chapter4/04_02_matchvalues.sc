for {
  x <- Seq(1, 2, 2.7, "one", "two", 'four)                           // <1>
} {
  val str = x match {                                                // <2>
    case 1          => "int 1"                                       // <3>
    case i: Int     => "other int: "+i                               // <4>
    case d: Double  => "a double: "+x                                // <5>
    case "one"      => "string one"                                  // <6>
    case s: String  => "other string: "+s                            // <7>
    case unexpected => "unexpected value: " + unexpected             // <8>
  }
  println(str)                                                       // <9>
}



for {
  x <- Seq(1, 2, 2.7, "one", "two", 'four)
} {
  val str = x match {
    case 1          => "int 1"
    case _: Int     => "other int: "+x
    case _: Double  => "a double: "+x
    case "one"      => "string one"
    case _: String  => "other string: "+x
    case _          => "unexpected value: " + x
  }
  println(str)
}
// _ 위치 지정자로 치환이 가능하다. 또한 x를 사용할 수 있다.


/*
  case 다음에 대문자로 시작하는 이름이 오면 타입 이름으로 간주한다.
  소문자로 시작하는 이름은 매치 또는 추출한 값을 담을 변수로 취급한다.
 */
def checkY(y: Int) = {
  for {
    x <- Seq(99, 100, 101)
  } {
    val str = x match {
      // case y => "found y!"
      case `y` => "found y!"
      case i: Int => "int: "+i
    }
    println(str)
  }
}
checkY(100)
// y 값을 매치 시키고 싶을 때는 위와같은 형식을 취해야 한다.


// 또한 아래처럼 OR 연산을 처리할 수도 있다.
for {
  x <- Seq(1, 2, 2.7, "one", "two", 'four)
} {
  val str = x match {
    case _: Int | _: Double => "a number: "+x
    case "one"              => "string one"
    case _: String          => "other string: "+x
    case _                  => "unexpected value: " + x
  }
  println(str)
}
