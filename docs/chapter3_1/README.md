# chapter3. 기초를 튼튼히(1)

## 3.1 연산자 오버로딩

- +는 메서드이다.
- 중위(infix) 표기법: 인자가 하나뿐인 메서드의 경우 중위(infix) 표기법에 따라 마침표와 괄호를 생략할 수 있다.

    ```scala
    1+2

    1.+(2)
    ```

- 후위(postfix) 표기법: 인자가 없는 메서드는 항상 마침표 없이 호출 할 수 있다. 그러나, scala 2.10 에서 이런 기능을 선택적인 기능으로 만들었다.

    ```scala
    1 toString  // 여기서는 경고가 출력 된다. 

    1.toString  // 빈 파라메터를 받는 함수 호출

    // 이렇게 미리 import 하거나 컴파일러 옵션으로 feature에 대한 자세한 기록을 출력하도록 한다.
    import scala.language.postfixOps // 경고가 나타나지 않는다.
    // 추가로 -language:postfixOps 를 지정하면 컴파일러가 해당 기능을 항상 켜둔다.
    1 toString  // 경고가 출력되지 않음.
    ```

- 식별자에서 사용할 수 있는 문자는 어떤 것이 있을까? (메서드, 타입 이름, 변수 이름 등에 사용)
    - 문자: 괄호나 일부 특수문자를 제외하고 사용할 수 있다.
    - 예약어는 사용불가
    - 일반 식별자: 글자, 숫자, $, _, 연산자의 조합
    - 일반 식별자: 연산자
    - 역작은따옴표 리터럴
    - 패턴 매칭 식별자

→ 역작은따옴표 리터럴의 예시

```scala
def `test that addition works` = assert(1+1 == 2) // 테스트 이름을 길게 붙일 때 유용
`test that addition works`  // 함수를 실행

def `print string`: Unit = println("the function is called.")
`print string`  // 함수를 실행
```

- 편의 구문
    - 아카 라이브러리에서 비동기 메시지를 보낼 때 사용하는 느낌표(!)를 생각해보자. 
    이 느낌표(!) 도 method 일 뿐이다.

## 3.2 빈 인자 목록이 있는 메서드

- scala는 인자가 없는 함수에 대해 괄호를 생략할 수 있도록 유연성을 제공한다.
- 다른 언어에서는 `List.size` 와 `List.size()` 둘 중 하나를 선택해서 사용해야 한다.
(왜냐하면, 전자는 변수(혹은 프로퍼티)이고, 후자는 함수이기 때문에)
scala 에서는 두 방법 모두 사용이 가능하다
- 
    ```scala
    "hello".length()
    "hello".length
    ```

- 이와 같은 유연성에 대한 '관례'는 `side effect가 없고 인자가 빈 인자라면 괄호를 생략하는 방법`을 택한다.
    - 괄호가 있는 함수는 주의를 기울이라는 의미이다. -Xlint 옵션을 컴파일러에 부여하면, side effect가 발생하는 메서드에 괄호를 생략하면 워닝을 표시한다.
- 
    ```scala
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
    ```

## 3.3 우선순위 규칙

1. 모든 글자
2. |
3. ^
4. &
5. < >
6. = !
7. :
8. 
9. / %
10. 다른 모든 특수 문자

→ 단, 대입에 사용하는 =은 가장 낮은 우선순위를 갖는다. 

- 연속적으로 호출할 때의 우선순위
    - 기본적으로 왼쪽 → 오른쪽으로 묶인다
    - 
        ```scala
        2.0 * 4.0 / 3.0 * 5.0  // 이 코드는
        (((2.0 * 4.0) / 3.0) * 5.0)  // 이렇게 풀린다.
        ```

- 단, : 으로 끝나는 메서드는 항상 오른쪽 → 왼쪽으로 묶인다.
- 
    ```scala
    val list = List('b', 'c', 'd')
    'a' :: list

    list.::('a') // 위 식은 이것과 같다.
    ```

## 3.4 도메인 특화 언어(DSL)은 나중에 따로 알아보자.

- (궁금하다면, scalatest lib를 한 번 사용해보자)

## 3.5 스칼라 if 문

