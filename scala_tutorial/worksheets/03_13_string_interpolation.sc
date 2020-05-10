/*
3.13 문자열 언터폴레이션

 */
val name = "gordon lee"
println(s"Hello, $name")

// 두 가지 유형이 있다.
  // printf 와 같은 형식화를 지원하며, f를 문자열 앞에 붙인다.
  // raw 문자열 인터폴레이션이라 부르며, \n 과 같은 이스케이프 문자를 변환하지 않는다.
val gross = 10000F
val net = 64000F
val percent = (net / gross) * 100
println(f"$$${gross}%.2f vs. $$${net}%.2f or ${percent}%.1f%%")
println(s"$$${gross}%.2f vs. $$${net}%.2f or ${percent}%.1f%%")
// $ 표현을 위해서 $$ 를 사용한다. % -> %%
// 스칼라는 printf 형식화를 위해 자바의 Formatter를 사용한다.
// 사용한 변수의 타입과 형식 지정이 일치해야 한다. 일부 암시 변환이 적용되기도 한다.
// Int -> Float (OK) , Double -> Int (NG)

// 때로 스칼라 컴파일러가 자바 문자열을 StringLike 로 감싸서 새로운 메서드를 추가하는 경우도 있다.
val s = "%02d: name = %s".format(5, "Gordon Lee")
println(s)

// 아래는 내장 문자열 인터폴레이션은 제어 문자를 확장하지 않은 raw 형식이다.
s"123\n$name\n456"
raw"123\n$name\n456"
