/*
3.2 빈 인자 목록이 있는 메서드
scala는 인자가 없는 함수에 대해 괄호를 생략할 수 있도록 유연성을 제공한다.
 */

// 다른 언어에서는 List.size 와 List.size() 둘 중 하나를 선택해서 사용해야 한다.
//  (왜냐하면, 전자는 변수(혹은 프로퍼티)이고, 후자는 함수이기 때문에)
// scala 에서는 두 방법 모두 사용이 가능하다

"hello".length()
"hello".length
// 이와 같은 유연성에 대한 '관례'는 side effect가 없고 인자가 빈 인자라면 괄호를 생략하는 방법을 택한다.
// 괄호가 있는 함수는 주의를 기울이라는 의미이다.
// -Xlint 옵션을 컴파일러에 부여하면, side effect가 발생하는 메서드에 괄호를 생략하면 워닝을 표시한다.
def isEven(n: Int) = (n%2) == 0

List(1,2,3,4) filter isEven foreach println

// 위 함수가 의미하는 바를 풀어서 쓴다면 아래와 같이 쓸 수 있다.
List(1,2,3,4)
  .filter((i:Int) => isEven(i))
  .foreach((i:Int) => println(i))

List(1,2,3,4)
  .filter(i => isEven(i))
  .foreach(i => println(i))

List(1,2,3,4)
  .filter(isEven)
  .foreach(println)

