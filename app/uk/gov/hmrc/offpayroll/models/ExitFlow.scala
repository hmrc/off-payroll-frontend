package uk.gov.hmrc.offpayroll.models

/**
  * Created by peter on 11/01/2017.
  */
object ExitFlow extends Webflow {

  override def version: String = "1.0.1-beta"

  override def getNext(currentElement: Element): Option[Element] = getNext(currentElement, clusters(0))

  override def getStart(): Element = getElementById(0,0).get

  override def getElementById(clusterId: Int, elementId: Int): Option[Element] = {
    if(clusterId == 0 && elementId < clusters(0).clusterElements.size)
      Option(clusters(0).clusterElements(elementId))
    else
      Option.empty
  }

  override def getElementByTag(tag: String): Option[Element] =
    clusters.head.clusterElements.find(element => element.questionTag == tag)

  override def clusters: List[Cluster] =
    List(ExitCluster)

  override def getClusterByName(name: String): Cluster =
    clusters.find(cluster => cluster.name == name).get

}
