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
Any element of a pattern, extend this to create new ones.
*/
class PatternElement extends Ordered[PatternElement] {

  /*
  Result of comparing this with operand that.
  returns x where
    x < 0 iff this < that
    x == 0 iff this == that
    x > 0 iff this > that
  */
  // Turning off cyclomatic complexity checking here because the code is crystal clear
  // and any refactoring will make it more unclear
  // scalastyle:off cyclomatic.complexity
  override def compare(that:PatternElement) = {
    this match {
      case _:WildUnder => that match {
        case _:WildUnder => 0
        case _:PatternWord => -1
        case _:WildStar => -1
      }
      case thisw:PatternWord => that match {
        case _:WildUnder => 1
        case thatw:PatternWord => if (thisw.word<thatw.word) -1 else if (thisw.word==thatw.word) 0 else 1
        case _:WildStar => -1
      }
      case _:WildStar => that match {
        case _:WildUnder => 1
        case _:PatternWord => 1
        case _:WildStar => 0
      }
    }
  }
  // scalastyle:on
}
