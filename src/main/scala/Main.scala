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
    val limits: List[Double] = List(1e3, 2e3, 5e3,
                                    1e4, 2e4, 5e4,
                                    1e5, 2e5, 5e5,
                                    1e6)
    println("======= Measure with 2 operations =======")
    limits.map(_.toInt).foreach(measureFns(mapFilter2op(_), collect2op(_), loop2op(_)))
    println("======= Measure with 3 operations =======")
    limits.map(_.toInt).foreach(measureFns(mapFilter3op(_), collect3op(_), loop3op(_)))
  }

  def mapFilter2op(list: List[Int]) = {
    list.filter(_ % 2 == 0).map(_ + 10)
  }

  def collect2op(list: List[Int]) = {
    list.collect{
      case x if x % 2 == 0 =>
        x + 10
    }
  }

  def loop2op(list: List[Int]) = {

    val array = ArrayBuffer[Int]()
    val len = list.length
    var x = 0
    while(x < len) {
      if(x % 2 == 0) {
        array.append(x + 10)
      }
      x += 1
    }
  }

  def mapFilter3op(list: List[Int]) = {
    list.filter(_ % 2 == 0).map(_ + 10).map(x => x*x)
  }

  def collect3op(list: List[Int]) = {
    list.collect{
      case x if x % 2 == 0 =>
        val y = x + 10
        y * y
    }
  }

  def loop3op(list: List[Int]) = {

    val array = ArrayBuffer[Int]()
    val len = list.length
    var x = 0
    while(x < len) {
      if(x % 2 == 0) {
        val y = x + 10
        val z = y * y
        array.append(z)
      }
      x += 1
    }
  }

  def measureFns(
                  mapFilterOp: List[Int] => Unit,
                  collectOp: List[Int] => Unit,
                  loopOp: List[Int] => Unit
                )(upperLimit: Int): Unit = {
    val list = (0 to upperLimit).toList

    val mapFilterTime = standardConfig.measure {
      mapFilterOp(list)
    }

    val collectTime = standardConfig.measure {
      collectOp(list)
    }

    val loopTime = standardConfig.measure {
      loopOp(list)
    }

    println(s"map filter time: $mapFilterTime on $upperLimit elements")
    println(s"   collect time: $collectTime on $upperLimit elements")
    println(s"      loop time: $loopTime on $upperLimit elements")
    val mfPerLoop = mapFilterTime.value - loopTime.value
    val mfPerLooptimes = mapFilterTime.value / loopTime.value
    val mfPerCollectTimes = mapFilterTime.value / collectTime.value
    val collectPerLoop = collectTime.value / loopTime.value
    println(s"map filter /    loop: $mfPerLooptimes")
    println(s"map filter / collect: $mfPerCollectTimes")
    println(s"collect    /    loop: $collectPerLoop")
    println(s"loop       / collect: ${1.0 / collectPerLoop}")
    println()
  }
}
