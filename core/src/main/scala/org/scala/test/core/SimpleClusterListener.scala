package org.scala.test.core

import akka.actor.{Actor, ActorLogging}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent._

class SimpleClusterListener extends Actor with ActorLogging {

  val cluster = Cluster(context.system)

  // subscribe to cluster changes, re-subscribe when restart
  override def preStart() {
    cluster.subscribe(self, InitialStateAsEvents, classOf[MemberEvent],
      classOf[UnreachableMember])
  }

  override def postStop() {
    cluster.unsubscribe(self)
  }

  def receive = {
    case MemberUp(member) =>
      //log.info("Member is Up: {}", member.address)
      println(member.address)
    case UnreachableMember(member) =>
      println(member.address)
    // log.info("Member detected as unreachable: {}", member)
    case MemberRemoved(member, previousStatus) =>
      // log.info("Member is Removed: {} after {}", member.address, previousStatus)
      println(member.address)
    case x: MemberEvent =>
      println(x)
  }
}
