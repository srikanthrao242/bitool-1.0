package com.bitool.analytics.doc.tasks

import akka.http.scaladsl.model.StatusCodes.BadRequest
import akka.http.scaladsl.server.{Directives, Route}
import com.bitool.analytics.util.{CirceSupportForAkkaHttp, ErrorResponse}
import io.circe._

/**
  * Abastract base class for TaskHandlers with a default behavior of returning a BadRequest response in the event
  * that the inner json cannot be decoded.
  *
  * @author srikanth
  */
abstract class TaskHandlerBase[T <: Task]
  extends TaskHandler
    with CirceSupportForAkkaHttp
    with Directives {

  override def handleJson(json: Json): Route = {
    decoder.decodeJson(json).fold(
      decodingFailure => {
        import io.circe.generic.auto._
        complete(BadRequest, ErrorResponse(BadRequest.intValue, decodingFailure.toString()))
      },
      taskRequest => handleTask(taskRequest)
    )
  }

  def handleTask(taskRequest: T): Route
  def decoder: Decoder[T]
}
