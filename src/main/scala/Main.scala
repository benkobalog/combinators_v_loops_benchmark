import org.scalameter._

import scala.collection.mutable.ArrayBuffer

object Main {

  val standardConfig = config(
    Key.exec.minWarmupRuns -> 40,
    Key.exec.maxWarmupRuns -> 80,
    Key.exec.benchRuns -> 120,
    Key.verbose -> false
  ) withWarmer(new Warmer.Default)



  def main(args: Array[String]): Unit = {
    val limits: List[Double] = List(1e3, 1e4, 1e5, 1e6)
    println("Measure with 2 operations")
    limits.map(_.toInt).foreach(measureFns(combinator2op(_), loop2op(_)))
    println("Measure with 3 operations")
    limits.map(_.toInt).foreach(measureFns(combinator3op(_), loop3op(_)))
  }

  def combinator2op(list: List[Int]) = {
    list.filter(_ % 2 == 0).map(_ + 10)
  }

  def loop2op(list: List[Int]) = {

    val array = ArrayBuffer[Int]()
    list.foreach { x =>
      if(x % 2 == 0) {
        array.append(x + 10)
      }
    }
  }

  def combinator3op(list: List[Int]) = {
    list.filter(_ % 2 == 0).map(_ + 10).map(x => x*x)
  }

  def loop3op(list: List[Int]) = {

    val array = ArrayBuffer[Int]()
    list.foreach { x =>
      if(x % 2 == 0) {
        val y = x + 10
        val z = y * y
        array.append(z)
      }
    }
  }

  def measureFns(
                  combOp: List[Int] => Unit,
                  loopOp: List[Int] => Unit
                )(upperLimit: Int): Unit = {
    val list = (0 to upperLimit).toList

    val combinatorTime = standardConfig.measure {
      combOp(list)
    }

    val loopTime = standardConfig.measure {
      loopOp(list)
    }

    println(s"combinators time: $combinatorTime ms with $upperLimit upper limit")
    println(s"      array time: $loopTime ms with $upperLimit upper limit")
    println(s"diff: ${combinatorTime.value - loopTime.value}")
    println()
  }

}
