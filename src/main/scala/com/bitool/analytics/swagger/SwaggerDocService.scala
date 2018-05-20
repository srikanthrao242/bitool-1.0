package com.bitool.analytics.swagger

import java.util.ServiceLoader

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.bitool.analytics.doc.tasks.TaskService
import com.github.swagger.akka._
import com.github.swagger.akka.model.Info

import scala.collection.JavaConverters._
import scala.reflect.runtime.{universe => ru}

/**
  * Akka Http service providing swagger info
  *
  * @author srikanth
  */
class SwaggerDocService()(implicit system: ActorSystem, actorMaterializer: ActorMaterializer)
  extends SwaggerHttpService
    with HasActorSystem {

  override implicit val actorSystem: ActorSystem = system
  override implicit val materializer: ActorMaterializer = actorMaterializer

  override val apiTypes = Seq(
    ru.typeOf[TaskService]
  )

  override val info = Info(
    title = "Bitool",
    description = "Analytics",
    version = "1.0"
  )

}
