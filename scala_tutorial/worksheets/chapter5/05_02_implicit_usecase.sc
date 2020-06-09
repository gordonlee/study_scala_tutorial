import scala.collection.{GenTraversableOnce, immutable}
import scala.concurrent.{ExecutionContext, Future}

/*
  5.2 암시적 인자를 사용하는 시나리오
  : 암시적 인자를 사용하는 몇 가지 관용구

  범주로 보면
    - 준비를 위한 코드를 없애는 효과를 기대
    - 매개변수화한 타입을 받는 메서드에 사용해서 버그를 줄이거나 허용되는 타입을 제한하기 위해 사용
 */

/*
  5.2.1 실행 맥락 제공하기

 */
// import scala.concurrent.ExecutionContext.Implicits.global
// def apply[T](body: => T)(implicit executor: ExecutionContext): Future[T]
// 이 경우 우리는 ExecutionContext를 명시하지 않는 대신 컴파일러가 사용할 전역 기본 맥락을 임포트 했다.
// 실행 맥락을 넘길 때 암시적 인자를 추천한다. 예를 들어, 트랜잭션, 디비연결, 스레드 풀, 사용자 세션 등이 있다.

/*
  5.2.2 사용 가능한 기능 제어하기

  일부 메뉴는 로그인한 경우만 보여야 하고, 일부는 로긴하지 않으면 보여야 한다.
 */
case class Session() {
  def loggedin() = true
}
case class Item()
case class Menu(items: List[Item]) {
}

val helpItem: Item = Item()
val searchItem: Item= Item()
val viewAccountItem: Item= Item()
val editAccountItem: Item= Item()
val loginItem: Item= Item()

def createMenu(implicit session: Session): Menu = {
  val defaultItems = List(helpItem, searchItem)
  val accountItems =
    if (session.loggedin()) List(viewAccountItem, editAccountItem)
    else List(loginItem)
  Menu(defaultItems ++ accountItems)
}

/*
  5.2.3 사용 가능한 인스턴스 제한하기

  맨 처음 map을 시작할 때 사용했던 컬렉션과 동일한 타입의 컬렉션을 반환하고 싶다.
  map 에는 암시적인 빌더를 전달하는 관례를 택했다.

  https://scala-lang.org/files/archive/api/2.12.x/scala/collection/TraversableLike.html
  scala 2.13.x version 에서는 제거되었다.
  설명에 대한 이해만 하고 넘어가도록 하자

  +A 는 B 가 A의 서브타입이면 TraversableLike[B]가 TraversableLike[A] 의 서브타입임을 의미한다.
  CanBuildFrom 은 빌더다. 암시적 빌더 객체가 존재하는 한 원하는 새로운 컬렉션을 이를 통해 생성할 수 있음을 강조하기 위해서다.
  Repr은 원소를 저장하기 위해 내부적으로 사용하는 실제 컬렉션 타입이다.
  That은 우리가 만들고자 하는 대상 컬렉션의 타입 매개변수다.
  따라서 map 연산의 결과 컬렉션으로 허용되는 것은 현재 범위에 암시적(implicit)으로 선언된 CanBuildFrom 에 대응하는 인스턴스에 따라 결정된다.

 */

// 아래 예제를 보자.
//src/main/scala/implicits/java-database-api.scala
/*
  핵심은 getxxx 시리즈 함수이다.
  이를 get[T] 형태로 만들어 하나로 처리하면 더 좋지 않을까?

//src/main/scala/implicits/scala-database-api.scala

implicits 객체 안에서, 자바 JRow를 우리가 원하는 get[T] 메서드가 존재하는 타입으로 감싸주는 암시 클래스를 정의한다.
이런 클래스를 암시적 변환(implicit conversion)이라고 부른다.

생성한 것은 JRow인데, 어떻게 SRow 의 get[T] 함수가 호출이 되었을까?
그것은 SRow 객체에 implicit을 명시하고, 클래스 본문의 범위 안에 들어있기 때문이다.

이 결과로 암시적 객체를 사용하여 공통 슈퍼타입이 없는 경우에도 허용하는 타입을 제한할 수 있음을 확인할 수 있다.
 */


/*
  5.2.4 암시적 증거 제공하기

  이번엔 허용할 타입을 제한하되 각 타입이 공통 슈퍼타입을 따를 필요는 없는 "암시적 증거(implicit evidence)"라는 기법을 살펴보자

  우리가 아는 것처럼 Map 생성자는 키-값 쌍(즉, 튜플2)를 인자로 요구한다.
  이 상황에서 시퀀스 안에 있는 튜플이 쌍이 아니면 어떻게 막을 수 있을까?
  https://scala-lang.org/files/archive/api/2.12.x/scala/collection/TraversableOnce.html
 */