```scala
if (2+2 == 5 ) {
  println("Hello from 1984.")
} else if (2 + 2 == 3) {
  println("Hello from Remedial Math class?")
} else {
  println("Hello from a non-Orwellian future.")
}
```

- 스칼라는 if 식의 결과값을 다른 변수에 저장할 수 있다.
- if 문 자체가 `식` 이므로, 삼항 연산자는 지원하지 않는다.
- 
    ```scala
    val configFile = new java.io.File("somefile.txt")

    val configFilePath = if (configFile.exists()) {
      configFile.getAbsolutePath()
    } else {
      configFile.createNewFile()
      configFile.getAbsolutePath()
    }
    ```

## 3.6 스칼라 for 내장

- 제너레이터 식
- 
    ```scala
    val numbers = List(1,2,3,4,5,6,7,8)
    for (n <- numbers)  // 이걸(<-) 제너레이터 식(generator expression) 이라고 부른다
      println(n)

    for (i <- 1 to 10) println(i)
    ```

- 가드: 값 걸러내기
- 
    ```scala
    // if 식을 추가해서 원하는 원소만 남기는 것
    for (n <- numbers
         if n % 2 == 0) println(n)

    // 가드를 둘 이상 걸 수도 있다.
    for (n <- numbers
         if n % 2 == 0
         if n % 3 == 0) println(n)

    for (n <- numbers
         if n % 2 == 0 && n % 3 == 0) println(n)
    ```

- yield로 값 만들어내기
    - 시각적 효과를 위해 괄호를 중괄호로 변경할 수 있다.
    - tip: for 내장에 식이 하나만 들어가면 괄호를, 여러 식이 들어가면 중괄호를 사용하는 것이 비공식적인 관례이다. 또한 괄호를 사용할 경우 중간에 세미콜론(;)을 추가해야 한다는 사실도 기억하라.
    - 

    ```scala
    val filteredNumbers = for {
      n <- numbers
      if n % 2 == 0
      if n % 3 == 0
    } yield n

    val filteredNumbers2 = for (
      n <- numbers
      if n % 2 == 0
      if n % 3 == 0;
      j <- 10 to 20
      if j % 3 == 0
    ) yield (n, j)

    println(filteredNumbers)
    println(filteredNumbers2)
    ```

- 확장 영역과 값 정의
    - 스칼라의 for 내장은 복잡한 조건을 루프 안에 들어와서 판단하는 것보다 미리 원소를 걸러내는 형태로 작성한다.
    - 
        ```scala
        val dogBreeds = List (
          Some("Doberman"), None, Some("Yorkshire Terrier"),
          Some("Dachshund"), None, Some("Scottish Terrier"),
          None, Some("Great Dane"), Some("Portuguese Water Dog")
        )

        for {
          breedOption <- dogBreeds  // breedOption -> Some(string)
          breed <- breedOption  // breed -> string
          upcasedBreed = breed.toUpperCase()  // upcasedBreed -> string
        } println(upcasedBreed)

        // 아래는 패턴 매칭을 사용해서 더 깔끕하게 만든 것이다.
        for {
          Some(breed) <- dogBreeds  // Some() <- obj 는 Some인 경우에만 성공해서 품종명을 뽑아낸다.
          upcasedBreed = breed.toUpperCase()
        } println(upcasedBreed)
        ```

    - 미리 원소를 걸러내기 때문에 continue, break 등의 명령어를 스칼라에서 제공하지는 않는다.
    - 자바에 객체를 끌어와서 사용할 수는 있다.

## 3.7 다른 루프 표현

- while loop
    - 
    ```scala
    import java.util.Calendar

    def isFridayThirteen(cal: Calendar): Boolean = {
        val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
        val dayOfMonth = cal.get(Calendar.DAY_OF_MONTH)
        (dayOfWeek == Calendar.FRIDAY) && (dayOfMonth == 13)
    }

    var breakPoint = true  // 무한히 돌게 하지 않기 위한 임시 처리
    while(!isFridayThirteen(Calendar.getInstance) && breakPoint) {
        println("Today isn't Friday the 13th. Lame.")
        breakPoint = false
    }
    ```

- do-while loop
    - 
    ```scala
    var count = 0
    do {
        count += 1
        println(count)
    } while(count < 10)
    ```