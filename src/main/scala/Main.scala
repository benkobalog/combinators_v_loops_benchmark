import org.scalameter._

import scala.collection.mutable.ArrayBuffer

object Main {

  val standardConfig = config(
    Key.exec.minWarmupRuns -> 40,
    Key.exec.maxWarmupRuns -> 80,
    Key.exec.benchRuns -> 120,
    Key.verbose -> false
  ).withWarmer(new Warmer.Default)

  def main(args: Array[String]): Unit = {

    val mapFilter = new MapFilterImpl
    val collect = new CollectImpl
    val arrayBuffer = new ArrayImpl


    val limits: List[Double] = List(1e3, 2e3, 5e3,
                                    1e4, 2e4, 5e4,
                                    1e5, 2e5, 5e5,
                                    1e6)
    println("======= Measure with 2 operations =======")
    limits.map(_.toInt).foreach(measureFns(mapFilter.o2, collect.o2, arrayBuffer.o2))
    println("======= Measure with 3 operations =======")
    limits.map(_.toInt).foreach(measureFns(mapFilter.o3, collect.o3, arrayBuffer.o3))
  }


  def measureFns(
                  mapFilterOp: List[Int] => List[_],
                  collectOp: List[Int] => List[_],
                  loopOp: List[Int] => ArrayBuffer[Int]
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
    val mfPerLoopTimes = mapFilterTime.value / loopTime.value
    val mfPerCollectTimes = mapFilterTime.value / collectTime.value
    val collectPerLoop = collectTime.value / loopTime.value
    println(s"map filter /    loop: $mfPerLoopTimes")
    println(s"map filter / collect: $mfPerCollectTimes")
    println(s"collect    /    loop: $collectPerLoop")
    println(s"loop       / collect: ${1.0 / collectPerLoop}")
    println()
  }
}
