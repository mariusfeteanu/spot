case class Template(templateElements:List[TemplateElement]){
  def apply(bot:Bot):String = {
    templateElements.map({
    // For words just say the word
    case templateWord:TemplateWord => templateWord.word
    // For redirects load the respective pattern
    case srai:Srai => srai(bot)
    case placeholder:Placeholder => placeholder match {
      case _:StarPlaceholder => bot.context match {
        case Some(context:PatternContext) => context.star
        case None => ""
      }
      case _ => "(*)"
    }
    case _=> "?"
  }).mkString(" ")
  }
}
