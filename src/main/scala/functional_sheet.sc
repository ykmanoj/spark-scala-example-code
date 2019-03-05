object functional_sheet {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  val names = List("Fred", "Joe", "Bob")          //> names  : List[String] = List(Fred, Joe, Bob)
  val li = names.map(name => <li>{name}</li>)     //> li  : List[scala.xml.Elem] = List(<li>Fred</li>, <li>Joe</li>, <li>Bob</li>)
                                                  //| 
  println(li)                                     //> List(<li>Fred</li>, <li>Joe</li>, <li>Bob</li>)
  val inputList: List[Double] = List(1.0, 3.0,5.0,6.0)
                                                  //> inputList  : List[Double] = List(1.0, 3.0, 5.0, 6.0)
  println(inputList.foldLeft(5.0)((acc,i)=>acc+i))//> 20.0
  inputList.head                                  //> res0: Double = 1.0
  inputList.tail                                  //> res1: List[Double] = List(3.0, 5.0, 6.0)
  inputList.head::inputList.tail                  //> res2: List[Double] = List(1.0, 3.0, 5.0, 6.0)
  val test = inputList match {
  case head :: tail => tail.foldLeft((head, 1.0)) { (avg, cur) =>
  println(avg,cur)
    ((avg._1 * avg._2 + cur)/(avg._2 + 1.0), avg._2 + 1.0)
  }._1
  case Nil => Double.NaN
}                                                 //> ((1.0,1.0),3.0)
                                                  //| ((2.0,2.0),5.0)
                                                  //| ((3.0,3.0),6.0)
                                                  //| test  : Double = 3.75
                                                  
 val books: List[Book] = List(
Book("Structure and Interpretation of Computer Programs",
	List("Abelson, Harold", "Sussman, Gerald J.")),
Book("Principles of Compiler Design",
	List("Aho, Alfred", "Ullman, Jeffrey")),
Book("Programming in Modula-2",
	List("Wirth, Niklaus")),
Book("Introduction to Functional Programming",
	List("Bird, Richard")),
Book("The Java Language Specification",
	List("Gosling, James", "Joy, Bill", "Steele, Guy", "Bracha, Gilad")))
                                                  //> books  : List[Book] = List(Book(Structure and Interpretation of Computer Pr
                                                  //| ograms,List(Abelson, Harold, Sussman, Gerald J.)), Book(Principles of Compi
                                                  //| ler Design,List(Aho, Alfred, Ullman, Jeffrey)), Book(Programming in Modula-
                                                  //| 2,List(Wirth, Niklaus)), Book(Introduction to Functional Programming,List(B
                                                  //| ird, Richard)), Book(The Java Language Specification,List(Gosling, James, J
                                                  //| oy, Bill, Steele, Guy, Bracha, Gilad)))
  


val authors = for(b1 <- books;b2 <- books if b1!=b2;
	a1<-b1.authors ; a2<-b2.authors if a1==a2) yield a1
                                                  //> authors  : List[String] = List()

}
case class Book(title: String, authors: List[String])