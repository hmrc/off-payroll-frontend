import uk.gov.hmrc.offpayroll.models._

val wb = OffPayrollWebflow

val l = wb.clusters.flatMap(_.clusterElements)
  .map(a => s"cluster ${a.clusterParent.clusterID} children ${a.children.size} type ${a.elementType}")

l(0)
l(1)
l(2)
l(3)
l(4)
l(5)
l(6)
l(7)
l(8)
l(9)
l(10)
l(11)
l(12)
l(13)
l(14)
l(15)








