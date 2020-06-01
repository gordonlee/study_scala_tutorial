
val dogBreeds = Seq(Some("Doberman"), None, Some("Yorkshire Terrier"),
  Some("Dachshund"), None, Some("Scottish Terrier"),
  None, Some("Great Dane"), Some("Portuguese Water Dog"))

println("second pass:")
for {
  Some(breed) <- dogBreeds
  upcasedBreed = breed.toUpperCase()
} println(upcasedBreed)


// src/main/scala/progscala2/patternmatching/match-fun-args.sc

case class Address(street: String, city: String, country: String)
case class Person(name: String, age: Int)

val as = Seq(
  Address("1 Scala Lane", "Anytown", "USA"),
  Address("2 Clojure Lane", "Othertown", "USA"))
val ps = Seq(
  Person("Buck Trends", 29),
  Person("Clo Jure", 28))

val pas = ps zip as

// Ugly way:
pas map { tup =>
  val Person(name, age) = tup._1
  val Address(street, city, country) = tup._2
  s"$name (age: $age) lives at $street, $city, in $country"
}

// Nicer way:
pas map {
  case (Person(name, age), Address(street, city, country)) =>
    s"$name (age: $age) lives at $street, $city, in $country"
}



// src/main/scala/progscala2/patternmatching/regex-assignments.sc

val cols = """\*|[\w, ]+"""
val table = """\w+"""
val others = """.*"""
val selectRE =
  s"""SELECT\\s*(DISTINCT)?\\s+($cols)\\s*FROM\\s+($table)\\s*($others)?;""".r

val selectRE(distinct1, cols1, table1, otherClauses) =
  "SELECT DISTINCT * FROM atable;"
val selectRE(distinct2, cols2, table2, otherClauses) =
  "SELECT col1, col2 FROM atable;"
val selectRE(distinct3, cols3, table3, otherClauses) =
  "SELECT DISTINCT col1, col2 FROM atable;"
val selectRE(distinct4, cols4, table4, otherClauses) =
  "SELECT DISTINCT col1, col2 FROM atable WHERE col1 = 'foo';"