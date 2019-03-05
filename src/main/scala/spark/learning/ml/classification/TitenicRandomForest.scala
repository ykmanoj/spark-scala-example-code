package spark.learning.ml.classification

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.DoubleType
import org.apache.spark.ml.feature.StringIndexer
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.classification.RandomForestClassificationModel
import org.apache.spark.ml.classification.RandomForestClassifier
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator

class TitenicRandomForest {
  def execute(spark:SparkSession,filePath:String) = {
    import spark.implicits._
    val startTime = System.nanoTime()
		//
		val passengers = spark.read.option("header","true").
		  option("inferSchema","true").csv(filePath)
    println("Passengers has "+passengers.count()+" rows")
    passengers.show(5)
    passengers.printSchema()
    //
    val passengers1 = passengers.select(passengers("Pclass"),passengers("Survived").cast(DoubleType).as("Survived"),
        passengers("Gender"),passengers("Age"),passengers("SibSp"),passengers("Parch"),passengers("Fare"))
    passengers1.show(5)
    
    //convert String type using StringIndexer
    val indexer = new StringIndexer().setInputCol("Gender").setOutputCol("Gender_Cat")
    val indexPassengers = indexer.fit(passengers1).transform(passengers1)
    
    val passengers3 = indexPassengers.na.drop()
    println("Orig = " + indexPassengers.count() + " Final = " + passengers3.count() + " Dropped = " + (indexPassengers.count() - passengers3.count()))

    //Create Vector Assembler
    val assembler = new VectorAssembler().setInputCols(Array("Pclass", "Gender_Cat", "Age", "SibSp", "Parch", "Fare")).
    setOutputCol("features")
    val passengerAssebler = assembler.transform(passengers3)
    passengerAssebler.show(5)
    val Array(train,test) = passengerAssebler.randomSplit(Array(0.9,0.2))
    println("Train = " + train.count() + " Test = " + test.count())
    //Train in RandomForest
    val rfTitenic = new RandomForestClassifier()
    .setLabelCol("Survived")
    .setMaxDepth(10)
    .setMaxBins(40)
    .setFeaturesCol("features")
    .setImpurity("entropy")
    
    val mdlTree = rfTitenic.fit(train)
    //println("The tree has %d nodes.".format(mdlTree.numTrees))
    println(mdlTree.toDebugString)
    println(mdlTree.toString)
    println(mdlTree.featureImportances)
    
    
     // predict test set and calculate accuracy
    //
    val predictions = mdlTree.transform(test)
    predictions.show(5)
    //
    val evaluator = new MulticlassClassificationEvaluator()
    evaluator.setLabelCol("Survived")
    evaluator.setMetricName("accuracy") // could be f1, "weightedPrecision" or "weightedRecall"
    //
    val accuracy = evaluator.evaluate(predictions)
    println("Test Accuracy = %.2f%%".format(accuracy * 100))
    //
    val elapsedTime = (System.nanoTime() - startTime) / 1e9
    println("Elapsed time: %.2f seconds".format(elapsedTime))
  
  }
  
  
}