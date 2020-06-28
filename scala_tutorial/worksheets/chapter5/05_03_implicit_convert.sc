/*
  5.3 암시적 변환

 */

// Map 을 초기화 할 수 있는 튜플은 아래와 같은 형태들을 지원한다.
(1, "one")
1 -> "one"
Tuple2(1, "one")
// Pair(1, "one")

// Map의 apply 함수의 선언을 보면 명확히 튜플을 기준으로 명시하고 있다.

{
  //https://www.scala-lang.org/api/current/scala/collection/Map$.html#apply[K,V](elems:(K,V)*):C[K,V]
  // def apply[K, V](elems: (K, V)*): Map[K, V]
}

{
  // 간단히 -> 가 정의된 wrapper 객체를 사용하는 방법이 있다.
  // 실제로 Predef 에는 이미 아래와 같은 객체가 있다.
  implicit final class ArrowAssoc[A](val self: A) {
    def ->[B](y:B): Tuple2[A, B] = Tuple2(self, y)
  }

  // 그래서 실제로 사용하는 사람은 -> 라고 생각하지만 실제 코드는 아래처럼 동작한다.
  Map(new ArrowAssoc("one") -> 1, new ArrowAssoc("two") -> 2)
}

/*
  implicitConversions이 수행되는 검색 규칙

  1. 객체와 메서드의 조합이 성공적으로 타입 검사를 통과하는 경우 타입 변환을 시도하지 않는다.
  2. implicit 키워드가 앞에 붙은 클래스와 메서드만 고려한다.
  3. 현재 범위 안에 있는 암시적 클래스와 메서드만 고려한다. 또한 대상 타입의 동반 객체 안에 정의된
    암시적 메서드도 고려시 포함시킨다.
  4. 암시 메서드를 둘 이상 조합해서, 한 타입을 다른 중간 타입으로 변환한 다음에 최종 타입으로
    변환하는 식의 변환은 시도하지 않는다. 오직 타입 인스턴스를 하나 받아서 목표로 하는
    타입의 인스턴스를 반환하는 메서드만 고려한다.
  5. 적용 가능한 암시적 변환이 둘 이상 있는 경우에는 변환을 수행할 수 없다. 유일하고 모호하지 않을
    가능성이 있어야 변환을 수행한다.
 */

// 동반 객체가 있는 경우의 검색

import scala.language.implicitConversions

case class Foo(s: String)
object Foo {
  implicit def fromString(s: String): Foo = Foo("Foo.Implicit: " + s)  // 암시적으로 동반 객체는 implicit이 동작한다.
}

// implicit def overridingConversion(s: String): Foo = Foo("Boo " + s)
// 위 경우처럼 기존에 있던 implicit을 가려질 수 있다.

class O {
  def m1(foo:Foo): Unit = println(foo)
  def m(s: String): Unit = m1(s)
  /*
    Error:(50, 57) Foo.type does not take parameters
    implicit def fromString(s: String): Foo = Foo(s)
   */
}
// O.m
val o = new O
o.m("abc")

// 이와 더불어, String의 reverse, capitalize 등의 함수도 String 객체 안에 있지 않다.
// 또, Range 타입의 경우 1 to 100 by 3 에서 to, by, until 등의 단어가 실제로는 언어가 제공하는 키워드가 아니고
// 래퍼 타입의 메서드라는 것을 이해할 수 있다.

/*
  5.3.1 자신만의 문자열 인터폴레이션 만들기
 */

// 컴파일러는 x"foo bar" 와 같은 식을 보면 scala.StringContext 에 x라는 메서드를 찾는다.
// 이는 아래처럼 변환된다.
val name2 = ("Buck", "Trends")
s"Hello, ${name2._1} ${name2._2}"
StringContext("Hello, ", " ", "").s(name2._1, name2._2)

// StringContext 에는 f, raw 라는 메서드도 있다.

// 우리만의 인터폴레이션을 만들려면 '확장'해서 새로운 메서드를 추가하면 된다.
// 예제로 간단히 JSON 문자열을 json.JSONObject 로 변환하는 것을 만들어 보자.

// src/main/scala/progscala2/implicits/custom-string-interpolator.sc

import scala.util.parsing.json._

object Interpolators {
  implicit class jsonForStringContext(val sc: StringContext) {       // <1>
    def json(values: Any*): JSONObject = {                           // <2>
      val keyRE = """^[\s{,]*(\S+):\s*""".r                          // <3>
      val keys = sc.parts map {                                      // <4>
        case keyRE(key) => key
        case str => str
      }
      val kvs = keys zip values                                      // <5>
      JSONObject(kvs.toMap)                                          // <6>
    }
  }
}

import Interpolators._

val name = "Dean Wampler"
val book = "Programming Scala, Second Edition"

val jsonobj = json"{name: $name, book: $book}"                       // <7>
println(jsonobj)

/*
  5.3.2 표현력 문제

  어떤 모듈의 소스코드를 수정하지 않고 확장하고자 하는 욕구를 일컬어 "표현력 문제(expression problem)"이라 한다.
  객체 지향은 서브타입 다형성(subtype polymorphism)으로 이 문제를 해결한다.
  이 경우, 추가로 들어있는 쓰지 않는 코드가 쌓이고, 대부분의 동작 정의가 여러 번 반복될 수 밖에 없다.
  이는 단일 책임 원칙(Single Responsibility Principle) 이라는 고전적인 설계 원칙으로 이어진다.

  스칼라는 이를 혼합 기능으로 그때그때 조합하는 객체를 선언할 수 있으며,
  동적으로 타입을 결정하는 메타프로그래밍 기능을 제공한다. 이를 실행 시점에 동작시키면 어떤 타입을 변경하는 일이
  굉장히 위험해진다.
  스칼라의 암시적 변환은 타입 클래스를 타입을 정적으로 지정하면서 구현할 수 있게 해준다.
 */
