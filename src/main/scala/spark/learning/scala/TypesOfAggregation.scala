package spark.learning.scala

import org.apache.spark.sql.SparkSession

object TypesOfAggregation {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName("Broadcast Test")
      //.config("spark.broadcast.blockSize", blockSize)
      .master("local")
      .getOrCreate()

    val words = Array("one one one one", "two two two rwo", "three", "three three three", "four four four")

    val sc = spark.sparkContext
    sc.setLogLevel("ERROR")
    val wordRDD = sc.parallelize(words).map(word => (word, 1))
        
    val wordMapRDD = sc.parallelize(words).map(word => word.split(" "))// (word, 1))
    
    wordMapRDD.collect()//(println)
    val wordFlatRDD = sc.parallelize(words).flatMap(word => word.split(" "))//(word, 1))
    
    //wordFlatRDD.map((_,1)).collect()//foreach(println)

    val wordCountsreducebykey = wordRDD
      .reduceByKey(_ + _)
      .collect()

    val wordCountsGroupbykey = wordRDD
      .groupByKey()
      .map(t => (t._1, t._2.sum))
      .collect()

    val workCountsaggregatebykey = wordRDD.aggregateByKey(1)((k, v) => v.toInt + k, (v, k) => k + v).collect.foreach(println)

    val rdd = sc.parallelize(List(
      ("A", 3), ("A", 9), ("A", 12), ("A", 0), ("A", 5), ("B", 4),
      ("B", 10), ("B", 11), ("B", 20), ("B", 25), ("C", 32), ("C", 91),
      ("C", 122), ("C", 3), ("C", 55)), 2)

    val workCountcombinebykey = rdd.combineByKey((x: Int) => (x, 1), (acc: (Int, Int), x) => (acc._1 + x, acc._2 + 1), (acc1: (Int, Int), acc2: (Int, Int)) => (acc1._1 + acc2._1, acc1._2 + acc2._2)).collect().foreach(println)

  }
}