# chatper2. 입력은 조금만 일은 더 많이

## 2.1 세미콜론

- 스칼라는 delimiter인 세미콜론을 '추론' 한다.

## 2.2 변수 정의

- 크게 아래와 같은 두 가지 변수 지정이 가능하다
    - val : 읽기 전용
        - 객체의 경우, 객체 자체는 변경이 불가능하지만 객체 내부의 어떤 필드 값은 변경이 가능.
    - var : 읽기 쓰기

## 2.3 범위

- 수열의 범위를 지정할 때, 대부분 for문에서 사용하는 그것.
- 예제
    - 

    ```scala
    1 to 10  // Int범위, 끝 값 포함, 증분 1
    Range (1,2,3,4,5,6,7,8,9,10)

    1 until 10  // Int범위, 끝 값 제외, 증분 1
    Range (1,2,3,4,5,6,7,8,9)

    1 to 10 by 3  // Int범위, 끝 값 포함, 증분 3
    Range (1,4,7,10)

    10 to 1 by -3  // Int범위, 끝 값 포함, 증분 3, 큰 값부터 감소
    Range (10, 7, 4, 1)

    1L to 10L by 3  // Long 범위, 끝 값 포함, 증분 3
    NumericRange[Long] (1, 4, 7, 10)

    1.1f to 10.3f by 3.1f  // Float 범위, 증분이 1이 아님
    NumericRange[Float] (1.1, 4.2, 7.299997)
    ```

## 2.4 부분 함수 (PartialFunction)

- 모든 가능한 입력에 대해 결과를 정의하지 않는 함수
- case 절만 들어갈 수 있다.
- case 절의 하나와 일치하지 않는 값이 부분 함수의 인자로 들어오면 MatchError Exception 발생.
    - PartialFunction이 어떤 입력과 일치하는지는 isDefinedAt() method를 호출해서 알 수 있다. (예외를 발생시키지 않을 수 있다.)
- 

    ```scala
    // partial function!

    val pf1: PartialFunction[Any, String] = { case s: String => "YES"}
    val pf2: PartialFunction[Any, String] = { case d: Double => "YES"}
    val pf = pf1 orElse pf2

    def tryPF(x:Any, f: PartialFunction[Any, String]): String =
      try{ f(x).toString } catch { case _: MatchError => "ERROR!"}

    def d(x:Any, f:PartialFunction[Any, String]) =
      f.isDefinedAt(x).toString

    List("str", 3.14, 10) foreach { x =>
      println("------" + "x is " + x.toString + "------")
      println("pf1 isDefinedAt:  " + d(x, pf1))
      println("pf1 try:  " + tryPF(x, pf1))
      println("pf2 isDefinedAt:  " + d(x, pf2))
      println("pf2 try:  " + tryPF(x, pf2))
      println("pf isDefinedAt:  " + d(x, pf))
      println("pf try:  " + tryPF(x, pf))
    }
    ```

## 2.5 method

- 기본 인자와 이름 붙은 인자
    - 

        ```scala
        case class Point(x: Double = 0.0, y: Double = 0.0) {
          def shift(deltax: Double=0.0, deltay: Double = 0.0) =
            copy (x + deltax, y + deltay)
        }

        val p1 = new Point(x=3.3, y=4.4)
        val p2 = p1.copy(y=6.6)
        ```

- 인자 목록이 여럿 있는 메서드
    - 

        ```scala
        abstract class Shape() {
          def draw(offset: Point = Point(0.0, 0.0))(f: String => Unit): Unit =
            f(s"draw(offset = $offset), ${this.toString}")
        }

        case class Circle(center: Point, radius: Double) extends Shape

        case class Rectangle(lowerLeft: Point, height: Double, width: Double) extends Shape

        val s: Shape = new Circle(Point(1.0,2.0), 3.0)

        s.draw(Point(5.0, 5.0))(str => println(s"ShapesDrawingActor1: $str"))

        //MEMO: 첫 번째 이점으로 괄호를 중괄호로 변경해서 사용할 수 있다. 아래 구문은 모두 같은 동작을 한다.
        s.draw(Point(5.0, 5.0)){str => println(s"ShapesDrawingActor2: $str")}

        s.draw(Point(5.0, 5.0)){str =>
          println(s"ShapesDrawingActor3: $str")
        }

        s.draw(Point(5.0, 5.0)){
          str => println(s"ShapesDrawingActor4: $str")
        }

        //MEMO: 두 번째 이점으로는 인자 목록에 대해 타입 추론이 가능하다.
        def m1[A](a: A, f: A => String) = f(a)
        def m2[A](a: A)(f: A => String) = f(a)

        // m1(100, i => s"$i + $i") // ERROR! i 값을 추론하지 못한다.
        m1[Integer](100, i => s"$i + $i")
        m2(100)(i => s"$i + $i")
        ```

