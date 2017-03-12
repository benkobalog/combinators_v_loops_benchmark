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
    measureFns(1e3.toInt)
    measureFns(1e4.toInt)
    measureFns(1e5.toInt)
    measureFns(1e6.toInt)

  }

  def combinator2op(list: List[Int]) = {

    val l2 = list.filter(_ % 2 == 0).map(_ + 10)
  }

  def loop2op(list: List[Int]) = {

    val array = ArrayBuffer[Int]()
    list.foreach { x =>
      if(x % 2 == 0) {
        array.append(x + 10)
      }
    }
  }

  def measureFns(
                  upperLimit: Int
                ): Unit = {
    val list = (0 to upperLimit).toList

    val combinatorTime = standardConfig.measure {
      combinator2op(list)
    }

    val loopTime = standardConfig.measure {
      loop2op(list)
    }
    println(s"combinators time: $combinatorTime ms with $upperLimit upper limit")
    println(s"      array time: $loopTime ms with $upperLimit upper limit")
    println()
  }

}
