/*
2.13 추상 타입과 매개변수화한 타입
  대괄호를 이용하여 템플릿을 표현할 수 있다.

 */

// 매개변수화한 다형성, parametric polymorphism
val strings: List[String] = List ("one", "two", "three")

// [+A]: covariant typing. type B 가 A 의 서브타입인 경우 List[B] 도 List[A]의 서브타입이라는 뜻이다.
// [-A]: contravariant typing. A의 서브타입인 B에 대해 Foo[B]는 Foo[A]의 슈퍼타입이다.

// 추상 타입
import java.io._
/*
abstract class BulkReader {
  type In
  val source: In
  def read: String // 원본을 읽어서 String으로 변환한다.
}
 */
// 위 클래스를 아래처럼 사용한다
abstract class BulkReader[In] {
  val source: In
  def read: String // 원본을 읽어서 String으로 변환한다.
}

// class StringBulkReader(val source: String) extends BulkReader {
class StringBulkReader(val source: String) extends BulkReader[String] {
  type In = String
  def read: String = source
}

// class FileBulkReader(val source: File) extends BulkReader {
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
