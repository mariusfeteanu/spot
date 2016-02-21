object Run {
  def main(args:Array[String]):Unit = {
    val bot = Bot("aiml/default.aiml")
    var bye = false

    do{
      val userLine = readLine("q:")
      userLine match {
        case null => bye = true
        case "bye" => bye = true
        case question:String => println("a:"+bot(question))
      }
    } while (!bye)
    println("bye")
  }
}
