package com.bitool.analytics.doc.tasks

import com.bitool.analytics.akkacore.AkkaCoreModule
import com.bitool.analytics.doc.RequestArgs.CREATE_TABLE
import com.softwaremill.macwire._
import com.bitool.analytics.doc.services.handlers.CreateTableHandler
import io.circe.generic.auto._
/**
  * Created by srikanth on 5/20/18.
  */
trait TasksModule {
  this:AkkaCoreModule=>

  lazy val createTable = wire[CreateTableHandler[CREATE_TABLE]]




}
