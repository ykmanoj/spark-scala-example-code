package practice

import org.apache.spark.sql.SparkSession
import scala.collection.immutable.Set

object Hadooptest3 {
  
  case class Employee(empid:String, salary:Double,
age:Int, state:String)
  case class Result(Salary:Double, frequency:Long,
age_freq:Int, state_freq:Int)

  def main(args: Array[String]): Unit = {
    var input = "file:///D:/StartUp/example.spark.ml//src//main//resources//data//employee.txt"
    System.setProperty("hadoop.home.dir", "D:\\StartUp\\hadoop-common-2.2.0-bin-master");
    val spark: SparkSession = SparkSession.builder().master("local").getOrCreate()
    spark.sparkContext.setLogLevel("ERROR")

    import spark.implicits._
    import org.apache.spark.sql.functions._
    val employeeRaw = spark.read.textFile(input).rdd
    val emp=employeeRaw.map { x =>  x.split("\t")}.map { x => Employee(x(0),x(1).trim.toDouble,x(2).trim().toInt,x(3)) }.toDS()
    val empFreq = emp.groupBy("salary").agg(count("empid").alias("frequency"), collect_set("age").alias("ageSet"),collect_set("state").alias("stateSet")).rdd
    val result =empFreq.map { x => Result(x(0).asInstanceOf[Double],x(1).toString().toInt.asInstanceOf[Int],x(2).asInstanceOf[Seq[Int]].length.toInt, x(3).asInstanceOf[Seq[Int]].length.asInstanceOf[Int]) }.toDS()
    result.show()
    spark.stop()
}
}
