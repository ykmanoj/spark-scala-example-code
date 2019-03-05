package spark.learning.scala

import org.apache.spark.SparkContext
//import org.eclipse.jetty.client.ContentExchange
import org.eclipse.jetty.client.HttpClient

object BasicmapPartition {
    def main(args: Array[String]) {
      val master = args.length match {
        case x: Int if x > 0 => args(0)
        case _ => "local"
      }
      val sc = new SparkContext(master, "BasicMapPartitions", System.getenv("SPARK_HOME"))
      val input = sc.parallelize(List("KK6JKQ", "Ve3UoW", "kk6jlk", "W6BB"))
      
    }
}