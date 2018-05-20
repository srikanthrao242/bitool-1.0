package com.bitool.analytics

import java.io.{PrintWriter, StringWriter}
import java.util.ServiceLoader

import akka.http.scaladsl.model.StatusCodes.InternalServerError
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ExceptionHandler, Route, RouteConcatenation}
import com.bitool.analytics.akkacore.AkkaCoreModule
import com.bitool.analytics.doc.tasks.{TaskHandlerProviders, TaskHandlerSPI, TaskService}
import com.bitool.analytics.sparkcore.SparkCoreModule
import com.bitool.analytics.swagger.SwaggerDocService
import com.bitool.analytics.util.{CirceSupportForAkkaHttp, ErrorResponse}
import com.typesafe.scalalogging.LazyLogging
import io.circe.generic.auto._

import scala.collection.JavaConverters._

/**
  * The REST API layer. It exposes the REST services, but does not provide any
  * web server interface.<br/>
  * Notice that it requires to be mixed in with ``core.CoreActors``, which provides access
  * to the top-level actors that make up the system.
  */

trait RootRoutes extends RouteConcatenation with CirceSupportForAkkaHttp with LazyLogging {
  this: AkkaCoreModule
  with SparkCoreModule =>

  val exceptionHandler = ExceptionHandler {
    case exception: Exception =>
      val sw = new StringWriter
      exception.printStackTrace(new PrintWriter(sw))
      logger.error("uncaught exception", exception)
      complete(InternalServerError, ErrorResponse(InternalServerError.intValue, s"uncaught exception: $sw")
    )
  }

  private val taskHandlers = new TaskHandlerProviders().getTaskHandlers//ServiceLoader.load(classOf[TaskHandlerSPI]).asScala.flatMap(_.getTaskHandlers).toSeq

  val routes: Route =
    pathPrefix("api") {
      handleExceptions(exceptionHandler) {
        new TaskService(taskHandlers).routes
      }
    } ~
      new SwaggerDocService().routes
      //new MetricsService().routes
}
