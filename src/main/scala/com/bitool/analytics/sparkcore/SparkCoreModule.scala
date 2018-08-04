package com.bitool.analytics.sparkcore

import java.io.File

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

/**
  * Created by srikanth on 5/20/18.
  */
trait SparkCoreModule {
  private val dir = new File("spark-warehouse")
  private val successful = dir.mkdir()
  private var warehouseLocation = new File("spark-warehouse").getAbsolutePath
  if(successful) warehouseLocation = dir.getAbsolutePath
  //private val warehouseLocation = "file:${system:user.dir}/spark-warehouse"
  final implicit lazy val SPARK = SparkSession
                                  .builder()
                                  .master("local[4]")
                                  .appName("BITOOL")
                                  .config("spark.sql.warehouse.dir", warehouseLocation)
                                  .config("spark.sql.sources.maxConcurrentWrites","1")
                                  .config("spark.sql.parquet.compression.codec", "snappy")
                                  .config("hive.exec.dynamic.partition", "true")
                                  .config("hive.exec.dynamic.partition.mode", "nonstrict")
                                  .config("parquet.compression", "SNAPPY")
                                  .config("hive.exec.max.dynamic.partitions", "3000")
                                  .config("parquet.enable.dictionary", "false")
                                  .config("hive.support.concurrency", "true")
                                  .enableHiveSupport()
                                  .getOrCreate()
  final implicit lazy val SPARK_CONTEXT = SPARK.sparkContext
}
