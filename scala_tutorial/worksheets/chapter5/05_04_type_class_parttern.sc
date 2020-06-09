case class Address(street: String, city: String)
case class Person(name: String, address: Address)

trait ToJSON {
  def toJSON(level: Int = 0): String

  val INDENTATION = "  "
  def indentation(level: Int = 0): (String,String) =
    (INDENTATION * level, INDENTATION * (level+1))
}

implicit class AddressToJSON(address: Address) extends ToJSON {
  override def toJSON(level: Int = 0): String = {
    val (outdent, indent) = indentation(level)
    s"""{
       |${indent}"street": "${address.street}",
       |${indent}"city":   "${address.city}"
       |${outdent}}"""//.stripMargin
  }
}

implicit class PersonToJSON(person: Person) extends ToJSON {
  override def toJSON(level: Int = 0): String = {
    val (outdent, indent) = indentation(level)
    s"""{
       |${indent}"name":    "${person.name}",
       |${indent}"address": ${person.address.toJSON(level + 1)}
       |${outdent}}"""// .stripMargin
  }
}

val a = Address("1 Scala Lane", "Anytown")
val p = Person("Buck Trends", a)

println(a.toJSON())
println()
println(p.toJSON())

// 다른 언어의 확장 메서드(extension method) 방식으로 사용할 수 있다.
// 다른 관점에서는 임의 다형성(ad hoc polymorphism) 이라고 한다.


/*
  5.5 ~ 5.9는 그냥 읽어 보세요~
 */