// import scala.collection.TraversableOnce
/*
trait TraversableOnce[+A] extends Any with GenTraversableOnce[A] {
  def toMap[T, U](implicit ev: A <:< (T, U)): immutable.Map[T, U] = {
    val b = immutable.Map.newBuilder[T, U]
    for (x <- self)
      b += x

    b.result()
  }
}

암시적 인자 ev는 제약조건을 강제하기 위해 필요한 '증거'다.
그 인자는 Predef 에 정의된 <:< 라는 타입을 사용한다.
타입 매개변수가 둘 있는 타입은 '중위' 표기법으로 쓸 수 있다고 했다. 따라서 아래가 성립한다.
https://www.scala-lang.org/api/current/scala/$less$colon$less.html
*/
import scala._
class A() {}
class B() extends A() {}
val ev: <:<[A, B] = null
val ev2: A <:< B = null

val l1 = List(1,2,3)
// l1.toMap
/*
Error:(118, 11) Cannot prove that Int <:< (T, U).
l1.toMap
 */

val l2 = List("one" -> 1, "two" -> 2, "three" -> 3)
l2.toMap

/*
  5.2.5 타입 소거 우회하기

  암시적 객체가 증거 역할만을 하는 또 다른 예가 바로 타입 소거(type erasure)로 인한 제약을 우회하는 기법이다.
  이전에 보았듯이 JVM은 매개변수화한 타입의 타입 인자를 '망각한다'.
 */
object M {
  def m(seq: Seq[Int]): Unit = println(s"Seq[Int]: $seq")
  // def m(seq: Seq[String]): Unit = println(s"Seq[String]: $seq")
  /*
    Error:(135, 7) double definition:
    def m(seq: Seq[Int]): Unit at line 19 and
    def m(seq: Seq[String]): Unit at line 20
    have same type after erasure: (seq: Seq)Unit
    def m(seq: Seq[String]): Unit = println(s"Seq[String]: $seq")
   */
}
// 위의 컴파일러는 바이트 코드 상에서 이 두 메서드가 실질적으로 동일하기 때문에 위와 같은 정의를 금지한다.
// 이 두 메서드의 모호성을 제거하기 위해 암시적 인자를 추가할 수 있다.
object M2 {
  implicit object IntMaker
  implicit object StringMaker
  def m(seq: Seq[Int])(implicit i : IntMaker.type): Unit = println(s"Seq[Int]: $seq")
  def m(seq: Seq[String])(implicit s: StringMaker.type): Unit = println(s"Seq[String]: $seq")
}
import M2._
m(List(1,2,3))
m(List("1", "2", "3"))
/*
  이제 컴파일러는 Int, String을 타입 소거 후에도 구분할 수 있다.
  왜 암시적으로 Int와 String에 대해 값을 사용하지 않았을까?
    암시적인 동작은 다른 모듈에 영향을 미칠 수 있기 때문이다. 사용자가 재정의 하지 않으면 기본 값에 대한 암시값이 사용될 것이고,
    사용자가 재정의하면 모호성 이슈가 생길 수 있다. (A 모듈의 암시값을 써야할지 B 모듈의 암시값을 써야할지)
 */

/*
  5.2.6 오류 메시지 개선하기

  오류 메시지에 더 잘 보이게 하기
 */

case class ListWrapper(list: List[Int])
// List(1,2,3).map[Int, ListWrapper](_*2)
/*
Error:(167, 41) Cannot construct a collection of type <<<ListWrapper>>> with elements of type Int based on a collection of type List[Int].
List(1,2,3).map[Int, ListWrapper](_*2)
https://scala-lang.org/files/archive/api/2.12.x/scala/collection/generic/CanBuildFrom.html
 */
// import scala.collection.generic
//@implicitNotFound(msg = "Cannot construct a collection of type ${To} with elements of type ${Elem} based on a collection of type ${From}.")
//trait CanBuildFrom[-From, -Elem, +To] {

/*
  5.2.7 유령 타입
  필요한 타입이 존재하는 경우 이런 암시적 인스턴스를 모두 없애 버리는 것. 타입은 있는데 인스턴스 정의는 없는 타입을 유령 타입(phantom type) 이라고 부른다
  이 기법의 주 기능은 '표식' 역할이다.

  //src/main/scala/implicits/phantom-types.scala
  Step 타입은 사용되지 않지만 표식으로 사용되었다.

  이를 파이프라인으로 구성하면 아래와같은 코드로 축약할 수 있다.
  //src/main/scala/implicits/phantom-types-pipeline.scala
 */

/*
  5.2.8 암시적 인자를 처리하기 위한 규칙

  1. 마지막 인자 목록에만 암시적 인자가 들어갈 수 있다.
  2. implicit 키워드는 인자 목록의 맨 처음에 와야 하며, 오직 한 번만 나타낼 수 있다. 인자 목록 안에서 암시적 인자 다음에
    '비암시적' 인자가 따라 올 수 없다.
  3. 인자 목록이 implicit 키워드로 시작하면, 그 인자 목록 안의 모든 인자가 암시적 인자가 된다.
 */

class Bad {
  def m(i: Int, implicit s: String) = "boo"
}

class Bad2 {
  def m(i: Int)(implicit s : String)(implicit d: Double) = "boo"
}

class Good1 {
  def m(i: Int)(implicit s: String, d: Double) = "boo"
}

class Good2 {
  def m(implicit i: Int, s: String, d: Double) = "boo"
}
