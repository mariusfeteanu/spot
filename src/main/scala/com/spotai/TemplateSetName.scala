package com.spotai

/*
The <set/> element of a template, will be replaced by its text value,
  and that value will be solved into the current bot context.
*/
class TemplateSetName(name:String, templateElements:List[TemplateElement]) extends Template(templateElements) with TemplateElement{
  override def apply(bot:Bot, patternContext:PatternContext):String = {
    // Ask the current bots
    val response = super.apply(bot, patternContext)
    bot.context.predicates = (bot.context.predicates - name) + (name -> response)
    response
  }
}

object TemplateSetName {
  def apply(name:String, templateElements:List[TemplateElement]):TemplateSetName = {
    new TemplateSetName(name, templateElements)
  }
}
