package spark.learning.ml.classification

import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.SparkSession

class DecesionTreeAlgo(inputLoc: String, outputLoc: String, modelLoc: String, fileType: String, delimeter: String) extends BasicClassfication {
  def getDataFrame() = {
    val dataFrame = fileType match {
      case "csv" => {
        DecesionTreeAlgo.spark.read.csv(inputLoc)
      }
      case "json" => {
        DecesionTreeAlgo.spark.read.json(inputLoc)
      }
      case _ => {
        val records = DecesionTreeAlgo.sparkContext.textFile(inputLoc, 1)
        val firstRow = records.first
        createDataFrame(DecesionTreeAlgo.sqlContext, records, firstRow.mkString("|"), delimeter)
      }
    }
    dataFrame
  }
  //val categoryFeatures = ""

def decesionTree(args: Array[String]): Unit = {
    val inputLoc = "file:///usr/local/datasets/loan.csv"
    val modelLoc = "file:///usr/local/machine-learning/model/loan-model"
    val outputLoc = "file:///usr/local/machine-learning/model/output/loan/prediction.txt"
    val loanDataDecesion = new DecesionTreeAlgo(inputLoc, outputLoc, modelLoc, "csv", ",")
    val loanData = loanDataDecesion.getDataFrame()
    loanData.show(1)

  }
}
object DecesionTreeAlgo {

  val spark: SparkSession = SparkSession.builder.getOrCreate()
  val sparkContext: SparkContext = SparkContext.getOrCreate()
  val sqlContext: SQLContext = new SQLContext(sparkContext)
}