package com.spotai
package template

import scala.util.Random

import com.spotai.pattern.state.PatternContext
import com.spotai.Bot

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
