// BEGIN FOR1
// src/script/scala/progscala3/patternmatching/MatchBoolean.scala

val bools = Seq(true, false)

for (bool <- bools)
  bool match {
    case true => println("Got heads")
    case false => println("Got tails")
  }
// END FOR1

// case 절이 없으면? -> 경고가 발생한다. MatchError

// BEGIN FOR_IF
for (bool <- bools) {
  val which = if (bool) "head" else "tails"
  println("Got " + which)

}

// END FOR_IF

// BEGIN FOR_PARTIAL
bools foreach {
  case true => println("Got heads")
  case false => println("Got tails")
}
// END FOR_PARTIAL

// BEGIN FOR_BAD
for (bool <- bools) {
      // bool match {}
  case true => println("Got heads")
  case false => println("Got tails")
}
// END FOR_BAD