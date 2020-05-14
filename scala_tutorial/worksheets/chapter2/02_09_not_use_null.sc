// 2.9 Option, Some, None: null 사용 피하기

/*
  Option: 추상 클래스로 값이 있는 상황인 Some과 값이 없는 상황을 표현하는 None이 있다.
  Some: 값이 있는 상황
  None: 값이 없는 상황

  기존 언어에서 null 을 이용해야 하는 경우가 있다면 사용을 고려해보자
  https://alvinalexander.com/scala/using-scala-option-some-none-idiom-function-java-null/
 */

val stateCapitals = Map(
  "Alabama" -> "Montgomery",
  "Alaska" -> "Juneau",
  "Wyoming" -> "Cheyenne"
)


stateCapitals.get("Wyoming")  // Option[String]
stateCapitals.get("Wyoming").get  // String
//possible like
stateCapitals("Wyoming")

stateCapitals.get("Wyoming_notfound") // Option[String]
// stateCapitals("Wyoming_notfound") // java.util.NoSuchElementException
// stateCapitals.get("Wyoming_notfound").get // Exception!
  // java.util.NoSuchElementException
stateCapitals.getOrElse("Wyoming", "Oops!")  // String
stateCapitals.getOrElse("Wyoming_notfound", "Oops2!")  // String

// 다른 예제
val str = Some[String](1234.toString)
val i = Some(1234)
val i2 = Some[String](null)  //  값이 있다고 판단한다. do not use like this

i.isEmpty
i2.isEmpty

i.get.toString
// i2.get.toString // 이건 익셉션 (null값을 꺼낸 다음에 toString 하므로)

var opt: Option[String] = Option[String]("a")
opt.isEmpty
opt = Option("b")
opt = None
opt.isEmpty

opt = Some("abc")
opt.isEmpty


/*
2.10 봉인된 클래스 계층

sealed abstract class Option[+A] ... { ... }
sealed 키워드는 모든 서브클래스가 같은 소스파일 안에 선언되어야 한다고 알려주는 것이다.
이 기법은 결과적으로 Option의 서브타입을 추가하지 못하도록 막는다.

그 밖에 사용자가 서브타입을 만들지 못하도록 막고 싶다면, 특정 타입을 final로 정의할 수도 있다.
 */

final class A
// class B extends A // final class 는 상속 받을 수 없다.

sealed class C
class D extends C // sealed 는 같은 페이지에서 상속이 가능하다.
