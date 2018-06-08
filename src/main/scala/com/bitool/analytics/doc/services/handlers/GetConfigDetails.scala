package com.bitool.analytics.doc.services.handlers

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Route
import com.bitool.analytics.doc.services.ScenarioArguments
import com.bitool.analytics.doc.services.core.GetData
import com.bitool.analytics.doc.services.handlers.CreateTable.TableAccepted
import com.bitool.analytics.doc.services.handlers.GetConfigDetails.{GetConfigReq, RequestAccepted}
import com.bitool.analytics.doc.tasks.{Task, TaskHandlerBase}
import com.bitool.analytics.util.LazyLogging
import io.circe.Decoder
import io.circe.generic.auto._
import io.circe.generic.semiauto._

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

/**
  * Created by srikanth on 6/8/18.
  */
object GetConfigDetails {
  /**
    * Incoming request to create a scenario
    * @param name name for the scenario
    * @tparam A the application-specific scenario arguments type
    */
  case class GetConfigReq[A <: ScenarioArguments](name: String,args:A) extends Task

  /**
    * Outgoing response when a scenario request has been successfully initiated and is underway
    * @param isDone isCreated or not
    * @param data responce
    */
  case class RequestAccepted[B](isDone: Boolean,data:B)

}
class GetConfigDetails[A<:ScenarioArguments] (implicit argDecoder: Decoder[A],
                        actorSystem: ActorSystem,
                        ec: ExecutionContext)
  extends TaskHandlerBase[A]
    with LazyLogging {

  override def taskType: String = "GET_CONFIG_DETAILS"

  override def decoder:  Decoder[GetConfigReq[A]] = deriveDecoder[GetConfigReq[A]]

  override def handleTask(taskRequest: A): Route = {
    val db = new GetData()
    val response = db.getDataSources
    onComplete(response){
      case Success(res)=> complete(Accepted,RequestAccepted(isDone = true,res))
      case Failure(ex)    => complete((InternalServerError, s"An error occurred: ${ex.getMessage}"))
    }
  }
}
