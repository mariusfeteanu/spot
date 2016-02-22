package com.spotai

/*
A template for the bots response.
*/
case class Template(templateElements:List[TemplateElement]){
  def apply(bot:Bot):String = {
    templateElements.map({
    // For words just say the word
    case templateWord:TemplateWord => templateWord.word
    // For redirects load the respective pattern
    case srai:Srai => srai(bot)
    case templateStar:TemplateStar => bot.context match {
        case Some(context:PatternContext) => context.star
        case None => ""
      }
    case _ => ???
  }).mkString(" ")
  }
}
