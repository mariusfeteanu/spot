object Run {
  def main(args:Array[String]):Unit = {
    val bot = Bot("aiml/default.aiml")
    println(bot("activate shields"))
  }
}
