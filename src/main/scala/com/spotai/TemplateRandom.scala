package com.spotai

import scala.util.Random

class TemplateRandom(elements:List[Template]) extends TemplateElement{
  def apply(bot:Bot, patternContext:PatternContext):String = {
    val rand = new Random()
    elements(rand.nextInt(elements.size))(bot, patternContext)
  }
}

object TemplateRandom {
  def apply(elements:List[Template]):TemplateRandom = {
    new TemplateRandom(elements)
  }
}
