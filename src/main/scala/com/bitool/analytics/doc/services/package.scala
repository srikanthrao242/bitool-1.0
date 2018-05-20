package com.bitool.analytics.doc

import com.bitool.analytics.doc.services.ScenarioArguments

/**
  * Created by srikanth on 5/20/18.
  */
package object RequestArgs {
  case class CREATE_TABLE(path : String,tableName:String,delimiter:String) extends ScenarioArguments
}
