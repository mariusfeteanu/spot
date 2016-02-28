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
import org.scalatest.FlatSpec

import com.spotai.pattern.{Pattern, WildStar, WildUnder, PatternWord}

class PatternSpec extends FlatSpec{
  behavior of "An empty Pattern (from empty list)."
  it must "have no elements" in {
    val pattern = Pattern(Nil)
    assert(pattern.patternElements.size == 0)
  }

  behavior of "An empty Pattern (from empty string)."
  it must "have no elements" in {
    val pattern = new Pattern("")
    assert(pattern.patternElements.size == 0)
  }
  it must "parse * to a list of one WildStar element." in {
    val pattern = new Pattern("*")
    assert(pattern.patternElements.size == 1)
    assert(pattern.patternElements(0) == WildStar())
  }
  it must "parse _ to a list of one WildUnder element." in {
    val pattern = new Pattern("_")
    assert(pattern.patternElements.size == 1)
    assert(pattern.patternElements(0) == WildUnder())
  }
  it must "parse word XYZ to a list of one PatternWord element containing just XYZ." in {
    val pattern = new Pattern("XYZ")
    assert(pattern.patternElements.size == 1)
    assert(pattern.patternElements(0) == PatternWord("XYZ"))
  }
  it must "parse a string of words into a list of identical PatternWords." in {
    val pattern = new Pattern("XX YY ZZ")
    assert(pattern.patternElements.size == 3)
    assert(pattern.patternElements == List(
      PatternWord("XX"),
      PatternWord("YY"),
      PatternWord("ZZ")
    ))
  }
  it must "parse a complex string into a list of identical PatternWords." in {
    val pattern = new Pattern("XX YY * _ ZZ")
    assert(pattern.patternElements.size == 5)
    assert(pattern.patternElements == List(
      PatternWord("XX"),
      PatternWord("YY"),
      WildStar(),
      WildUnder(),
      PatternWord("ZZ")
    ))
  }
}
