
class MapFilterImpl extends RunOps[Int]{
  override def o2(list: List[Int]): List[Int] =
    list.filter(_ % 2 == 0).map(_ + 10)

  override def o3(list: List[Int]): List[Int] =
    list.filter(_ % 2 == 0).map(_ + 10).map(x => x*x)
}
