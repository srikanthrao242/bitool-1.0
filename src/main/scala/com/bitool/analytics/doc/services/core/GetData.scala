package com.bitool.analytics.doc.services.core

import com.bitool.analytics.sparkcore.SparkCoreModule
import com.typesafe.config.ConfigFactory
import scala.collection.mutable.ArrayBuffer
import scala.concurrent._
import scala.async.Async.async

/**
  * Created by srikanth on 6/8/18.
  */

class GetData extends SparkCoreModule{

  val config = ConfigFactory.load()

  def getDataSources(implicit ec: ExecutionContext): Future[Map[String,Any]] = async{
    var g_map = Map[String,Any]()
    val metaDB = config.getString("databases.metadb")
    val sqlDF = SPARK.sql("SELECT db_name FROM  "
      +metaDB+".database_info ")
    var map = Map[String,Any]()
    import SPARK.sqlContext.implicits._
    var result = sqlDF.columns.foreach(col => {
      map += (col ->
        sqlDF.groupBy(col)
          .count()
          .map(_.getString(0))
          .collect())
    })
    g_map += ("data" -> map)
    g_map += ("msg" -> "Success")
    g_map
  }
  def getCategories(datasource:String): Future[Map[String,Any]] = async{
    var g_map = Map[String,Any]()
    val metaDB = config.getString("databases.metadb")
    val sqlDFString = SPARK.sql("SELECT category FROM  "
      +metaDB+".stringpredicates where db_name='"+datasource+"'");
    val sqlDFNumeric = SPARK.sql("SELECT category FROM  "
      +metaDB+".numericpredicates where db_name='"+datasource+"'");
    val sqlDFtimeStamp = SPARK.sql("SELECT category FROM  "
      +metaDB+".timestamppredicates where db_name='"+datasource+"'");
    val sqlDFdate = SPARK.sql("SELECT category FROM  "
      +metaDB+".datepredicates where db_name='"+datasource+"'");
    val sqlDFBoolean = SPARK.sql("SELECT category FROM  "
      +metaDB+".booleanpredicates where db_name='"+datasource+"'");
    val sqlDFbinary = SPARK.sql("SELECT category FROM  "
      +metaDB+".binarypredicates where db_name='"+datasource+"'");
    var map = Map[String,Any]()
    import SPARK.sqlContext.implicits._
    sqlDFString.columns.foreach(col => {
      map += ("string" ->
        sqlDFString.groupBy(col)
          .count()
          .map(_.getString(0))
          .collect())
    })
    sqlDFNumeric.columns.foreach(col => {
      map += ("numerics" ->
        sqlDFNumeric.groupBy(col)
          .count()
          .map(_.getString(0))
          .collect())
    })
    sqlDFtimeStamp.columns.foreach(col => {
      map +=("timestamp" ->
        sqlDFtimeStamp.groupBy(col)
          .count()
          .map(_.getString(0))
          .collect())
    })
    sqlDFdate.columns.foreach(col => {
      map += ("date" ->
        sqlDFdate.groupBy(col)
          .count()
          .map(_.getString(0))
          .collect())
    })
    sqlDFBoolean.columns.foreach(col => {
      map += ("boolean" ->
        sqlDFBoolean.groupBy(col)
          .count()
          .map(_.getString(0))
          .collect())
    })
    sqlDFbinary.columns.foreach(col => {
      map += ("binary" ->
        sqlDFbinary.groupBy(col)
          .count()
          .map(_.getString(0))
          .collect())
    })
    g_map += ("data"-> map)
    g_map += ("msg"-> "Success")
    g_map
  }
  def getTableData(map:Map[String,Array[Map[String,Any]]],dsName:String)(implicit ec: ExecutionContext):Future[Map[String,Any]]=async{
    println("getTableData invoked")
    val mainDB = config.getString("databases.maindb")
    var g_Map = Map[String,Any]()
    var queryString = "SELECT * FROM "+mainDB+"."+dsName+" WHERE "
    var condition = new ArrayBuffer[String]()
    var whereString = ""
    map.map(cat=>{
      val catMap = cat._2
      if(cat._1.equals("numeric")|| cat._1.equals("date") || cat._1.equals("timestamp")){
        catMap.foreach(sCatName=>{
          val sCatMap = sCatName
          sCatMap.foreach(sCat =>{
            var arr = sCat._2.asInstanceOf[Array[Any]]
            whereString = whereString+sCat._1+">="+arr(0).toString+" AND "+sCat._1+"<="+arr(1).toString+" "
          })
        })
        condition.+=(whereString)
      }else{
        catMap.map(sCatName=>{
          var sCatMap = sCatName
          sCatMap.map(sCat =>{
            var arr = sCat._2.asInstanceOf[Array[Any]]
            var i =0 ;
            var whereString = ""
            println(arr.length)
            if(arr.length == 1){
              whereString = whereString+sCat._1+"='"+arr(i)+"'"
            }else{
              while(i<arr.length){
                if(i == arr.length-1){
                  whereString = whereString+"'"+arr(i)+"' ) "
                }else if(i == 0){
                  whereString = whereString+sCat._1+" IN ( '"+arr(i)+"', "
                }else{
                  whereString = whereString+"'"+arr(i)+"',"
                }
                i = i+1
              }
            }
            condition.+=(whereString)
          })
        })
      }
    })
    var i = 0
    while(i<condition.length){
      if(i == condition.length-1){
        queryString = queryString+condition(i)
      }else{
        queryString = queryString+condition(i)+" AND "
      }
      i=i+1
    }
    println("*******************************************************")
    println(queryString)
    println("*******************************************************************")
    val dsDf = SPARK.sql(queryString)
    val hashMap = Map[String,Any]()
    val colNames = dsDf.columns
    //      import scala.reflect.ClassTag
    //      implicit def kryoEncoder[A](implicit ct: ClassTag[A]) = org.apache.spark.sql.Encoders.kryo[A](ct)
    //      dsDf.columns.map(col => {
    //         hashMap.put(col ,
    //         dsDf.groupBy(col)
    //           .count()
    //           .map(_.get(0))
    //           .collect())
    //         })
    //println(write(hashMap.asScala))

    g_Map += ("msg" -> "success")
    g_Map += ("data" -> dsDf.rdd.collect())
    g_Map += ("colNames" -> colNames)

    g_Map
  }
}
