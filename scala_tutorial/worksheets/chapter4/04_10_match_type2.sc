for {
  x <- Seq(List(5.5,5.6,5.7), List("a", "b"))
} yield (x match {
  case seqd: Seq[Double] => ("seq double", seqd)
  case seqs: Seq[String] => ("seq string", seqs)
  case _                 => ("unknown!", x)
})
// JVM의 type erasure 때문에 등장하는 경고다.
/*
  자바5에 제네릭을 도입하던 때부터 내려온 유산이다.
  자바 5 이전 코드와의 하위 호환성을 유지하기 위해 JVM 바이트 코드에는 List 타입과 같은 제네릭(매개변수화한) 타입의
  인스턴스에 사용했던 타입 매개변수 정보가 들어 있지 않다.
  따라서, 컴파일러는 주어진 객체가 List라는 것은 알고 있지만, "실행 시점"에 List[Double] 과 List[String]을 구별할 수 없다는 사실을 우리에게 알려준다.
  컴파일러는 두 번째 case절을 도달 불가능한 코드로 간주한다.
 */

// 타입을 매칭하는 방법 (멋지지 않지만, 컬렉션에 대해 일치시키고 head 원소에 대한 매치를 내포시킨다.)

def doSeqMatch[T](seq: Seq[T]): String = seq match {
  case Nil => "Nothing"
  case head +: _ => head match {
    case _ : Double => "Double"
    case _ : String => "String"
    case _ => "Unmatched seq element"
  }
}

for {
  x <- Seq(List(5.5,5.6,5.7), List("a", "b"), Nil)
} yield {
  x match {
    case seq: Seq[_] => (s"seq ${doSeqMatch(seq)}", seq)
    case _           => ("unknown!", x)
  }
}