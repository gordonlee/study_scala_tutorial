
object ImplicitComplieTest {

  implicit var rate: Float = 0.08F

  def calcTax(amount: Float)(implicit rate: Float): Float = amount * rate

  def main(args: Array[String]) = {
    rate = 0.09F
    println(calcTax(500F))
  }
}
