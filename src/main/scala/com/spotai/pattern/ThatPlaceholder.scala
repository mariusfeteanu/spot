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

/*
The _ wildcar pattern element.
*/

class ThatPlaceholder extends PatternElement {
  override def equals(that:Any) = {
    that match {
      case _:ThatPlaceholder => true
      case _ => false
    }
  }
  override def hashCode = 0
  override def toString() = "_"
}

object ThatPlaceholder {
  def apply() = {
    new ThatPlaceholder()
  }
}
