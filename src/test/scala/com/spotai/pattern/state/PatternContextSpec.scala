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

import org.scalatest._
import org.scalatest.prop.TableDrivenPropertyChecks._

class PatternContextSpec extends FlatSpec with Matchers {
  behavior of "A PatternContext."
  it must "be equal to another if created from the same String" in {
    PatternContext("XYZ") shouldBe PatternContext("XYZ")
  }
  it must "be different from another if created from different String" in {
    PatternContext("XYZ") should not be PatternContext("ZYX")
  }
  it must "remember the wildcard value from which it was created." in {
    PatternContext("XYZ").star shouldBe "XYZ"
  }
  it must "be able to aggregate wildcards" in {
    PatternContext("ABC").withStar("DEF").withStar("GHI").star shouldBe "ABC DEF GHI"
  }
}
