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

import org.scalatest._

class PatternWordSpec extends FlatSpec with Matchers {
  behavior of "A PatternWord."
  it must "be equal to another if created from the same String" in {
    PatternWord("XYZ") shouldBe PatternWord("XYZ")
  }
  it must "be different from another if created from different String" in {
    PatternWord("XYZ") should not be PatternWord("ZYX")
  }
  it must "have a content equal to the string it was created from" in {
    PatternWord("XYZ").word shouldBe "XYZ"
  }
}
