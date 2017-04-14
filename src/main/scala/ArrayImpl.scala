import scala.collection.mutable.ArrayBuffer

class ArrayImpl extends RunOps[Int]{
  override def o2(list: List[Int]): ArrayBuffer[Int] = {
    val array = ArrayBuffer[Int]()
    val len = list.length
    var x = 0
    while(x < len) {
      if(x % 2 == 0) {
        array.append(x + 10)
      }
      x += 1
    }
    array
  }

  override def o3(list: List[Int]): ArrayBuffer[Int] = {
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
    array
  }
}
