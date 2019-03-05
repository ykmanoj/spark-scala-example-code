package spark.learning.ml.classification

import org.apache.spark.sql.SparkSession
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.feature.StringIndexer
import org.apache.spark.sql.functions
import org.apache.spark.ml.classification.RandomForestClassifier
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator

class RandomForestExample {
  var inputfile: String = "test"
  def randomForestCancerJsonData(args: Array[String]): Unit = {
    if (args.length > 0) {
      inputfile = args(0)
    }

    val spark: SparkSession = SparkSession.builder().master("local").getOrCreate()
    val sqlContext = spark.sqlContext
    import spark.implicits._
    val cancerDF = sqlContext.read.json(inputfile)
    cancerDF.printSchema()
    //println(cancerDF.first())
    //sqlContext.createOrReplaceTempView("cancer")
    val cancerDFNullFilter = cancerDF.filter("bareNuclei is not null")
    //val oneVal = cancerDFNullFilter.filter(test=> test(2)==4.0).take(20)

   /*Prepare the data for classification
We need to do two things to prepare our data for the random forest classifier

Create a column that is a vector of all the features (predictor values)
Transform the class field to an index—it needs to contain a few discrete values
First, we create a “feature” column of all the predictor values.*/

    val assembler = new VectorAssembler().setInputCols(Array("bareNuclei",
      "blandChromatin", "clumpThickness", "marginalAdhesion", "mitoses",
      "normalNucleoli", "singleEpithelialCellSize", "uniformityOfCellShape",
      "uniformityOfCellSize")).setOutputCol("features")

    val assemblerData = assembler.transform(cancerDFNullFilter)
    //Next, we create discrete target values (labels) from the class field.

    val labelIndexer = new StringIndexer().setInputCol("class").setOutputCol("label")
    
    val df4 = labelIndexer.fit(assemblerData).transform(assemblerData)
    val splitSeed = 5043
    val Array(trainingData, testData) = df4.randomSplit(Array(0.7, 0.3), splitSeed)
    //val filterTest = testData.filter(test=> test(2)==4.0)
    /*70% of the data is used to training the model. The remaining 30% is held back for testing.

Now let’s train a random forest classifier that has the following hyper-parameter values

Gini impurity
A maximum tree depth of 3
20 trees in the forest
Automatically selects the number of features to consider for splits at each tree node
Uses a random number seed of 5043, allowing us to repeat the results
We create the classifier and then use it to train (fit) the model.*/

    val classifier = new RandomForestClassifier()
      .setImpurity("gini")
      .setMaxDepth(3)
      .setNumTrees(20)
      .setFeatureSubsetStrategy("auto")
      .setSeed(5043)
      
    val model = classifier.fit(trainingData)

    /*Predicting diagnoses using the test data
We can now ask the model to predict diagnoses for the test samples.
*/
    val predictions = model.transform(testData)
    //Let’s examine the first 5 predictions.

    predictions.select("sampleCodeNumber", "label", "prediction").show(100)

    /*Evaluate the quality of the model
Spark provides us with tools to evaluate the accuracy of our model. Let’s generate the “precision” metric by comparing the label column with the prediction column.
*/
    //Thread.sleep(100000)
    val evaluator = new MulticlassClassificationEvaluator()
      .setLabelCol("label")
      .setPredictionCol("prediction")
      .setMetricName("weightedPrecision")
    val accuracy = evaluator.evaluate(predictions)
    println(evaluator.evaluate(predictions))
    
    spark.stop()
  }
}