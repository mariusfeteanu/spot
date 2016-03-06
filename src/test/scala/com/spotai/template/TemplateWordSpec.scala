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

import org.scalatest._

class TemplateWordSpec extends FlatSpec with Matchers {
  behavior of "A TemplateWord."
  it must "be equal to another if created from the same String" in {
    TemplateWord("XYZ") shouldBe TemplateWord("XYZ")
  }
  it must "be different from another if created from different String" in {
    TemplateWord("XYZ") should not be TemplateWord("ZYX")
  }
  it must "have a content equal to the string it was created from" in {
    TemplateWord("XYZ").word shouldBe "XYZ"
  }
}
