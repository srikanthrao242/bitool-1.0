package com.bitool.analytics.sparkcore

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

/**
  * Created by srikanth on 5/20/18.
  */
trait SparkCoreModule {
  private val warehouseLocation = "file:${system:user.dir}/spark-warehouse"
  private val hiveMetastore = "file:${system:user.dir}/Hive-Warehouse"
  private val rootLogger = Logger.getRootLogger
  rootLogger.setLevel(Level.ERROR)
  implicit lazy val SPARK = SparkSession
    .builder()
    .master("local[4]")
    .appName("BBITOOL")
    .config("spark.sql.warehouse.dir", warehouseLocation)
    //.config("javax.jdo.option.ConnectionURL", s"jdbc:derby:;databaseName=$warehouseLocation;create=true")
    //.config("hive.metastore.warehouse.dir", warehouseLocation)
    //.config("datanucleus.rdbms.datastoreAdapterClassName", "org.datanucleus.store.rdbms.adapter.DerbyAdapter")
    //.config(ConfVars.METASTOREURIS.varname, "")
    //.config("javax.jdo.option.ConnectionDriverName","com.mysql.jdbc.Driver")
    //.config(confvar.varname, confvar.getDefaultExpr())
    //.config("spark.sql.hive.thriftServer.singleSession",true)
    .enableHiveSupport()
    .getOrCreate()
  implicit lazy val SPARK_CONTEXT = SPARK.sparkContext
}
