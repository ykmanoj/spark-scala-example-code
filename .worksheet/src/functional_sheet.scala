object functional_sheet {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(69); 
  println("Welcome to the Scala worksheet");$skip(41); 
  val names = List("Fred", "Joe", "Bob");System.out.println("""names  : List[String] = """ + $show(names ));$skip(46); 
  val li = names.map(name => <li>{name}</li>);System.out.println("""li  : List[scala.xml.Elem] = """ + $show(li ));$skip(14); 
  println(li);$skip(55); 
  val inputList: List[Double] = List(1.0, 3.0,5.0,6.0);System.out.println("""inputList  : List[Double] = """ + $show(inputList ));$skip(51); 
  println(inputList.foldLeft(5.0)((acc,i)=>acc+i));$skip(17); val res$0 = 
  inputList.head;System.out.println("""res0: Double = """ + $show(res$0));$skip(17); val res$1 = 
  inputList.tail;System.out.println("""res1: List[Double] = """ + $show(res$1));$skip(33); val res$2 = 
  inputList.head::inputList.tail;System.out.println("""res2: List[Double] = """ + $show(res$2));$skip(209); 
  val test = inputList match {
  case head :: tail => tail.foldLeft((head, 1.0)) { (avg, cur) =>
  println(avg,cur)
    ((avg._1 * avg._2 + cur)/(avg._2 + 1.0), avg._2 + 1.0)
  }._1
  case Nil => Double.NaN
};System.out.println("""test  : Double = """ + $show(test ));$skip(510); 
                                                  
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
	List("Gosling, James", "Joy, Bill", "Steele, Guy", "Bracha, Gilad")));System.out.println("""books  : List[Book] = """ + $show(books ));$skip(111); 
  


val authors = for(b1 <- books;b2 <- books if b1!=b2;
	a1<-b1.authors ; a2<-b2.authors if a1==a2) yield a1;System.out.println("""authors  : List[String] = """ + $show(authors ))}

}
case class Book(title: String, authors: List[String])
