println("hello, scala worksheet!")

class Upper {
  def upper(strings:String*): Seq[String] = {
    strings.map((s:String) => s.toUpperCase())
  }
}

val up = new Upper
println("Hello", "World!")
println(up.upper("Hello", "World!"))


