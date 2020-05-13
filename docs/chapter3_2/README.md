# chapter3. 기초를 튼튼히(2)

## 3.8 조건 연산자

- 쇼트서킷(short-circuiting) 연산자: 순차적으로 판단하되, 조건이 항상 만족하면 그 이후는 평가하지 않고 끝낸다.
    - 예시) &&, ||

## 3.9 try, catch, finally

- 스칼라는 예외나 예외 처리의 필요성을 줄이는 코딩 유형을 장려한다.
- 자바와 달리 스칼라에는 검증 예외(checked exception)가 없다.
- 또한, 스칼라 메서드 정의에는 throws 절이 없다. 대신 @throws 어노테이션이 있다.
- 
    ```scala
    object TryCatch {
      def main(args: Array[String]) = {
        args foreach (arg => countLine(arg))
      }

      import scala.io.Source
      import scala.util.control.NonFatal

      def countLine(filename: String): Unit = {
        println()
        var source: Option[Source] = None
        try {
          source = Some(Source.fromFile(filename))
          val size = source.get.getLines.size
          println(s"file $filename has $size lines")
        } catch {
          case NonFatal(ex) => println(s"Non fatal exception! $ex")
        } finally {
          for (s <- source) {
            println(s"Closing $filename")
            s.close
          }
        }
      }
    }
    ```

    ## 3.10 이름에 의한 호출과 값에 의한 호출

    - 샘플코드
    - 
    ```scala
    import scala.language.reflectiveCalls
    import scala.util.control.NonFatal

    object manage {
      def apply[R <: { def close():Unit }, T](resource: => R)(f: R => T): T = {
        var res: Option[R] = None
        try {
          res = Some(resource)         // Only reference "resource" once!!
          f(res.get)                   // Return the T instance
        } catch {
          case NonFatal(ex) =>
            println(s"manage.apply(): Non fatal exception! $ex")
            throw ex
        } finally {
          if (res != None) {
            println(s"Closing resource...")
            res.get.close()
          }
        }
      }
    }

    object TryCatchARM {
      /** Usage: scala rounding.TryCatch filename1 filename2 ... */
      def main(args: Array[String]): Unit = {
        val sizes = args map { arg =>
          try {
            val size = returnFileLength(arg)
            println(s"file $arg has $size lines")
            size
          }
          catch {
            case NonFatal(ex) =>
              println(s"caught $ex")
              -1
          }
        }
        println("Returned sizes: " + (sizes.mkString(", ")))
      }

      import scala.io.Source

      def returnFileLength(fileName: String): Int = {
        println()  // Add a blank line for legibility
        manage(Source.fromFile(fileName)) { source =>
          source.getLines.size
        }
      }
    }
    ```

- apply 함수 파악
    - R 과 T 는 타입이다. R은 관리할 자원의 타입이다.
    <: 가 생소할텐데 R이 다른 어떤 것의 서브 클래스임을 의미한다.
    여기서는 close():Unit 메서드가 들어있는 구조적 타입임을 의미한다. (일종의 Closable)
    - T는 자원을 가지고 작업을 수행하도록 넘겨지는 익명 함수에서 반환하는 타입이다.
    - resource 는 '이름에 의한' 매개변수이다. 당장은 괄호 없이 호출할 수 있는 함수로 생각하자.
    - R을 인자로 받고 T라는 타입의 결과를 반환하는 함수다.
- 전체 파악
    - Source를 만드는 첫 식은 실행이 manage로 넘어가기 '전에' 즉시 평가되지 않는다.
    계산은 manage에 있는 val res = Some(resource)에 이를 때까지 지연된다.
    - 다른 대부분의 언어처럼 일반적으로 '값에 의한 호출(call by value)를 사용한다.
    따라서 manage(Source.fromFile(filename))을 값에 의한 호출 문맥으로 쓰면
    Source.fromFile을 먼저 호출해서 반환된 결과를 manage에 넘긴다.
    - 이름에 의한 매개변수가 함수처럼 동작한다는 것을 기억하자.
    즉, 이름에 의한 매개변수는 매번 사용할 때마다 재평가 된다.
- 아래는 이름에 의한 호출에 대한 다른예제이다.
- 
    ```scala
    // 아래는 이름에 의한 호출에 대한 다른 예제이다.

    @annotation.tailrec
    def continue(conditional: => Boolean)(body: => Unit): Unit = {
      if (conditional) {
        body
        continue(conditional)(body)
      }
    }

    var count = 0
    continue(count < 5) {
      println(s"at $count")
      count += 1
    }
    ```

- 매번 condition, body 평가가 매번 일어남

## 3.11 lazy init val

- 특정 값을 초기화하기 위해 식을 계산하는 과정을 여러 번 반복하지 않고 단 한 번만 수행해야 하는 경우는 어떻게 할까?
    - 식의 계산이 비싸므로, 값이 실제 필요할 때까지 그 식을 계산하는 비용을 지불하고 싶지 않음
    - 즉시 필요하지 않은 작업의 실행을 미뤄서 시작하는 시간을 향상시킴
    - 다른 초기화가 먼저 일어날 수 있도록 지연시켜서 초기화
