package spark.learning.scala

object Random extends App{
  def sum(f: Int => Int): (Int, Int) => Int = { 
    def sumF(a: Int, b: Int): Int ={
    if (a > b) 0 else f(a) + sumF(a + 1, b)
    }
    sumF
   }
  println("Sum Int ==="+sum(x=>x)(1,10))
}