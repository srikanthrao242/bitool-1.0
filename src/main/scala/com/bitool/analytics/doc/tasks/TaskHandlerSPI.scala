package com.bitool.analytics.doc.tasks

/**
  * A Service Provider Interface (SPI) that should be implemented by the API and specified in META-INF/services
  * to provide the task handlers at runtime.
  *
  * @see [[java.util.ServiceLoader]]
  * @author srikanth
  */
trait TaskHandlerSPI {

  /**
    * @return The list of task handlers that should be register ed in the [[TaskService]]
    */
  def getTaskHandlers: Seq[TaskHandler]
}
