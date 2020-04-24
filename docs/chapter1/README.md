# chapter 1. 스칼라 소개

## scala?

- 공식 홈페이지

    ![chapter%201/_2020-04-16__11.08.26.png](chapter%201/_2020-04-16__11.08.26.png)

- 객체지향 프로그래밍과 함수형 프로그래밍이 결합된 높은 수준의 언어다. 정적 타입은 복잡한 앱에서의 버그를 피하게 도와주고, JVM 과 JS 런타임은 높은 성능을 가진 시스템을 쉽게 접근할 수 있게 해준다.

## 1.1 왜 스칼라인가?

- JVM과 JS
    - JVM에서 제공하는 라이브러리를 계승할 수 있다는 장점이 있다.
    - scala.js 도 실험 프로젝트로 진행 중이다.
- 정적타입
    - 정적 타입 지정이 필요하지만 타입 추론을 이용하여 타입 표기를 생략할 수 있게 한다.
- 다중 패러다임 - 객체지향 + 함수형 프로그래밍
- 복잡한 타입 시스템
    - 타입 추론을 사용하여 동적 타입 언어 수준으로 간결하게 할 수 있다.
- 간결하고 우아하며 유연한 문법
- 규모 확장성
    - trait를 사용한 혼합 합성, 추상타입멤버와 제네릭스, 내포 클래스, 명시적인 자기 타입

scalable language → scala. 2001년 마틴 오더스키가 시작

## 1.2 스칼라 설치하기

환경 구성

- scala 2.11.2
- java 1.8.0_241
- sbt build

이후부터는 Intellij 를 이용하여 코딩한다. 

- 스크린샷으로 보는 Intellij 설정하기

    ![chapter%201/Untitled.png](chapter%201/Untitled.png)

    ![chapter%201/Untitled%201.png](chapter%201/Untitled%201.png)

    ![chapter%201/Untitled%202.png](chapter%201/Untitled%202.png)

    ![chapter%201/Untitled%203.png](chapter%201/Untitled%203.png)

    ![chapter%201/Untitled%204.png](chapter%201/Untitled%204.png)

    여기까지 했다면 준비 완료

1.2.1 SBT 사용하기 

- Intellij의 하단에 위치한 `sbt shell`에서 동일한 작업을 할 수 있다.

    ![chapter%201/Untitled%205.png](chapter%201/Untitled%205.png)

    ![chapter%201/Untitled%206.png](chapter%201/Untitled%206.png)

1.2.2 스칼라 명령행 도구 실행하기 

- 하려면 할 수 있으나 intellij가 다운받은 path 등을 고려하여 접근해야 해서 포기함.
- 우회하는 방법으로 scala 공식사이트에서 바이너리를 다운받아 bin 폴더에서 명령하면 잘 동작한다. (특히 윈도우에서는 path등의 문제가 ㅂㄷㅂㄷ)

1.2.3 IDE에서 스칼라 REPL 실행하기 

- REPL?
    - Read, Eval, Print, Loop
- IDE 활용
    - .scala 로 클래스를 만들어 compile → execution 하는 방법
    - .sc 로 만들어서 worksheet 으로 바로 실행하는 방법
    - 위 두 가지 방법을 혼용해가며 코딩할 예정이다.

## 1.3 스칼라 맛보기

- 별 내용 없어요.. 각자 읽으면서 해보기
- scalap 를 이용하여 class 파일 열어보기
    - 

        ![chapter%201/Untitled%207.png](chapter%201/Untitled%207.png)

- object
    - object 로 선언한 객체는 싱글턴(singleton) 객체다. 스칼라는 싱글턴 디자인 패턴을 1급 요소로 받아들였다.
    - 이는 싱글턴이기 때문에 new로 객체를 할당할 수 없다. (그럴 필요도 없다)
- 위치 지정자 (placeholder, _)
    - 

        (s: String) => s.toUpperCase()
        _.toUpperCase() // 위와 같은 의미이다.

## 1.4 동시성 맛보기

- 이것도 그냥 각자 알아서...

## 참고

- scalac (compile option)
    - [https://docs.scala-lang.org/overviews/compiler-options/index.html](https://docs.scala-lang.org/overviews/compiler-options/index.html)
- web interpreter
    - 공식 홈페이지에서 호스팅
        - [https://scastie.scala-lang.org/mEG7gtjVRqKVqrFFjDhmGQ](https://scastie.scala-lang.org/mEG7gtjVRqKVqrFFjDhmGQ)
    - scalaFiddle
        - [https://scalafiddle.io/](https://scalafiddle.io/)
- 책에서 제공하는 샘플 코드
    - [https://github.com/deanwampler/programming-scala-book-code-examples](https://github.com/deanwampler/programming-scala-book-code-examples)