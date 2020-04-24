// method

  // 2.5.1 기본 인자와 이름 붙은 인자
case class Point(x: Double = 0.0, y: Double = 0.0) {
  def shift(deltax: Double=0.0, deltay: Double = 0.0) =
    copy (x + deltax, y + deltay)
}

val p1 = new Point(x=3.3, y=4.4)
val p2 = p1.copy(y=6.6)


  // 2.5.2 인자 목록이 여럿 있는 메서드
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

  // 2.5.3 future 맛보기
/*
  scala.concurrent.Future. 동시성 도구이다 아카가 Future를 사용하지만,
  액터의 모든 기능이 필요하지 않은 경우 Future만 별도로 사용할 수 있다.
  자세한 내용은 17 챕터에서 다룬다.
*/
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

  // 2.5.4 내포된 메서드 정의와 재귀

def factorial (i: Int): Long = {
  @tailrec
  def fact(i: Int, accumulator:Long): Long = {
    if (i <= 1) accumulator
    else fact(i - 1, i * accumulator)
  }

  fact(i, 1)
}

(0 to 5) foreach( i => println(factorial(i)))
/*
  factorial의 i 와 fact의 i 는 다르다.
  fact의 리턴 타입은 추론이 불가능하다. (재귀)
  꼬리 재귀 최적화(tail-call optimization)를 수행한다.
    @tailrec은 컴파일러가 꼬리재귀 최적화를 하는지 보장할 수 있다.
 */

// @tailrec // 재귀가 두 함수로 깊어지기 때문에 최적화가 불가능하다.
def fibonacci(i:Int): Long = {
  if (i <= 1) 1L
  else fibonacci(i-2) + fibonacci(i-1)
}