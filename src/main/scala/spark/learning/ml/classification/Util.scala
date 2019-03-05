package spark.learning.ml.classification

import org.apache.spark.SparkContext

object Util {
  def readDataFromText(sparkContext:SparkContext,file:String,header:Boolean, seperator:String)={
    sparkContext.textFile(file, 2)
  }
}