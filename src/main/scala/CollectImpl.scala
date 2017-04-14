
class CollectImpl extends RunOps[Int]{
  override def o2(list: List[Int]): List[Int] =
    list.collect {
      case x if x % 2 == 0 =>
        x + 10
    }

  override def o3(list: List[Int]): List[Int] =
    list.collect {
      case x if x % 2 == 0 =>
        val y = x + 10
        y * y
    }
}

