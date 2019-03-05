package spark.learning.ml.learning

import org.apache.spark.ml.feature.NGram
import org.apache.spark.sql.internal._
import org.apache.spark.sql.SparkSession
import org.apache.spark.ml.feature.RegexTokenizer
import org.apache.spark.ml.feature.StringIndexer

object PracticeCode {

  val spark: SparkSession = SparkSession.builder().master("local").getOrCreate()
  val sqlContext = spark.sqlContext
  import spark.implicits._
  import spark.sql
  def main(args: Array[String]): Unit = {
oneHotEncoderEx
  }

  def ngrramEx = {
    val regexTok = new RegexTokenizer()
    val bigram = new NGram("bigrams")
    val df = Seq((0, "hello test word india"), (1, "manoj india cricket world")).toDF("id", "sentence")
    val tokenized = regexTok.setInputCol("sentence").setOutputCol("tokenized").transform(df)
    bigram.setInputCol("tokenized").transform(tokenized).show
  }

  def oneHotEncoderEx() = {
    val df = Seq(
      (0, "a"), (1, "b"),
      (2, "c"), (3, "a"),
      (4, "a"), (5, "c"),
      (6,"d"),(7,"d"),
      (6,"b"),(7,"b"))
      .toDF("label", "category")
    val indexer = new StringIndexer().setInputCol("category").setOutputCol("cat_index").fit(df)
    val indexed = indexer.transform(df)
    import org.apache.spark.sql.types.NumericType
    indexed.schema("cat_index").dataType.isInstanceOf[NumericType]
    import org.apache.spark.ml.feature.OneHotEncoder
    val oneHot = new OneHotEncoder()
      .setInputCol("cat_index")
      .setOutputCol("cat_vec")
    val oneHotted = oneHot.transform(indexed)
    oneHotted.foreach { println(_) }

  }
}