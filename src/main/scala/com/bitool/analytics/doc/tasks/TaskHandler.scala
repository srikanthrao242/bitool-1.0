package com.bitool.analytics.doc.tasks

import akka.http.scaladsl.server.Route
import io.circe.Json

/**
  * Base trait for a handler capable of handling incoming tasks to the [[TaskService]], differentiated by
  * the taskType that is in the Json
  *
  * @author srikanth
  */
trait TaskHandler {
  def taskType: String
  def handleJson(json: Json): Route
}