- future 맛보기
    - scala.concurrent.Future
    - 동시성 도구이다 아카가 Future를 사용하지만, 액터의 모든 기능이 필요하지 않은 경우 Future만 별도로 사용할 수 있다. (자세한건 17챕터)
    - 

        ```scala
        import scala.annotation.tailrec
        import scala.concurrent.Future
        import scala.concurrent.ExecutionContext.Implicits.global

        def doWork(index: Int) = {
          Thread.sleep((math.random*1000%1000).toLong)
          index
        }

        (1 to 5) foreach { index =>
          val future = Future {
            doWork(index)
          }
          future onSuccess {
            case answer: Int => println(s"Success! return: $answer")
          }
          future onFailure {
            case th: Throwable => println(s"FAILURE! return: $th")
          }
        }

        Thread.sleep(1500)
        println("Finito!")
        ```

- 내포된 메서드 정의와 재귀
    - 재귀 함수를 작성할 때 가장 걱정되는 것이 아마도 stack overflow 일 것이다.
    - 이를 위해 스칼라 컴파일러는 꼬리재귀 최적화라는 것을 수행한다.
    - 

        ```scala
        def factorial (i: Int): Long = {
          @tailrec
          def fact(i: Int, accumulator:Long): Long = {
            if (i <= 1) accumulator
            else fact(i - 1, i * accumulator)
          }

          fact(i, 1)
        }

        (0 to 5) foreach( i => println(factorial(i)))
        ```

    - @tailrec 어노테이션은 컴파일러가 꼬리재귀를 수행할 수 있는지 미리 검토하게 해준다.
        - 꼬리 재귀 최적화는 A 함수에서 다시 A 함수 1개로 넘어가는 경우 해당된다.
    - 아래의 경우는 꼬리 재귀 최적화가 불가능하여 complie time에 에러를 확인하게 된다.
    - 

        ```scala
        @tailrec // 재귀가 두 함수로 깊어지기 때문에 최적화가 불가능하다.
        def fibonacci(i:Int): Long = {
          if (i <= 1) 1L
          else fibonacci(i-2) + fibonacci(i-1)
        }
        ```

## 2.6 타입 추론

- 

    ```scala
    import scala.collection.immutable.HashMap

    val intToStringMap: HashMap[Integer, String] = new HashMap
    val intToStringMap2 = new HashMap[Integer, String]
    ```

- 일부 함수형 프로그래밍 언어는 전역 타입 추론을 사용해서 거의 모든 타입을 추론할 수 있다. 
그러나, scala는 서브타입 다형성(subtype polymorphism) 즉 상속을 지원해야 해서 그렇게 할 수 없다.
    - 

        ```
        언제 명시적으로 타입을 표기해야 하는가?
        - var, val 선언에서 값을 대입하지 않는 경우
        - 모든 메서드 매개변수
        - 다음과 같은 메서드 반환 타입의 경우
          - 메서드 안에서 return을 명시적으로 호출하는 경우
          - 매서드가 재귀적인 경우
          - overloading한 둘 이상의 메서드가 있고, 그중 한 메서드가 다른 메서드를 호출하는 경우
          - 컴파일러가 추론한 타입이 코더의 의도보다 더 일반적인 경우 (ex> Any)
        ```

    - case: overloading method가 다른 동일 이름의 메서드를 호출하는 경우
        - strings :_* 는 컴파일러에게 주는 힌트로 strings 라는 리스트를 가변 인자 목록(*)으로 다루되, 타입은 알 수 없지만 추론한 타입(:_)을 사용하라는 뜻이다.
        - 실제 상황에서는 따로 joiner를 만들지 말고 가변 인자 목록을 사용처에서 활용하는 것이 나을 수 있다.

            ```scala
            object StringUtilV1 {
              def joiner(strings: String*): String = strings.mkString("-")
              // def joiner(strings: List[String]) = joiner(strings: _*) // 컴파일 오류
              def joiner(strings: List[String]):String = joiner(strings: _*) // 컴파일 오류
            }
            println( StringUtilV1.joiner(List("P", "S")))
            ```

    - case: Any가 추론되는 케이스
        - 중요한 것은 이 내용이 컴파일이 된다는 것이다.
        사용하는 쪽과 별도로 빌드되는 API를 만드는 경우, 메서드 타입을 할 수 있는 한 가장 일반적인 타입으로 명시하라.

            ```scala
            def makeList(strings: String*) = {
              if (strings.length == 0)
                List(0) // 0이라는 원소가 하나 들어있는 List[Int] 를 반환한다.
              else
                strings.toList
            }

            // val list: List[String] = makeList()
            val list = makeList()

            ```

    - case: 실수하기 쉬운 타입 (프로시져)
        - 

            ```scala
            def double(i: Int) { 2 * i }
            println(double(2))

            def double2(i: Int) = { 2 * i}
            println(double2(2))
            ```

        - 등호가 없으면 return type은 Unit 으로 잡히게 된다.
        또한, 이러한 프로시져 문법은 scala 2.11 부터 deprecated 되었다.

