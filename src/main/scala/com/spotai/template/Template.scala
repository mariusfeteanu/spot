/*
Spot is a bot, implementing a subset of AIML, and some extensions.
Copyright (C) 2016  Marius Feteanu

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.spotai
package template

import com.spotai.pattern.state.PatternContext
import com.spotai.Bot

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
    case templateGetName:TemplateGetName => templateGetName(bot.context)
    case templateStar:TemplateStar => patternContext.star
    case templateRandom:TemplateRandom => templateRandom(bot, patternContext)
    case _ => ???
    }).mkString(" ")
  }
}
