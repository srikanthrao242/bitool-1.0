package com.bitool.analytics

import com.bitool.analytics.akkacore.AkkaCoreModule
import com.bitool.analytics.doc.tasks.TasksModule
import com.bitool.analytics.sparkcore.SparkCoreModule

/**
  * Created by srikanth on 5/19/18.
  */
object RestEndpoint extends App
                    with AkkaCoreModule
                    with SparkCoreModule
                    with WebServer
                    with RootRoutes
                    with TasksModule
