package spark.learning.ml.classification

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{ StructType, StructField, StringType };
import org.apache.spark.sql.internal._
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.Row

trait BasicClassfication {

  def createDataFrame(sqlContext: SQLContext,records:RDD[String],features:String, delimeter: String) = {
    val schema =
      StructType(
        features.split("|").map(fieldName => StructField(fieldName, StringType, true)))
        
    val rowRDD = records.map(row => Row(row.split(delimeter)))
    sqlContext.createDataFrame(rowRDD, schema)

  }

}