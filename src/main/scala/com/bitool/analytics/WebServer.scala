package com.bitool.analytics

import akka.http.scaladsl.Http
import com.bitool.analytics.akkacore.AkkaCoreModule
import com.typesafe.scalalogging.LazyLogging

import scala.async.Async.{async, await}

/**
  * Starts a web server and begins listening.
  *
  * @author kcpaul
  */
trait WebServer extends LazyLogging {
  this: AkkaCoreModule
    with RootRoutes =>

  private val port = Option(System.getenv("PORT")).map(_.toInt).getOrElse(8080)

  private val binding = Http().bindAndHandle(routes, "0.0.0.0", port)

  async {
    await(binding)
    logger.info(s"server listening on port $port")
  }
}
