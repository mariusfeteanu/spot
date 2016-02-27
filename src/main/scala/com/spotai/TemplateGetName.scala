package com.spotai

/*
The <set/> element of a template, will be replaced by its text value,
  and that value will be solved into the current bot context.
*/
class TemplateGetName(name:String) extends TemplateElement{
  def apply(bot:Bot):String = {
    bot.context.predicates.getOrElse(name, "")
  }
}

object TemplateGetName {
  def apply(name:String):TemplateGetName = {
    new TemplateGetName(name)
  }
}
