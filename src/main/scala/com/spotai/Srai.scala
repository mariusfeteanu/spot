package com.spotai

/*
A srai recursive template element, will be produced by asking the bot to evaluate it's contents
as a question
*/
class Srai(templateElements:List[TemplateElement]) extends Template(templateElements) with TemplateElement{
  override def apply(bot:Bot, patternContext:PatternContext):String = {
    // Ask the current bot, using the as the current question
    // with the srai interpreted as a a template for a question
    bot(super.apply(bot, patternContext))
  }
}
