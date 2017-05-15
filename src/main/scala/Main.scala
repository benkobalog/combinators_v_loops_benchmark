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
    val twoOps =
      limits
        .map(_.toInt)
        .map(measureFns(mapFilter.o2, collect.o2, arrayBuffer.o2))

    println("L vs M  | C vs M  | C vs L  | limit")
    twoOps.foreach{ case (ml, mc, cl, limit)
      => println(f"$ml%1.5f | $mc%1.5f | $cl%1.5f | $limit") }

    println("======= Measure with 3 operations =======")
    val threeOps =
      limits
        .map(_.toInt)
        .map(measureFns(mapFilter.o3, collect.o3, arrayBuffer.o3))

    println("L vs M  | C vs M  | C vs L  | limit")
    threeOps.foreach{ case (ml, mc, cl, limit)
      => println(f"$ml%1.5f | $mc%1.5f | $cl%1.5f | $limit") }

    println("M -> Map Filter | C -> Collect | L -> Loop")
  }


  def measureFns(
                  mapFilterOp: List[Int] => List[_],
                  collectOp: List[Int] => List[_],
                  loopOp: List[Int] => ArrayBuffer[Int]
                )(upperLimit: Int) = {
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

    val mfPerLoopTimes = mapFilterTime.value / loopTime.value
    val mfPerCollectTimes = mapFilterTime.value / collectTime.value
    val collectPerLoop = loopTime.value / collectTime.value

    (mfPerLoopTimes , mfPerCollectTimes, collectPerLoop, upperLimit)
  }
}
