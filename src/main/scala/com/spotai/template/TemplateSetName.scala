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
