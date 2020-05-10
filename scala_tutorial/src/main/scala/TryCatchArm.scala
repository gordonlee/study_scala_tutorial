
import scala.language.reflectiveCalls
import scala.util.control.NonFatal

object manage {
  def apply[R <: { def close():Unit }, T](resource: => R)(f: R => T): T = {
    var res: Option[R] = None
    try {
      res = Some(resource)         // Only reference "resource" once!!
      f(res.get)                   // Return the T instance
    } catch {
      case NonFatal(ex) =>
        println(s"manage.apply(): Non fatal exception! $ex")
        throw ex
    } finally {
      if (res != None) {
        println(s"Closing resource...")
        res.get.close()
      }
    }
  }
}

object TryCatchARM {
  /** Usage: scala rounding.TryCatch filename1 filename2 ... */
  def main(args: Array[String]): Unit = {
    val sizes = args map { arg =>
      try {
        val size = returnFileLength(arg)
        println(s"file $arg has $size lines")
        size
      }
      catch {
        case NonFatal(ex) =>
          println(s"caught $ex")
          -1
      }
    }
    println("Returned sizes: " + (sizes.mkString(", ")))
  }

  import scala.io.Source

  def returnFileLength(fileName: String): Int = {
    println()  // Add a blank line for legibility
    manage(Source.fromFile(fileName)) { source =>
      source.getLines.size
    }
  }
}
