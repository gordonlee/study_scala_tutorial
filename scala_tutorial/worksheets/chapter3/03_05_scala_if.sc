/*
3.5 스칼라 if 문
 */

if (2+2 == 5 ) {
  println("Hello from 1984.")
} else if (2 + 2 == 3) {
  println("Hello from Remedial Math class?")
} else {
  println("Hello from a non-Orwellian future.")
}

// 스칼라는 if 식의 결과값을 다른 변수에 저장할 수 있다.
val configFile = new java.io.File("somefile.txt")

val configFilePath = if (configFile.exists()) {
  configFile.getAbsolutePath()
} else {
  configFile.createNewFile()
  configFile.getAbsolutePath()
}
// if문 자체가 식 이므로, 삼항 연산자는 지원하지 않는다. (기능 중복)
