package spark.learning.scala

import org.apache.spark.SparkContext

object AccumulatorExample {
  def main(args: Array[String]) {
    val master_inputFile:(String,String) = args.length match {
      case 1 => (args(0),"test.txt")
      case 2 => (args(0),args(1))
      case _ => ("local","test.txt")
    }
    
      val sc = new SparkContext(master_inputFile._1, "BasicLoadNums", System.getenv("SPARK_HOME"))
      val file = sc.textFile(master_inputFile._2)
      val errorLines = sc.accumulator(0.0)  // Create an Accumulator[Int] initialized to 0
      val dataLines = sc.accumulator(0.0)  // Create a second Accumulator[Int] initialized to 0
      val lines = file.flatMap { line => line.split("\t") }.collect()
      val counts = file.flatMap(line => {
        try {
          val input = line.split("\t")
          println(input)
          val data = Some((input(0), input(1).toInt))
          dataLines += 1
          data
        } catch {
          case e: java.lang.NumberFormatException => {
            errorLines += 1
            None
          }
          case e: java.lang.ArrayIndexOutOfBoundsException => {
            errorLines += 1
            None
          }
        }
      }).reduceByKey(_ + _)
      if (errorLines.value < 0.1 * dataLines.value) {
        counts.saveAsTextFile("output.txt")
      } else {
        println(s"Too many errors ${errorLines.value} for ${dataLines.value}")
      }
      
      println(lines.mkString("#"))
    }
}