package org.scala.test.leader

import akka.actor.{ActorSystem, Props}
import akka.cluster.Cluster
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.complete
import akka.http.scaladsl.server.Route
import org.scala.test.core.SimpleClusterListener

import java.util.concurrent.{Executors, TimeUnit}

object Leader extends App {

  // import akka.discovery.{Discovery, Lookup}

  implicit val system: ActorSystem = {
    ActorSystem.create("system")
  }

  system.actorOf(Props[SimpleClusterListener], "clusterListener")

  val cluster = Cluster(system)

  val executor = Executors.newSingleThreadScheduledExecutor()

  executor.scheduleAtFixedRate(() => {

    println(cluster.state.members)

  }, 3000, 3000, TimeUnit.MILLISECONDS)

  Http().newServerAt("0.0.0.0", 80).bind(routes)

  protected def routes: Route = complete("")

}
