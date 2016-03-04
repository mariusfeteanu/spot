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
import org.scalatest._

import com.spotai.pattern.{Pattern, WildStar, WildUnder, PatternWord}
import com.spotai.pattern.state.PatternContext
import com.spotai.Bot

class PatternSpec extends FlatSpec with Matchers {
  def getMatches(patternString:String, question:String) = {
    val pattern = new Pattern(patternString)
    var patternContext = PatternContext("")
    pattern.matches(Bot.split(question), patternContext)
  }
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
  it must "ignore case in a complex string when building the pattern." in {
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
  it must "match empty string" in {
    getMatches("", "") should not be empty
  }
  it must "not match an actual sentence" in {
    getMatches("", "ABC DEF") shouldBe empty
  }

  /* -------------------------- */
  behavior of "The pattern: 'XYZ'"
  it must "not match an empty string." in {
    getMatches("XYZ", "") shouldBe empty
  }
  it must "match that exact word." in {
    getMatches("XYZ", "XYZ") should not be empty
  }
  it must "not match another word." in {
    getMatches("XYZ", "ABC") shouldBe empty
  }
  it must "not match a sentence ending with XYZ." in {
    getMatches("XYZ", "ABC DEF XYZ") shouldBe empty
  }
  it must "not match a sentence begining with XYZ." in {
    getMatches("XYZ", "XYZ ABC DEF") shouldBe empty
  }
  it must "not match a sentence containing XYZ." in {
    getMatches("XYZ", "ABC XYZ DEF") shouldBe empty
  }

  /* --------------------------------------- */
  behavior of "The pattern: '*' (wildcar star)"
  it must "not match an empty string." in {
    getMatches("*", "") shouldBe empty
  }
  it must "match a word (we assume it matches any)." in {
    getMatches("*", "XYZ") should not be empty
    getMatches("*", "XYZ").get.star shouldBe "XYZ"
  }
  ignore must "match a sentence (we assume it matches any)." in {
    getMatches("*", "ABC DEF GHI") should not be empty
    getMatches("*", "ABC DEF GHI").get.star shouldBe "ABC DEF GHI"
  }

  /* ----------------------- */
  val preStarPattern = "* XYZ"
  behavior of s"The pattern: '$preStarPattern'"
  it must "not match an empty string." in {
    getMatches(s"$preStarPattern", "") shouldBe empty
  }
  it must "not match the single word XYZ." in {
    getMatches(s"$preStarPattern", "XYZ") shouldBe empty
  }
  it must "not match some other word." in {
    getMatches(s"$preStarPattern", "ABC") shouldBe empty
  }
  it must "not match a word preceded by XYZ" in {
    getMatches(s"$preStarPattern", "XYZ ABC") shouldBe empty
  }
  it must "not match a sentence containing XYZ" in {
    getMatches(s"$preStarPattern", "ABC XYZ DEF") shouldBe empty
  }
  it must "match a word followed by XYZ (we assume it matches any)." in {
    getMatches(s"$preStarPattern", "ABC XYZ") should not be empty
    getMatches(s"$preStarPattern", "ABC XYZ").get.star shouldBe "ABC"
  }
  ignore must "match a sentence followed by XYZ (we assume it matches any)." in {
    getMatches(s"$preStarPattern", "ABC DEF XYZ") should not be empty
    getMatches(s"$preStarPattern", "ABC DEF XYZ").get.star shouldBe "ABC DEF"
  }

  /* ----------------------- */
  val postStarPattern = "XYZ *"
  behavior of s"The pattern: '$postStarPattern'"
  it must "not match an empty string." in {
    getMatches(s"$postStarPattern", "") shouldBe empty
  }
  it must "not match the single word XYZ." in {
    getMatches(s"$postStarPattern", "XYZ") shouldBe empty
  }
  it must "not match some other word." in {
    getMatches(s"$postStarPattern", "ABC") shouldBe empty
  }
  it must "match a word preceded by XYZ" in {
    getMatches(s"$postStarPattern", "XYZ ABC") should not be empty
    getMatches(s"$postStarPattern", "XYZ ABC").get.star shouldBe "ABC"
  }
  it must "not match a sentence containing XYZ" in {
    getMatches(s"$postStarPattern", "ABC XYZ DEF") shouldBe empty
  }
  it must "not match a word followed by XYZ (we assume it matches any)." in {
    getMatches(s"$postStarPattern", "ABC XYZ") shouldBe empty
  }
  ignore must "match a sentence preceded by XYZ (we assume it matches any)." in {
    getMatches(s"$postStarPattern", "XYZ ABC DEF") shouldBe empty
  }

  /* ----------------------- */
  val containsStarPattern = "UVW * XYZ"
  behavior of s"The pattern: '$containsStarPattern'"
  it must "not match an empty string." in {
    getMatches(s"$containsStarPattern", "") shouldBe empty
  }
  /* Words not matching */
  it must "not match the single word XYZ." in {
    getMatches(s"$containsStarPattern", "XYZ") shouldBe empty
  }
  it must "not match the single word UVW." in {
    getMatches(s"$containsStarPattern", "UVW") shouldBe empty
  }
  it must "not match some other word." in {
    getMatches(s"$containsStarPattern", "ABC") shouldBe empty
  }
  it must "not match a word preceded by XYZ" in {
    getMatches(s"$containsStarPattern", "XYZ ABC") shouldBe empty
  }
  it must "not match a word preceded by UVW" in {
    getMatches(s"$containsStarPattern", "UVW ABC") shouldBe empty
  }
  it must "not match a word followed by XYZ" in {
    getMatches(s"$containsStarPattern", "ABC XYZ") shouldBe empty
  }
  it must "not match a word followed by UVW" in {
    getMatches(s"$containsStarPattern", "ABC UVW") shouldBe empty
  }

  /* Sentence not matching */
  it must "not match a sentence containing XYZ" in {
    getMatches(s"$containsStarPattern", "ABC XYZ DEF") shouldBe empty
  }
  it must "not match a sentence containing UVW" in {
    getMatches(s"$containsStarPattern", "ABC UVW DEF") shouldBe empty
  }

  it must "not match a sentence ending in XYZ (and not starting with UVW)" in {
    getMatches(s"$containsStarPattern", "ABC DEF XYZ") shouldBe empty
  }
  it must "not match a sentence ending in UVW" in {
    getMatches(s"$containsStarPattern", "ABC DEF UVW") shouldBe empty
  }

  it must "not match a sentence starting with XYZ" in {
    getMatches(s"$containsStarPattern", "XYZ ABC DEF") shouldBe empty
  }
  it must "not match a sentence starting with UVW (and not ending in XYZ)" in {
    getMatches(s"$containsStarPattern", "UVW ABC DEF") shouldBe empty
  }
  it must "not match a complex sentence with a different prefix" in {
    getMatches(s"$containsStarPattern", "DEF UVW ABC XYZ") shouldBe empty
  }
  it must "not match a complex sentence with a different suffix" in {
    getMatches(s"$containsStarPattern", "UVW ABC XYZ DEF") shouldBe empty
  }
  it must "not match a complex sentence with different suffix and prefix" in {
    getMatches(s"$containsStarPattern", "GHI UVW ABC XYZ DEF") shouldBe empty
  }

  /* Word matching */
  it must "match a simple sentence 'UVW ABC XYZ'" in {
    getMatches(s"$containsStarPattern", "UVW ABC XYZ") should not be empty
  }

  /* Sentence matching */
  it must "match a complex sentence 'UVW ABC DEF XYZ'" in {
    getMatches(s"$containsStarPattern", "UVW ABC DEF XYZ") should not be empty
  }
}
