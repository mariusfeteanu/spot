package com.spotai

/*
A template for the bots response.
*/
case class Template(templateElements:List[TemplateElement]){
  def apply(bot:Bot, patternContext:PatternContext):String = {
    templateElements.map({
    // For words just say the word
    case templateWord:TemplateWord => templateWord.word
    // For redirects load the respective pattern
    case srai:Srai => srai(bot, patternContext)
    case templateSetName:TemplateSetName => templateSetName(bot, patternContext)
    case templateGetName:TemplateGetName => templateGetName(bot)
    case templateStar:TemplateStar => patternContext.star
    case _ => ???
  }).mkString(" ")
  }
}
