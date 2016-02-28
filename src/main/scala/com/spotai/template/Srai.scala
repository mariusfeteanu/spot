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

object Srai {
  def apply(templateElements:List[TemplateElement]):Srai = {
    new Srai(templateElements)
  }
}
