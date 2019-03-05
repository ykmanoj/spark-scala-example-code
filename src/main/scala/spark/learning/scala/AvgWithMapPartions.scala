package spark.learning.scala

import org.apache.spark.SparkContext

object AvgWithMapPartions {
  case class AvgValue(var total:Int =0,var num:Int=0){
    def merge(input:Iterator[Int]):AvgValue={
      input.foreach{
        elem =>
          total += elem 
          num +=1
        }
      this
    }
    def merge(second: AvgValue):AvgValue={
      total+=second.total
      num+=second.num
      this
    }
    def avg():Float = total/num.toFloat
    
  }
  
  def main(args:Array[String]){
    val master = args.length match {
      case x:Int if x>0 => args(0)
      case _ => "local"
    }
    val sc = new SparkContext(master,"AvgWithMapPartions",System.getenv("SPARK_HOME"))
    val input = sc.parallelize(List(1,2,3,4,5,6,7,8,9), 3)
    val result = input.mapPartitions(partition => 
      Iterator(AvgValue(0, 0).merge(partition)))
      //.reduce((x,y) => x.merge(y))
      //println(result.total)

      result.foreach{println}
      //(x.total+y.total,x.num+y.num)
    
  }
}