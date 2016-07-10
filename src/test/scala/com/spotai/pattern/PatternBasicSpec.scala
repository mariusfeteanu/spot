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
import org.scalatest.prop.TableDrivenPropertyChecks._

import com.spotai.pattern.state.PatternContext

object PatternBasicSpec {
  def getMatches(patternString:String, question:String) = {
    val pattern = new Pattern(patternString)
    var patternContext = PatternContext("")
    pattern.matches(Bot.split(question), patternContext)
  }
  val allWildcards = Table(("wild"), ("*"), ("_"))
}

class PatternBasicSpec extends FlatSpec with Matchers {
  import PatternBasicSpec.getMatches
  import PatternBasicSpec.allWildcards
  /* ------------------------------------------- */
  behavior of "An empty Pattern (from empty list)."
  it must "have no elements" in {
    val pattern = Pattern(Nil)
    pattern.patternElements.size shouldBe 0
  }

  /* --------------------------------------------- */
  behavior of "An empty Pattern (from empty string)."
  it must "have no elements" in {
    val pattern = new Pattern("")
    pattern.patternElements.size shouldBe 0
  }

  /* -------------------------------- */
  behavior of "A Pattern (from string)."
  it must "parse * to a list of one WildStar element." in {
    val pattern = new Pattern("*")
    pattern.patternElements.size shouldBe 1
    pattern.patternElements(0) shouldBe WildStar()
  }
  it must "parse _ to a list of one WildUnder element." in {
    val pattern = new Pattern("_")
    pattern.patternElements.size shouldBe 1
    pattern.patternElements(0) shouldBe WildUnder()
  }
  it must "parse word XYZ to a list of one PatternWord element containing just XYZ." in {
    val pattern = new Pattern("XYZ")
    pattern.patternElements.size shouldBe 1
    pattern.patternElements(0) shouldBe PatternWord("XYZ")
  }
  it must "parse a string of words into a list of identical PatternWords." in {
    val pattern = new Pattern("XX YY ZZ")
    pattern.patternElements.size shouldBe 3
    pattern.patternElements shouldBe List(
      PatternWord("XX"),
      PatternWord("YY"),
      PatternWord("ZZ")
    )
  }
  it must "parse a complex string into a list of identical PatternWords." in {
    val pattern = new Pattern("XX YY * _ ZZ")
    pattern.patternElements.size shouldBe 5
    pattern.patternElements shouldBe List(
      PatternWord("XX"),
      PatternWord("YY"),
      WildStar(),
      WildUnder(),
      PatternWord("ZZ")
    )
  }
  /* Case insensitivity */
  it must "be case insensitive in a complex string when building the pattern." in {
    val pattern = new Pattern("Xx yy * _ ZZ")
    pattern.patternElements.size shouldBe 5
    pattern.patternElements shouldBe List(
      PatternWord("XX"),
      PatternWord("YY"),
      WildStar(),
      WildUnder(),
      PatternWord("ZZ")
    )
  }

  /* --------------------------------------- */
  behavior of "The pattern: '' (empty pattern)"
  it must "match empty string: ''" in {
    getMatches("", "") should not be empty
  }
  it must "not match an actual sentence: 'ABC DEF'" in {
    getMatches("", "ABC DEF") shouldBe empty
  }

  /* -------------------------- */
  behavior of "The pattern: 'XYZ'"
  it must "not match an empty string: ''" in {
    getMatches("XYZ", "") shouldBe empty
  }
  it must "match that exact word: 'XYZ'" in {
    getMatches("XYZ", "XYZ") should not be empty
  }
  it must "not match another word: 'ABC'" in {
    getMatches("XYZ", "ABC") shouldBe empty
  }
  it must "not match a sentence ending with XYZ: 'ABC DEF XYZ'" in {
    getMatches("XYZ", "ABC DEF XYZ") shouldBe empty
  }
  it must "not match a sentence begining with XYZ: 'XYZ ABC DEF'" in {
    getMatches("XYZ", "XYZ ABC DEF") shouldBe empty
  }
  it must "not match a sentence containing XYZ: 'ABC XYZ DEF'" in {
    getMatches("XYZ", "ABC XYZ DEF") shouldBe empty
  }

  /* ------------------------------- */
  behavior of "The pattern: (wildcard)"
  it must "not match an empty string: ''" in {
    forAll(allWildcards) { (wildcard:String) =>
      getMatches(s"$wildcard", "") shouldBe empty
    }
  }
  it must "match a word (we assume it matches any): 'XYZ'" in {
    forAll(allWildcards) { (wildcard:String) =>
      getMatches(s"$wildcard", "XYZ") should not be empty
      getMatches(s"$wildcard", "XYZ").get.star shouldBe "XYZ"
    }
  }
  it must "match a sentence (we assume it matches any): 'ABC DEF GHI'" in {
    forAll(allWildcards) { (wildcard:String) =>
      getMatches(s"$wildcard", "ABC DEF GHI") should not be empty
      getMatches(s"$wildcard", "ABC DEF GHI").get.star shouldBe "ABC DEF GHI"
    }
  }

}
