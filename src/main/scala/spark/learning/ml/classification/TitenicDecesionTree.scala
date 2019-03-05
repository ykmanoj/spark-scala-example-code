package spark.learning.ml.classification

import org.apache.spark.sql.SparkSession
import org.apache.spark.ml.feature.StringIndexer
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.classification.DecisionTreeClassifier
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.sql.types.DoubleType

class TitenicDecesionTree {
  def titenicDecesionTree(spark: SparkSession, filePath: String) = {
    val startTime = System.nanoTime()
    //
    val passengers = spark.read.option("header", "true").option("inferSchema", "true").csv(filePath)
    println("Passengers has " + passengers.count() + " rows")
    passengers.show(5)
    passengers.printSchema()
    //
    val passengers1 = passengers.select(passengers("Pclass"), passengers("Survived").cast(DoubleType).as("Survived"), 
        passengers("Gender"), passengers("Age"), passengers("SibSp"), passengers("Parch"), passengers("Fare"))
    passengers1.show(5)
    //
    // VectorAssembler does not support the StringType type. So convert Gender to numeric
    //
    val indexer = new StringIndexer()
    indexer.setInputCol("Gender")
    indexer.setOutputCol("GenderCat")
    val passengers2 = indexer.fit(passengers1).transform(passengers1)
    passengers2.show(5)
    //
    val passengers3 = passengers2.na.drop()
    println("Orig = " + passengers2.count() + " Final = " + passengers3.count() + " Dropped = " + (passengers2.count() - passengers3.count()))
    //
    val assembler = new VectorAssembler()
    assembler.setInputCols(Array("Pclass", "GenderCat", "Age", "SibSp", "Parch", "Fare"))
    assembler.setOutputCol("features")
    val passengers4 = assembler.transform(passengers3)
    passengers4.show(5)
    //
    // split data
    //
    val Array(train, test) = passengers4.randomSplit(Array(0.9, 0.1))
    println("Train = " + train.count() + " Test = " + test.count())
    //
    // Train a DecisionTree model.
    val algTree = new DecisionTreeClassifier()
    algTree.setLabelCol("Survived")
    algTree.setImpurity("gini") // could be "entropy"
    algTree.setMaxBins(32)
    algTree.setMaxDepth(5)
    algTree.setFeaturesCol("features")
    //
    val mdlTree = algTree.fit(train)
    println("The tree has %d nodes.".format(mdlTree.numNodes))
    println(mdlTree.toDebugString)
    println(mdlTree.toString)
    println(mdlTree.featureImportances)
    //
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