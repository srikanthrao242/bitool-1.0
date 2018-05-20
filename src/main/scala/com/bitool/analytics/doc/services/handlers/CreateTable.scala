package com.bitool.analytics.doc.services.handlers

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server._
import com.bitool.analytics.doc.RequestArgs.CREATE_TABLE
import com.bitool.analytics.doc.services.{DataBase, ScenarioArguments}
import com.bitool.analytics.doc.services.handlers.CreateTable.{CreateTableRequest, TableAccepted}
import com.bitool.analytics.doc.tasks.{Task, TaskHandlerBase}
import com.bitool.analytics.util.ErrorResponse
import com.typesafe.scalalogging.LazyLogging
import io.circe.Decoder
import io.circe.generic.semiauto._
import io.circe.generic.auto._

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

/**
  * Created by srikanth on 5/20/18.
  */
object CreateTable {

  /**
    * Incoming request to create a scenario
    * @param name name for the scenario
    * @param args scenario arguments (application-specific)
    * @tparam A the application-specific scenario arguments type
    */
  case class CreateTableRequest[A <: ScenarioArguments](name: String, args: A) extends Task

  /**
    * Outgoing response when a scenario request has been successfully initiated and is underway
    * @param isCreated isCreated or not
    */
  case class TableAccepted(isCreated: Boolean)

}

/**
  * Handles scenario creation requests
  *
  * @param argDecoder circe decoder capable of decoding the application-specific scenario arguments
  * @tparam A the application-specific scenario arguments type
  * @author SRIKANTH
  */
class CreateTableHandler[A <: ScenarioArguments](implicit argDecoder: Decoder[A],
                                                    actorSystem: ActorSystem,
                                                    ec: ExecutionContext)
  extends TaskHandlerBase[CreateTableRequest[A]]
    with LazyLogging {

  override def taskType: String = "CREATE_TABLE"

  override def decoder: Decoder[CreateTableRequest[A]] = deriveDecoder[CreateTableRequest[A]]

  override def handleTask(task: CreateTableRequest[A]): Route = {
    val db = new DataBase()
    val response = db.createTable(task.args.asInstanceOf[CREATE_TABLE])
    onComplete(response){
      case Success(isCreated)=> complete(Accepted,TableAccepted(true))
      case Failure(ex)    => complete((InternalServerError, s"An error occurred: ${ex.getMessage}"))
    }
  }

}

