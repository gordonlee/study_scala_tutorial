/*
  3.1 연산자 오버로딩
  > 1+2 // 여기서 +는 메서드다.
  따라서, 1+2 는 1.+(2) 로 표현할 수 있다. 인자가 하나뿐인 메서드의 경우 중위(infix) 표기법에 따라 마침표와 괄호를 생략할 수 있다.
 */
1+2
1.+(2)


/*
  후위(postfix) 표기법
    인자가 없는 메서드는 항상 마침표 없이 호출 할 수 있다.
    but: scala 2.10 에서 이런 기능을 선택적인 기능으로 만들었다.
*/


1 toString

1.toString // 일반적인 호출

import scala.language.postfixOps // 경고가 나타나지 않는다.
// 추가로 -language:postfixOps 를 지정하면 컴파일러가 해당 기능을 항상 켜둔다.
1 toString


/*
  식별자에서 사용할 수 있는 문자는 어떤 것이 있을까? (메서드, 타입 이름, 변수 이름 등에 사용)
    - 문자: 괄호나 일부 특수문자를 제외하고 사용할 수 있다.
    - 예약어는 사용불가
    - 일반 식별자: 글자, 숫자, $, _, 연산자의 조합
    - 일반 식별자: 연산자
    - 역작은따옴표 리터럴
    - 패턴 매칭 식별자
 */

//   역작은따옴표 리터럴
def `test that addition works` = assert(1+1 == 2) // 테스트 이름을 길게 붙일 때 유용
`test that addition works`

def `print string`: Unit = println("the function is called.")
`print string`


/*
  3.1.1 편의 구문
    1.4절에서의 비동기 메시지를 느낌표(!)를 사용해서 보내는 것을 기억하라
    이 것도 메서드일 뿐이다.
 */