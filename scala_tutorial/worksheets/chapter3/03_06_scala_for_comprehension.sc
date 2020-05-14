/*
3.6 스칼라 for 내장
 */

// 3.6.1 for 루프 기본식
// 3.6.2 제너레이터 식
val numbers = List(1,2,3,4,5,6,7,8)
for (n <- numbers)  // 이걸(<-) 제너레이터 식(generator expression) 이라고 부른다
  println(n)

for (i <- 1 to 10) println(i)

// 3.6.3 가드: 값 걸러내기
// if 식을 추가해서 원하는 원소만 남기는 것
for (n <- numbers
     if n % 2 == 0) println(n)

// 가드를 둘 이상 걸 수도 있다.
for (n <- numbers
     if n % 2 == 0
     if n % 3 == 0) println(n)

for (n <- numbers
     if n % 2 == 0 && n % 3 == 0) println(n)

// 3.6.4 yield로 값 만들어내기
// 시각적 효과를 위해 괄호를 중괄호로 변경할 수 있다.
// tip: for 내장에 식이 하나만 들어가면 괄호를, 여러 식이 들어가면 중괄호를 사용하는 것이 비공식적인 관례이다.
// 또한 괄호를 사용할 경우 중간에 세미콜론(;)을 추가해야 한다는 사실도 기억하라.
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

// 3.6.5 확장 영역과 값 정의
// 스칼라의 for 내장은 복잡한 조건을 루프 안에 들어와서 판단하는 것보다 미리 원소를 걸러내는 형태로 작성한다.
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

// 미리 원소를 걸러내기 때문에 continue, break 등의 명령어를 스칼라에서 제공하지는 않는다.
// 자바에 객체를 끌어와서 사용할 수는 있다.