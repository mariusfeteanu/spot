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
package pattern
package state

/*
The context in which a pattern was matched.
*/
case class PatternContext(
  // The value of the matched star wildcard
  star:String,
  inputDone:Boolean
){
  def this(star:String) = this(star, false)

  def withStar(star:String) = {
    star match {
      case _ if this.star != "" && inputDone => this
      case _ if this.star != "" && !inputDone => PatternContext(this.star + " " + star, this.inputDone)
      case _ => PatternContext(star, this.inputDone)
    }
  }
}

object PatternContext {
  def apply(star:String):PatternContext = {
    new PatternContext(star)
  }
}
