/*
3.10 이름에 의한 호출과 값에 의한 호출

먼저 객체를 Manage가 아닌 manage로 지정하여 함수를 이용하는 것처럼 지정했다.

apply 함수를 먼저 파악해보자
 */
/*
def apply[
  R <: { def close():Unit }
  , T]
(resource: => R)
(f: R => T): T = {
}
*/
/*
R 과 T 는 타입이다. R은 관리할 자원의 타입이다.
<: 가 생소할텐데 R이 다른 어떤 것의 서브 클래스임을 의미한다.
여기서는 close():Unit 메서드가 들어있는 구조적 타입임을 의미한다. (일종의 Closable)

T는 자원을 가지고 작업을 수행하도록 넘겨지는 익명 함수에서 반환하는 타입이다.

resource 는 '이름에 의한' 매개변수이다. 당장은 괄호 없이 호출할 수 있는 함수로 생각하자.

R을 인자로 받고 T라는 타입의 결과를 반환하는 함수다.
 */

/*
  Source를 만드는 첫 식은 실행이 manage로 넘어가기 '전에' 즉시 평가되지 않는다.
  계산은 manage에 있는 val res = Some(resource)에 이를 때까지 지연된다.

  다른 대부분의 언어처럼 일반적으로 '값에 의한 호출(call by value)를 사용한다.
  따라서 manage(Source.fromFile(filename))을 값에 의한 호출 문맥으로 쓰면
  Source.fromFile을 먼저 호출해서 반환된 결과를 manage에 넘긴다.

  이름에 의한 매개변수가 함수처럼 동작한다는 것을 기억하자.
  즉, 이름에 의한 매개변수는 매번 사용할 때마다 재평가 된다.
 */

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
