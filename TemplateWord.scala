case class TemplateWord(word:String) extends TemplateElement
class Srai(templateElements:List[TemplateElement]) extends Template(templateElements) with TemplateElement{
  override def apply(bot:Bot):String = {
    // Ask the current bot, using the as the current question
    // with the srai interpreted as a a template for a question
    bot(super.apply(bot))
  }
}
