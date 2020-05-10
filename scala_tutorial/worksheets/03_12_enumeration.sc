/*
3.12 열거값
여러 언어에서는 기본 구성 요소이지만, 스칼라는 표준 라이브러리의 Enumeration에 구현했다.
 */

object Breed extends Enumeration {
  type Breed = Value
  val doberman = Value("Doberman")
  val yorkie = Value("Yorkie")
  val scottie = Value("Scottie")
  val dane = Value("Dane")
  val portie = Value("Portie")
}
import Breed._

println("ID/tBreed")
for (breed <- Breed.values) println(s"${breed.id}\t${breed}")


// 인자를 주지 않고도 만들 수 있는데, 이 경우 리플렉션을 사용한다.
// 우리가 WeekDay를 임포트 했다는 것에 유의하자.

object WeekDay extends Enumeration {
  type WeekDay = Value
  val Mon, Tue, Wed, Thu, Fri, Sat, Sun = Value
}
import WeekDay._

def isWorkingDay(d: WeekDay) = ! (d == Sat || d == Sun)
WeekDay.values filter isWorkingDay foreach println
