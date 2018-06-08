package com.bitool.analytics.util

import org.apache.log4j.Logger

/**
  * Created by srikanth on 6/6/18.
  */
trait LazyLogging {
  final lazy val logger = Logger.getRootLogger
}
