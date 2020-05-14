import scala.collection.immutable.HashMap
// 2.6 타입 추론

val intToStringMap: HashMap[Integer, String] = new HashMap
val intToStringMap2 = new HashMap[Integer, String]

/*
  일부 함수형 프로그래밍 언어는 전역 타입 추론을 사용해서 거의 모든 타입을 추론할 수 있다.
  그러나, scala는 서브타입 다형성(subtype polymorphism) 즉 상속을 지원해야 해서 그렇게 할 수 없다.

  언제 명시적으로 타입을 표기해야 하는가?
  - var, val 선언에서 값을 대입하지 않는 경우
  - 모든 메서드 매개변수
  - 다음과 같은 메서드 반환 타입의 경우
    - 메서드 안에서 return을 명시적으로 호출하는 경우
    - 매서드가 재귀적인 경우
    - overloading한 둘 이상의 메서드가 있고, 그중 한 메서드가 다른 메서드를 호출하는 경우
    - 컴파일러가 추론한 타입이 코더의 의도보다 더 일반적인 경우 (ex> Any)
 */

// overloading 메서드가 다른 동일 이름의 메서드를 호출할 경우
object StringUtilV1 {
  def joiner(strings: String*): String = strings.mkString("-")
  // def joiner(strings: List[String]) = joiner(strings: _*) // 컴파일 오류
  def joiner(strings: List[String]):String = joiner(strings: _*) // 컴파일 오류
}
println( StringUtilV1.joiner(List("P", "S")))
// strings :_* 는 컴파일러에게 주는 힌트로 strings 라는 리스트를 가변 인자 목록(*)으로 다루되,
// 타입은 알 수 없지만 추론한 타입(:_)을 사용하라는 뜻이다.
// 실제 상황에서는 따로 joiner를 만들지 말고 가변 인자 목록을 사용처에서 활용하는 것이 나을 수 있다.

// Any가 추론되는 케이스
def makeList(strings: String*) = {
  if (strings.length == 0)
    List(0) // 0이라는 원소가 하나 들어있는 List[Int] 를 반환한다.
  else
    strings.toList
}

// val list: List[String] = makeList()
val list = makeList()
// 중요한 것은 이 내용이 컴파일이 된다는 것이다.
// 사용하는 쪽과 별도로 빌드되는 API를 만드는 경우, 메서드 타입을 할 수 있는 한 가장 일반적인 타입으로 명시하라.

//마지막으로 실수하기 쉬운 타입
def double(i: Int) { 2 * i }
println(double(2))

def double2(i: Int) = { 2 * i}
println(double2(2))
// 등호가 없으면 return type은 Unit 으로 잡히게 된다.
// 또한, 이러한 프로시져 문법은 scala 2.11 부터 deprecated 되었다.
