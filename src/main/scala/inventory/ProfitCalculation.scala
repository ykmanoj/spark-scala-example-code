package inventory

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.DateType
import org.apache.spark.sql.functions.unix_timestamp
import java.text.SimpleDateFormat
import java.sql.Timestamp
import java.util.Date


object ProfitCalculation {
 
/*
1, Buy, 10, 100.0, Jan 1
2, Buy, 20, 200.0, Jan 2
1, Buy, 15, 150.0, Jan 3
1, Sell, 5, 120.0, Jan 5
1, Sell, 10, 125.0, Jan 6 
*/  

case class Inventory(PRODUCT_ID:Int, TRANSACTION_TYPE:String, QUANTITY:Int, PRICE:Double, monthDate:String)  
  
def main(args: Array[String]): Unit = {
  
  val spark: SparkSession = SparkSession.builder().master("local").appName("ProfitCalculation").getOrCreate()
  spark.sparkContext.setLogLevel("ERROR")
  import spark.implicits._
  val rawInventoryData = spark.read.option("header", "true").textFile("file:///Users/manoj/work/workspace/scala/spark.scala.learning/src/main/resources/inventory.csv")
  rawInventoryData.map( x => x.split(",")(4)).show()
  val iDF = rawInventoryData.map(x => x.split(",")).map { x => 
     val format = new SimpleDateFormat("MMM dd")
     Inventory(x(0).trim.toInt,x(1).trim,x(2).trim.toInt,x(3).trim.toDouble,format.parse(x(4).trim).getTime.toString()) }.toDF().as[Inventory]
  
  iDF.show
  val buyerGR = iDF.filter(_.TRANSACTION_TYPE == "Buy")
  val sellerGR = iDF.filter(_.TRANSACTION_TYPE == "Sell")
  val rdd = iDF.rdd.map { x => ((x.PRODUCT_ID),(x.TRANSACTION_TYPE,x.QUANTITY,x.PRICE,new Timestamp(x.monthDate.toLong).getDate)) }
  val groupRDD = rdd.groupByKey()
  iDF.createOrReplaceTempView("Inventory")

  //val profitByDate= rdd.combineByKey(createCombiner, mergeValue, mergeCombiners)

}

def reduceProfit(x:(String, Int, Double, String),y:(String, Int, Double, String)):(String, Int, Double, String) = {
  val totalprice=0
  val quantity=1
  
  ("Sell", 1, 2.0, "d")
  
}

def combineProfit() = {
  
}
def groupProfit() = {
  
}
  
}