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

import com.spotai.pattern.Pattern
import com.spotai.template.Template

/*
Represents an AIML category.
*/
case class Category(stimulus:Pattern, response:Template, that:Option[Pattern], topic:Option[Pattern]) extends Ordered[Category] {

    /*
    Result of comparing this with operand that.
    returns x where
      x < 0 iff this < that
      x == 0 iff this == that
      x > 0 iff this > that
    */
    override def compare(that:Category) = {
      this.that match {
        case None => that.that match {
          case None => this.stimulus.compare(that.stimulus)
          case Some(that_that:Pattern) => 1
        }
        case Some(this_that:Pattern) => that.that match {
          case None => -1
          case Some(that_that:Pattern) => this_that.compare(that_that) //needs moar thats?
        }
      }
    }
}
