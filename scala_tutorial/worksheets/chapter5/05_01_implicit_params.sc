/*
  5.1 암시적 인자

  참고
    https://www.scala-lang.org/api/current/scala/Predef$.html
    https://docs.scala-lang.org/tutorials/FAQ/context-bounds.html  // context-bound
 */
def calcTax(amount: Float)(implicit rate: Float): Float = amount * rate

{
  implicit val currentTaxRate = 0.08F
  // implicit val currentTaxRate2 = 0.1F  // error: ambiguous implicit values
  val tax = calcTax(5000F)  // 이 메서드를 호출하는 코드에서는 지역 범위에 있는 암시 값을 사용.
  println(s"$tax")
}

// rate 에 대하여 어떤 경우는 '세금 휴일'을 적용할 수도 있다.

object SimpleStateSalesTax {
  implicit val rate: Float = 0.05F
}

case class ComplicatedSalesTaxData(
                                  baseRate: Float
                                  , isTaxHoliday: Boolean
                                  , storeId: Int
                                  )

object ComplicatedSalesTax {
  private def extraTaxRateForStore(id: Int): Float = {
    0.0F
  }

  implicit def rate(implicit cstd: ComplicatedSalesTaxData): Float = {
    if (cstd.isTaxHoliday) 0.0F
    else cstd.baseRate + extraTaxRateForStore(cstd.storeId)
  }
}

{
  import SimpleStateSalesTax.rate
  val amount = 100F
  println(s"Tax on $amount = ${calcTax(amount)}")
}

{
  import ComplicatedSalesTax.rate
  implicit val myStore = ComplicatedSalesTaxData(0.06F, false, 1010)
  // implicit val myStore = ComplicatedSalesTaxData(0.06F, true, 1010)

  val amount = 100F

  //ComplicatedSalesTax.rate()
  /*여기는 rate value 가 들어가야 하는 자리다. 임포트한 obj의 rate가 주입된다.
  아래와 같은 구문이 암시로 처리된다고 생각할 수 있다.
  * */
  val temp = calcTax(amount)(ComplicatedSalesTax.rate(myStore))
  println(s"Tax on $amount = $temp")
  println(s"Tax on $amount = ${calcTax(amount)}")  // myStore.rate
}

/*
  5.1.1 implicity 사용하기

  Predef에 있는 implicity method 와 타입 시그니처를 조합하면,
  매개변수화한 타입인 암시적 인자를 단 하나만 사용하는 메서드의 시그니처를 정의할 때 유용하다.
 */

// src/main/scala/progscala2/implicits/implicitly-args.sc

{
  import math.Ordering

  case class MyList[A](list: List[A]) {
    def sortBy1[B](f: A => B)(implicit ord: Ordering[B]): List[A] =
      list.sortBy(f)(ord)

    def sortBy2[B: Ordering](f: A => B): List[A] =
      list.sortBy(f)(implicitly[Ordering[B]])
    // sortBy2를 맥락 바운드(context bound) 라고 한다.
    // implicitly[Ordering[B]] 는 말로 풀어서 설명하면,
    // Ordering[B] 타입에 대한 암시(implicit)적 인스턴스를 지칭한다 라고 할 수 있다.
    def sort2Declare[B: Ordering] = {
      val e: Ordering[B] = implicitly[Ordering[B]]
      e
      // 여기서 e 인스턴스는 Ordering[B] 이며, 암시 파라메터이기 때문에 스코프에서 가져올 수 있는
      // 인스턴스를 가져온다
    }
  }

  val list = MyList(List(1, 3, 5, 2, 4))
  val e1 = list sortBy1 (i => -i)
  val e2 = list sortBy2[Integer] (i => -i)

  // sortBy1은 (f)(implicit ord)를 파라메터로 받는다. 하지만 ord 는 별도로 정의하지 않았다.
  // 어디서 온 것 일까?
  /*
    Ordering.scala file 을 보면 아래와 같은 구문이 있다. (추측임)

  implicit object Int extends IntOrdering

  trait LongOrdering extends Ordering[Long] {
    def compare(x: Long, y: Long) =
      if (x < y) -1
      else if (x == y) 0
      else 1
  }

  implicitly는 아래와 같은 선언을 Predef.scala 에서 하고 있다.
  @inline def implicitly[T](implicit e: T) = e    // for summoning implicit values from the nether world -- TODO: when dependent method types are on by default, give this result type `e.type`, so that inliner has better chance of knowing which method to inline in calls like `implicitly[MatchingStrategy[Option]].zero`
   */

  println(s"e1 is $e1 \n e2 is $e2")

  /*
  NOTE: context bound 와 implicitly 메서드를 결합한 것은 매개변수화한 타입의 암시적 인자가 필요한
  특별한 경우를 짧게 쓸 수 있는 방식이다. 여기서 타입 매겨변수는 범위 안에 있는 다른 타입 중 하나여야 한다.
  (예를 들면 암시적 Ordering[B] 타입 매개변수에 대해 [B: Ordering])
   */
}