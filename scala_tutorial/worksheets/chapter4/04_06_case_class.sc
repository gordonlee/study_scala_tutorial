// src/main/scala/progscala2/patternmatching/match-deep.sc

// Simplistic address type. Using all strings is questionable, too.
case class Address(street: String, city: String, country: String)
case class Person(name: String, age: Int, address: Address)

val alice   = Person("Alice",   25, Address("1 Scala Lane", "Chicago", "USA"))
val bob     = Person("Bob",     29, Address("2 Java Ave.",  "Miami",   "USA"))
val charlie = Person("Charlie", 32, Address("3 Python Ct.", "Boston",  "USA"))

for (person <- Seq(alice, bob, charlie)) {
  person match {
    case Person("Alice", 25, Address(_, "Chicago", _)) => println("Hi Alice!")
    case Person("Bob", 29, Address("2 Java Ave.", "Miami", "USA")) =>
      println("Hi Bob!")
    case Person(name, age, _) =>
      println(s"Who are you, $age year-old person named $name?")
  }
}



val itemsCosts = Seq(("Pencil", 0.52), ("Paper", 1.35), ("Notebook", 2.43))
val itemsCostsIndices = itemsCosts.zipWithIndex
for (itemCostIndex <- itemsCostsIndices) {
  itemCostIndex match {
    case ((item, cost), index) => println(s"$index: $item costs $cost each")
  }
}

/*
  4.6.1 unapply method
  match - case 절에 obj를 만났을 때, 스칼라는 해당 객체의 unapply(...) 메서드를 찾아서 호출한다.
  unapply 메서드는 Option[TupleN[]] 을 반환한다.
  이 말은 즉 class -> tuples 로 만드는 작업을 unapply, tuples -> class 로 만드는 작업을 apply 라고 할 수 있다.

 */

case class With[A,B](a: A, b: B)

// val fw1 = "Foo" With 1       // Doesn't work

val with1: With[String,Int] = With("Foo", 1)
val with2: String With Int  = With("Bar", 2)

List(with1, with2) foreach { w =>
  w match {
    case s With i => println(s"$s with $i")
    case _ => println(s"Unknown: $w")
  }
}


// 역순으로 조립할 수 있는데,
// List의 경우 원소를 뒤에 추가하는 :+ 메서드와 패턴 매치를 위한 :+ 객체는 리스트를 맨 앞에서부터 순회해야 하기 때문에
// 모두 O(n) 의 시간이 걸린다. 하지만, Vector와 같은 일부 다른 시퀀스의 경우에는 O(1)이 될 수되 있다.
val nonEmptyList   = List(1, 2, 3, 4, 5)
val nonEmptyVector = Vector(1, 2, 3, 4, 5)
val nonEmptyMap    = Map("one" -> 1, "two" -> 2, "three" -> 3)

def reverseSeqToString[T](l: Seq[T]): String = l match {
  case prefix :+ end => reverseSeqToString(prefix) + s" :+ $end"
  case Nil => "Nil"
}

for (seq <- Seq(nonEmptyList, nonEmptyVector, nonEmptyMap.toSeq)) {
  println(reverseSeqToString(seq))
}


/*
  4.6.2 unapplySeq
  미리 정해진 개수의 원소를 반환하는 대신 추출한 원소의 시퀀스를 반환하고 싶다면 어떻게 할까?
  -> 가변인자가 필요하다.
 */


// val nonEmptyList   = List(1, 2, 3, 4, 5)                             // <1>
val emptyList      = Nil
// val nonEmptyMap    = Map("one" -> 1, "two" -> 2, "three" -> 3)

// Process pairs
def windows[T](seq: Seq[T]): String = seq match {
  case Seq(head1, head2, _*) =>                                      // <2>
    s"($head1, $head2), " + windows(seq.tail)                        // <3>
  case Seq(head, _*) =>
    s"($head, _), " + windows(seq.tail)                              // <4>
  case Nil => "Nil"
}

for (seq <- Seq(nonEmptyList, emptyList, nonEmptyMap.toSeq)) {
  println(windows(seq))
}

// 앞에서 봤던 +: 를 사용할 수도 있다.

// Process pairs
def windows2[T](seq: Seq[T]): String = seq match {
  case head1 +: head2 +: tail => s"($head1, $head2), " + windows2(seq.tail)
  case head +: tail => s"($head, _), " + windows2(tail)
  case Nil => "Nil"
}

for (seq <- Seq(nonEmptyList, emptyList, nonEmptyMap.toSeq)) {
  println(windows2(seq))
}

// 슬라이딩 윈도우가 유용하기 때문에 이미 내장 메소드를 제공한다.
val seq = Seq(1,2,3,4,5)
val slide2 = seq.sliding(2)
slide2.toSeq
slide2.toList
seq.sliding(3,2).toList

