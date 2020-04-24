/*
2.12 타입과 멤버 임포트하기

  임포트 문은 어디에나 위치할 수 있다.
 */
import java.awt._ // 패키지 안의 모든 타입을 임포트 한다. _
import java.io.File // 개발 타입을 임포트 한다.
import java.io.File._ // 모든 정적 메소드와 필드를 임포트 한다.
import java.util.{Map, HashMap} // util에 Map과 HashMap만 임포트한다.

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

/*
  2.12.1 임포트는 상대적이다.
 */
import scala.collection.mutable._
import collection.mutable._ // 'scala'는 항상 임포트된 상태다.
import collection.immutable._ // 'scala'는 항상 임포트된 상태다.
import _root_.scala.collection.parallel._ // 실제 최상위 패키지로부터의 전체 경로

/*
  2.12.2 패키지 객체

  java 의
  static import com.example.json.JSON.*;
  처럼 동작하게 하고 싶을 때 이와 같은 접근을 할 수 있다. ( 정적 객체만 임포트 하고 싶을 때 )
  src/com/example/json/package.scala

 */