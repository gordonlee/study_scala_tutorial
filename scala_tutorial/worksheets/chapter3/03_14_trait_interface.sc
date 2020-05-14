/*
3.14 트레이트: 스칼라 인터페이스와 혼합
자바에는 interface가 있다. 그 안에 메서드를 선언할 수 있지만 정의 할 수는 없다.
또한 static 변수나 내포된 타입을 정의할 수 있다.

스칼라는 interface 를 trait 으로 바꿨다.
메서드를 선언하면서 원하면 정의까지 할 수 있는 인터페이스 정도로 생각하면 되겠다.
 */
class ServiceImportante(name: String) {
  def work(i: Int): Int = {
    println(s"ServiceImportante: Doing important work! $i")
    i + 1
  }
}

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
// 부모 클래스에서 정의한 구체적 메서드를 오버라이드 할 때 override 키워드를 명시해야 한다.
// 다른 언어처럼 super.work을 사용해서 부모 클래스에 있는 work 을 사용했다는 점을 기억하자.
