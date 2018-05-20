package com.bitool.analytics.doc.tasks

import com.bitool.analytics.RestEndpoint

/**
  * Created by srikanth on 5/21/18.
  */
class TaskHandlerProviders extends TaskHandlerSPI{
  override def getTaskHandlers: Seq[TaskHandler] =
    Seq(

        RestEndpoint.createTable


    )
}
