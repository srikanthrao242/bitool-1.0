package com.bitool.analytics

import akka.http.scaladsl.Http
import com.bitool.analytics.akkacore.AkkaCoreModule
import com.bitool.analytics.util.LazyLogging
import org.apache.log4j.{BasicConfigurator, Level, Logger}

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

  BasicConfigurator.configure()

  private val rootLogger = Logger.getRootLogger

  //rootLogger.setLevel(Level.ERROR)

  /*Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)*/
  async {
    await(binding)
    println("srikanth application is running dont care about log")
    logger.info(s"server listening on port $port")
  }
}
