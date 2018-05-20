package com.bitool.analytics.doc.tasks

import javax.ws.rs.Path

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.{Directives, Route, RouteConcatenation}
import com.bitool.analytics.util.{CirceSupportForAkkaHttp, ErrorResponse, SuccessResponse}
import com.typesafe.scalalogging.LazyLogging
import io.circe.Json
import io.swagger.annotations._
import io.circe.generic.auto._

import scala.concurrent.ExecutionContext

@Api("tasks")
@Path("/tasks")
class TaskService(taskHandlers: Seq[TaskHandler])
                 (implicit ec: ExecutionContext)
  extends RouteConcatenation
    with Directives
    with CirceSupportForAkkaHttp
    with LazyLogging {

  def routes: Route =
    pathPrefix("tasks") {
      pathEndOrSingleSlash {
        executeTask
      }
    }

  @ApiOperation(value = "Execute task",
    notes = "Executes a task",
    nickname = "executeTask", httpMethod = "POST", consumes = "application/json", produces = "application/json")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "body", value = "task request", required = true, paramType = "body", dataTypeClass = classOf[Json])
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Task completed", response = classOf[SuccessResponse]),
    new ApiResponse(code = 202, message = "Task accepted", response = classOf[SuccessResponse]),
    new ApiResponse(code = 400, message = "Bad request", response = classOf[ErrorResponse]),
    new ApiResponse(code = 500, message = "Internal server error", response = classOf[ErrorResponse])
  ))
  def executeTask: Route =
    post {
      entity(as[Json]) { taskRequest =>
        val taskTypeOption = taskRequest.hcursor.get[String]("type").toOption
        taskTypeOption match {
          case None => complete(BadRequest, ErrorResponse(BadRequest.intValue, "task type undefined"))
          case Some(taskType) =>
            val taskArgsOption = taskRequest.hcursor.get[Json]("args").toOption
            taskArgsOption match {
              case None => complete(BadRequest, ErrorResponse(BadRequest.intValue, "task args undefined"))
              case Some(taskArgs) => handleTask(taskType, taskArgs)
            }
        }
      }
    }

  private def handleTask(taskType: String, json: Json): Route = {
    val maybeHandler: Option[TaskHandler] = taskHandlers.find(_.taskType == taskType)
    if (maybeHandler.isEmpty)
      complete(BadRequest, ErrorResponse(BadRequest.intValue, s"unknown taskType: $taskType"))
    else
      maybeHandler.get.handleJson(json)
  }
}