## 2.7 예약어

- 한번 훑어 보시고, 필요하면 구글링...

## 2.8 리터럴 값

- 다른 경우는 그냥 읽고 이해하면 됩니다.
- 함수 리터럴
    - 아래 코드는 동일한 의미이다.

        ```scala
        val f1: (Int, String) => String         = (i, s) => i + s
        val f2: Function2[Int, String, String]  = (i, s) => i + s
        ```

- 튜플 리터럴
    - 한 번에 하나의 return 값만 주는 것이 아니라 이를 튜플로 묶어서 여러 값을 한 번에 넘길 수 있다.
    - 

        ```scala
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
        ```

    - 또한, 원소가 2개인 튜플은 화살표로도 정의할 수 있다.
    - 

        ```scala
        (1, "one")
        1 -> "one"
        Tuple2(1, "one")
        ```

## 2.9 Option, Some, None

- 각 객체의 정의
    - Option : 추상 클래스로 값이 있는 Some과 값이 없는 None 상태가 있다.
    - Some: 값이 있는 것을 의미하는 객체
    - None: 값이 없는 것을 의미하는 객체
- null 이 얼마나 위험한지는 책의 설명을 참조
    - [https://alvinalexander.com/scala/using-scala-option-some-none-idiom-function-java-null/](https://alvinalexander.com/scala/using-scala-option-some-none-idiom-function-java-null/)

        ```scala
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
        ```

- 책에 없는 개인 예제
    - 

        ```scala
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
        ```

## 2.10 봉인된 클래스 계층

- sealed: 모든 서브클래스가 같은 소스파일 안에 선언되어야 한다고 알림
    - 사용자의 입장에서는 결과적으로 서브타입을 추가하지 못한다. (같은 코드를 공유하는 작업자들은 가능)
- final: 서브타입이 아예 없다는 키워드
- 

    ```scala
    final class A
    // class B extends A // final class 는 상속 받을 수 없다.

    sealed class C
    class D extends C // sealed 는 같은 페이지에서 상속이 가능하다.
    ```

## 2.11 파일과 이름공간으로 코드 구조화하기

- 스칼라는 자바와 다르게 파일 이름 = 타입 이름일 필요가 없고, 패키지 구조 = 디렉터리 구조 일 필요도 없다.
    - 

        ```scala
        package com {
          package example {
            package pkg1 {
              class Class11 {
                def m = "m11"
              }
              class Class12 {
                def m = "m12"
              }
            }
            package pkg2 {
              class Class21 {
                def m = "m21"
                def makeClass11 = {
                  new pkg1.Class11
                }
                def makeClass12 = {
                  new pkg1.Class12
                }
              }
            }

            package pkg3.pkg31.pkg311 {
              class Class311 {
                def m = "m21"
              }
            }
          }
        }
        ```

- 연속 패키지 문 관용구. 해당 패키지를 가져와서 추가/코딩할 수도 있다.
    - 

        ```scala
        // 'example'에 있는 모든 패키지 수준의 선언을 영역으로 가져온다.
        package com.example

        // 'mypkg' 에 있는 모든 패키지 수준의 선언을 영역으로 가져온다.
        package mypkg

        class MyPkgClass {
        }
        ```

## 2.12 타입과 멤버 임포트하기

- 기본적인 사용 이해하기

    ```scala
    import java.awt._ // 패키지 안의 모든 타입을 임포트 한다. _
    import java.io.File // 개발 타입을 임포트 한다.
    import java.io.File._ // 모든 정적 메소드와 필드를 임포트 한다.
    import java.util.{Map, HashMap} // util에 Map과 HashMap만 임포트한다.
    ```

- import 문은 어디에나 위치할 수 있다.

    ```scala

    def stuffWithBigInteger() = {
      import java.math.BigInteger.{
      ONE => _,  // ONE은 임포트에서 제외한다.
      TEN, // 그대로 임포트한다.
      ZERO => JAVAZERO // ZERO를 JAVAZERO라는 이름(alias)으로 임포트한다.
      }

      println("ONE: " + ONE)
      println("TEN: " + TEN)
      println("JAVAZERO: " + JAVAZERO)

    }
    ```

- 2.12.1 임포트는 상대적이다

    ```scala
    import scala.collection.mutable._
    import collection.mutable._ // 'scala'는 항상 임포트된 상태다.
    import collection.immutable._ // 'scala'는 항상 임포트된 상태다.
    import _root_.scala.collection.parallel._ // 실제 최상위 패키지로부터의 전체 경로
    ```

- 2.12.2 패키지 객체

    ```scala
    // src/com/example/json/package.scala
    package com.example

    package object json {
      class JSONObject {}  // 노출할 멤버를 적절히 정의한다.
      def fromString(strings: String): JSONObject = { null }
    }
    ```

    - java의 static import com.example.json.JSON.* 처럼 동작하게 하고 싶을 때 위와 같은 접근을 할 수 있다.

## 2.13 추상 타입과 매개변수화한 타입

- 대괄호를 이용하여 템플릿을 표현할 수 있다.
- 매개변수화한 다형성

    ```scala
    // 매개변수화한 다형성, parametric polymorphism
    val strings: List[String] = List ("one", "two", "three")
    ```

    - [+A]: covariant typing. type B 가 A 의 서브타입인 경우 List[B] 도 List[A]의 서브타입이라는 뜻이다.
    - [-A]: contravariant typing. A의 서브타입인 B에 대해 Foo[B]는 Foo[A]의 슈퍼타입이다.
- 추상 타입

    ```scala
    // 추상 타입
    import java.io._

    abstract class BulkReader {
      type In
      val source: In
      def read: String // 원본을 읽어서 String으로 변환한다.
    }

    class StringBulkReader(val source: String) extends BulkReader {
      type In = String
      def read: String = source
    }

    class FileBulkReader(val source: File) extends BulkReader {
      type In = File
      def read: String = {
        val in = new BufferedInputStream(new FileInputStream(source))
        val numBytes = in.available()
        val bytes = new Array[Byte](numBytes)
        in.read(bytes, 0, numBytes)
        new String(bytes)
      }
    }

    println(new StringBulkReader("Hello Scala!").read)

    println(new FileBulkReader(
      new File("/Users/leegordon/Documents/github/study/scala_tutorial/worksheets/02_13_template_and_covariant.sc")
    ).read)
    ```

    - 위 처럼 type 키워드로 지정하여 타입을 입력받을 수 있다.
    - 이 코드를 아래처럼 변경할 수 있다.

        ```scala
        // 추상 타입
        import java.io._

        abstract class BulkReader[In] {
          val source: In
          def read: String // 원본을 읽어서 String으로 변환한다.
        }

        class StringBulkReader(val source: String) extends BulkReader[String] {
          type In = String
          def read: String = source
        }

        class FileBulkReader(val source: File) extends BulkReader[File] {
          type In = File
          def read: String = {
            val in = new BufferedInputStream(new FileInputStream(source))
            val numBytes = in.available()
            val bytes = new Array[Byte](numBytes)
            in.read(bytes, 0, numBytes)
            new String(bytes)
          }
        }

        println(new StringBulkReader("Hello Scala!").read)

        println(new FileBulkReader(
          new File("/Users/leegordon/Documents/github/study/scala_tutorial/worksheets/02_13_template_and_covariant.sc")
        ).read)
        ```