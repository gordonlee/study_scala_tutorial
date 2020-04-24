class part1_intro {
  def test_func(): Unit = {
    println("test function")
  }
}

object part1_intro {
  def main(args: Array[String]): Unit = {
    println("Hello, world!")
    val inst: part1_intro = new part1_intro()
    inst.test_func()
  }
}