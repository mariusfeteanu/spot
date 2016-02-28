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

import com.spotai.state.BotContext

/*
The <set/> element of a template, will be replaced by its text value,
  and that value will be solved into the current bot context.
*/
class TemplateGetName(name:String) extends TemplateElement{
  def apply(context:BotContext):String = {
    context.predicates.getOrElse(name, "")
  }
}

object TemplateGetName {
  def apply(name:String):TemplateGetName = {
    new TemplateGetName(name)
  }
}
