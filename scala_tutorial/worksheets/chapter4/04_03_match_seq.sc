import scala.annotation.tailrec
// src/main/scala/progscala2/patternmatching/match-seq.sc

val nonEmptySeq    = Seq(1, 2, 3, 4, 5)                              // <1>
val emptySeq       = Seq.empty[Int]
val nonEmptyList   = List(1, 2, 3, 4, 5)                             // <2>
val emptyList      = Nil
val nonEmptyVector = Vector(1, 2, 3, 4, 5)                           // <3>
val emptyVector    = Vector.empty[Int]
val nonEmptyMap    = Map("one" -> 1, "two" -> 2, "three" -> 3)       // <4>
val emptyMap       = Map.empty[String,Int]


def seqToString[T](seq: Seq[T]): String = seq match {                // <5>
  case head +: tail => s"$head +: " + seqToString(tail)              // <6>
  //case head +: tail => s"($head +: ${seqToString(tail)})"
  case Nil => "Nil"                                                  // <7>
}

for (seq <- Seq(                                                     // <8>
  nonEmptySeq, emptySeq, nonEmptyList, emptyList,
  nonEmptyVector, emptyVector, nonEmptyMap.toSeq, emptyMap.toSeq)) {
  println(seqToString(seq))
}

// list 를 위한 Nil 객체가 있음.

def seqToString2[T](seq: Seq[T]): String = seq match {                // <5>
  //case head +: tail => s"$head +: " + seqToString(tail)              // <6>
  case head +: tail => s"($head +: ${seqToString2(tail)})"
  case Nil => "Nil"                                                  // <7>
}
for (seq <- Seq(                                                     // <8>
  nonEmptySeq, emptySeq, nonEmptyList, emptyList,
  nonEmptyVector, emptyVector, nonEmptyMap.toSeq, emptyMap.toSeq)) {
  println(seqToString2(seq))
}
// 괄호를 묶어 풀리는 모습을 볼 수 있다.

def listToString[T](list: List[T]): String = list match {
  case head :: tail => s"($head :: ${listToString(tail)})"           // <1>
  case Nil => "(Nil)"
}

for (l <- List(nonEmptyList, emptyList)) { println(listToString(l)) }

val s1 = (1 :: (2 :: (3 :: (4 :: (5 :: (Nil))))))

val s2 = (("one",1) +: (("two",2) +: (("three",3) +: Nil)))

