/*
3.7 다른 루프 표현
 */

// 3.7.1 스칼라 while 루프

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

// 3.7.2 스칼라 do-while 루프

var count = 0
do {
  count += 1
  println(count)
} while(count < 10)

/*
3.8 조건 연산자
  쇼트서킷(short-circuiting) 연산자 : 순차적으로 판단하되, 조건이 항상 만족하면 그 이후는 평가하지 않고 끝낸다
  예시> && , || (and, or)
 */
