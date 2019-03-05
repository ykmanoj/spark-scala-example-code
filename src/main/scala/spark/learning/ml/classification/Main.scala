package spark.learning.ml.classification

import org.apache.spark.sql.SparkSession
import org.apache.log4j.Logger
import org.apache.log4j.Level



object Main {
  
 def main(args: Array[String]): Unit = {
    //System.setProperty("hadoop.home.dir", "D:\\StartUp\\hadoop-common-2.2.0-bin-master");
    Logger.getLogger("org").setLevel(Level.OFF)
    Logger.getLogger("akka").setLevel(Level.OFF)

    callExecutor("TitenicRandomForest")
    //callExecutor("DecesionTreeAlgo")     
    
  }
 
 def callExecutor(classname : String) = classname match {
   case "DecesionTreeAlgo" => {
    // val dct:DecesionTreeAlgo = new DecesionTreeAlgo
    // val inputArgs = Array("file:///D:/StartUp/example.spark.ml//src//main//resources//data//housing//Housing.csv")
    // dct.decesionTree(inputArgs);
   }
   case "RandomForestExample" => {
     val algo:RandomForestExample = new RandomForestExample
     val inputArgs = Array("file:///usr/local/datasets/cancer.json")
     algo.randomForestCancerJsonData(inputArgs);
   }
   
   case "TitenicDecesionTree" => {
     val filePath = "file:///usr/local/datasets/titanic3_02.csv"
     val spark = SparkSession.builder.master("local").appName("TitenicClassification").getOrCreate()
     val titenic: TitenicDecesionTree = new TitenicDecesionTree
     titenic.titenicDecesionTree(spark, filePath)

   }
   case "TitenicRandomForest" => {
     val filePath = "file:///usr/local/datasets/titanic3_02.csv"
     val spark = SparkSession.builder.master("local").appName("TitenicClassification").config("spark.logLineage", true).config("log4j.rootCategory","WARN, console").getOrCreate()
     val titenic: TitenicRandomForest = new TitenicRandomForest
     titenic.execute(spark, filePath)

   }
 }
  
}