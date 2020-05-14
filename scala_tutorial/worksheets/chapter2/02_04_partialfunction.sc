
// partial function!

val pf1: PartialFunction[Any, String] = { case s: String => "YES"}
val pf2: PartialFunction[Any, String] = { case d: Double => "YES"}
val pf = pf1 orElse pf2

def tryPF(x:Any, f: PartialFunction[Any, String]): String =
  try{ f(x).toString } catch { case _: MatchError => "ERROR!"}

def d(x:Any, f:PartialFunction[Any, String]) =
  f.isDefinedAt(x).toString

List("str", 3.14, 10) foreach { x =>
  println("------" + "x is " + x.toString + "------")
  println("pf1 isDefinedAt:  " + d(x, pf1))
  println("pf1 try:  " + tryPF(x, pf1))
  println("pf2 isDefinedAt:  " + d(x, pf2))
  println("pf2 try:  " + tryPF(x, pf2))
  println("pf isDefinedAt:  " + d(x, pf))
  println("pf try:  " + tryPF(x, pf))
}
