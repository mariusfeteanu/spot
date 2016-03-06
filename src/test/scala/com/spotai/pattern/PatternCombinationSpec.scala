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
import com.spotai.Bot

class PatternCombinationSpec extends FlatSpec with Matchers {
  import PatternSpec.getMatches
  /* ----------------------- */
  behavior of "The pattern: '* _'"
  val pattern1 = "* _"
  it must "not match an empty string." in {
    getMatches(s"$pattern1", "") shouldBe empty
  }
  it must "not match a single word." in {
    getMatches(s"$pattern1", "UVW") shouldBe empty
  }
  it must "match two random words." in {
    getMatches(s"$pattern1", "UVW XYZ") should not be empty
    getMatches(s"$pattern1", "UVW XYZ").get.star shouldBe "UVW XYZ"
  }
  it must "match many random words." in {
    getMatches(s"$pattern1", "UVW XYZ ABC DEF") should not be empty
    getMatches(s"$pattern1", "UVW XYZ ABC DEF").get.star shouldBe "UVW XYZ ABC DEF"
  }

  /* --------------------------- */
  behavior of "The pattern: '(wildcard) XYZ (otherWildcard)'"
  val allWildcardPairs = Table(("left", "right"), ("*", "_"), ("_", "*"))
  val patternFun1 = (left:String, right:String) => s"$left XYZ $right"

  it must "not match an empty string." in {
    forAll(allWildcardPairs) { (left:String, right:String) =>
      getMatches(patternFun1(left, right), "") shouldBe empty
    }
  }
  it must "not match a single word." in {
    forAll(allWildcardPairs) { (left:String, right:String) =>
      getMatches(patternFun1(left, right), "ABC") shouldBe empty
    }
  }
  it must "not match two random words." in {
    forAll(allWildcardPairs) { (left:String, right:String) =>
      getMatches(patternFun1(left, right), "ABC DEF") shouldBe empty
    }
  }
  it must "not match the word XYZ." in {
    forAll(allWildcardPairs) { (left:String, right:String) =>
      getMatches(patternFun1(left, right), "XYZ") shouldBe empty
    }
  }
  it must "not match a sentence ending in XYZ." in {
    forAll(allWildcardPairs) { (left:String, right:String) =>
      getMatches(patternFun1(left, right), "ABC XYZ") shouldBe empty
    }
  }
  it must "not match a starting with in XYZ." in {
    forAll(allWildcardPairs) { (left:String, right:String) =>
      getMatches(patternFun1(left, right), "XYZ ABC") shouldBe empty
    }
  }
  it must "match a simple sentence containing XYZ." in {
    forAll(allWildcardPairs) { (left:String, right:String) =>
      getMatches(patternFun1(left, right), "ABC XYZ DEF") should not be empty
    }
  }
  it must "match a complex sentence containing XYZ." in {
    forAll(allWildcardPairs) { (left:String, right:String) =>
      getMatches(patternFun1(left, right), "ABC DEF XYZ GHI JKL") should not be empty
    }
  }

}