- lazy keyword 는 값이 실제 필요할 때까지 계산을 유예해야 함을 표시한다.
또한, 일반 함수와 다르게 해당 값을 처음 갖는 순간 오직 한 번만 평가된다.
- 지연 값은 가드로 구현한다. 가드를 사용해서 지연값이 초기화되었는지 여부를 저장해둔 volatile 필드를 검사하고 꼭 필요할 때만 지연값을 초기화한다. 물론 스레드 세이프하게 구현한다.
이중 검사 락에서는 지연값이 들어있는 객체를 모니터로 사용한다. 지연값이 들어있는 객체에 대해 synchronized 를 실행하면 데드락에 빠질 수 있다.
- 
```scala
object ExpensiveResource {
  lazy val resource: Int = init()
  def init(): Int = {
    // do long job
    0
  }
}
```

## 3.12 열거값

- 
```scala
object Breed extends Enumeration {
  type Breed = Value
  val doberman = Value("Doberman")
  val yorkie = Value("Yorkie")
  val scottie = Value("Scottie")
  val dane = Value("Dane")
  val portie = Value("Portie")
}
import Breed._  // 사용을 위해 임포트

println("ID/tBreed")
for (breed <- Breed.values) println(s"${breed.id}\t${breed}")
// 정해진 value와 숫자형 id 값이 함께 제공된다. (구조형)
```

- 아래처럼 인자를 Value로 지정하지 않고 지정할 수 있는데, 이 경우 스칼라는 리플렉션을 사용한다.
- 

```scala
object WeekDay extends Enumeration {
  type WeekDay = Value
  val Mon, Tue, Wed, Thu, Fri, Sat, Sun = Value
}
import WeekDay._

def isWorkingDay(d: WeekDay) = ! (d == Sat || d == Sun)
WeekDay.values filter isWorkingDay foreach println
```

## 3.13. 스트링 인터폴레이션

```scala
/*
3.13 문자열 언터폴레이션
 */

val name = "gordon lee"
println(s"Hello, $name")

// 두 가지 유형이 있다.
  // printf 와 같은 형식화를 지원하며, f를 문자열 앞에 붙인다.
  // raw 문자열 인터폴레이션이라 부르며, \n 과 같은 이스케이프 문자를 변환하지 않는다.
val gross = 10000F
val net = 64000F
val percent = (net / gross) * 100
println(f"$$${gross}%.2f vs. $$${net}%.2f or ${percent}%.1f%%")
println(s"$$${gross}%.2f vs. $$${net}%.2f or ${percent}%.1f%%")
// $ 표현을 위해서 $$ 를 사용한다. % -> %%
// 스칼라는 printf 형식화를 위해 자바의 Formatter를 사용한다.
// 사용한 변수의 타입과 형식 지정이 일치해야 한다. 일부 암시 변환이 적용되기도 한다.
// Int -> Float (OK) , Double -> Int (NG)

// 때로 스칼라 컴파일러가 자바 문자열을 StringLike 로 감싸서 새로운 메서드를 추가하는 경우도 있다.
val s = "%02d: name = %s".format(5, "Gordon Lee")
println(s)

// 아래는 내장 문자열 인터폴레이션은 제어 문자를 확장하지 않은 raw 형식이다.
s"123\n$name\n456"
raw"123\n$name\n456"
```

- 스칼라는 printf 형식화를 위해 자바의 Formatter를 사용한다.

## 3.14 트레이트와 인터페이스

- 자바에는 interface가 있다. 그 안에 메서드를 선언할 수 있지만 정의 할 수는 없다.
- 또한 static 변수나 내포된 타입을 정의할 수 있다.
- 스칼라는 interface 를 trait 으로 바꿨다.
메서드를 선언하면서 원하면 정의까지 할 수 있는 인터페이스 정도로 생각하면 되겠다.

```scala
val service1 = new ServiceImportante("uno")
(1 to 3) foreach (i => println(s"Result: ${service1.work(i)}"))

// 여기에 표준 로깅 라이브러리를 혼합하자
trait Logging {
  def info (msg : String): Unit
  def warning (msg : String): Unit
  def error (msg : String): Unit
}
trait StdoutLogging extends Logging {
  def info (msg : String): Unit = println(s"Info: $msg")
  def warning (msg : String): Unit = println(s"Warning: $msg")
  def error (msg : String): Unit = println(s"Error: $msg")
}

// 이를 혼합하는 서비스를 선언해보자
val service2 = new ServiceImportante("dos") with StdoutLogging {
  override def work(i: Int): Int = {
    info(s"Starting work: i = $i")
    val result = super.work(i)
    info(s"Ending work: i = $i, result = $result")
    result
  }
}
(1 to 3) foreach (i => println(s"Result: ${service2.work(i)}"))

// 이 예제에서 우리는 실제로 로그 기능을 주입하기 위해 work의 동작을 변경 했다.
// 하지만 고객과의 '계약', 즉 외부 동작은 변경하지 않았다.

// 만약 ServiceImportante with StdoutLogging의 인스턴스가 많이 필요하다면, 클래스를 선언할 수도 있다.
class LoggedServiceImportante(name: String)
  extends ServiceImportante(name) with StdoutLogging {
  // ...
}
```

- 혼합하는 것 넘나 신박....
- 부모 클래스에서 정의한 메서드를 오버라이트할 때 override 키워드를 명시해야 한다.
- 또, 다른 언어처럼 super.work을 사용해서 부모 클래스에 있는 work 을 사용했다는 점을 기억하자.