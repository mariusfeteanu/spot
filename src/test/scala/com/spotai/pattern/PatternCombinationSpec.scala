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

  val allWildcardPairs = Table(
    ("left", "right"),
    ("*", "_"),
    ("_", "*")
  )
  val patternFun1 = (left:String, right:String) => s"$left $right"

  /* ----------------------- */
  behavior of "The pattern: '(wildcard) XYZ (otherWildcard)'"
  it must "not match an empty string: ''" in {
    forAll(allWildcardPairs) { (left:String, right:String) =>
      getMatches(patternFun1(left, right), "") shouldBe empty
    }
  }
  it must "not match a single word:'UVW'" in {
    forAll(allWildcardPairs) { (left:String, right:String) =>
      getMatches(patternFun1(left, right), "UVW") shouldBe empty
    }
  }
  it must "match two random words: 'UVW XYZ'" in {
    forAll(allWildcardPairs) { (left:String, right:String) =>
      getMatches(patternFun1(left, right), "UVW XYZ") should not be empty
      getMatches(patternFun1(left, right), "UVW XYZ").get.star shouldBe "UVW XYZ"
    }
  }
  it must "match many random words: 'UVW XYZ ABC DEF'" in {
    forAll(allWildcardPairs) { (left:String, right:String) =>
      getMatches(patternFun1(left, right), "UVW XYZ ABC DEF") should not be empty
      getMatches(patternFun1(left, right), "UVW XYZ ABC DEF").get.star shouldBe "UVW XYZ ABC DEF"
    }
  }

  /* --------------------------- */
  behavior of "The pattern: '(wildcard) XYZ (otherWildcard)'"
  val patternFun2 = (left:String, right:String) => s"$left XYZ $right"

  it must "not match an empty string: ''" in {
    forAll(allWildcardPairs) { (left:String, right:String) =>
      getMatches(patternFun2(left, right), "") shouldBe empty
    }
  }
  it must "not match a single word: 'ABC'" in {
    forAll(allWildcardPairs) { (left:String, right:String) =>
      getMatches(patternFun2(left, right), "ABC") shouldBe empty
    }
  }
  it must "not match two random words: 'ABC DEF'" in {
    forAll(allWildcardPairs) { (left:String, right:String) =>
      getMatches(patternFun2(left, right), "ABC DEF") shouldBe empty
    }
  }
  it must "not match the word: 'XYZ'" in {
    forAll(allWildcardPairs) { (left:String, right:String) =>
      getMatches(patternFun2(left, right), "XYZ") shouldBe empty
    }
  }
  it must "not match a sentence ending in XYZ: 'ABC XYZ', 'ABC DEF XYZ'" in {
    forAll(allWildcardPairs) { (left:String, right:String) =>
      getMatches(patternFun2(left, right), "ABC XYZ") shouldBe empty
      getMatches(patternFun2(left, right), "ABC DEF XYZ") shouldBe empty
    }
  }
  it must "not match a starting with in XYZ: 'XYZ ABC', 'XYZ ABC DEF'" in {
    forAll(allWildcardPairs) { (left:String, right:String) =>
      getMatches(patternFun2(left, right), "XYZ ABC") shouldBe empty
      getMatches(patternFun3(left, right), "XYZ ABC DEF") shouldBe empty
    }
  }
  it must "match a simple sentence containing XYZ: 'ABC XYZ DEF'" in {
    forAll(allWildcardPairs) { (left:String, right:String) =>
      getMatches(patternFun2(left, right), "ABC XYZ DEF") should not be empty
    }
  }
  it must "match a complex sentence containing XYZ: 'ABC DEF XYZ GHI JKL'" in {
    forAll(allWildcardPairs) { (left:String, right:String) =>
      getMatches(patternFun2(left, right), "ABC DEF XYZ GHI JKL") should not be empty
    }
  }

  /* --------------------------- */
  behavior of "The pattern: '(wildcard) (otherWildcard) XYZ '"
  val patternFun3 = (left:String, right:String) => s"$left $right XYZ"

  it must "not match an empty string: ''" in {
    forAll(allWildcardPairs) { (left:String, right:String) =>
      getMatches(patternFun3(left, right), "") shouldBe empty
    }
  }
  it must "not match a single word: 'ABC'" in {
    forAll(allWildcardPairs) { (left:String, right:String) =>
      getMatches(patternFun3(left, right), "ABC") shouldBe empty
    }
  }
  it must "not match two random words: 'ABC DEF'" in {
    forAll(allWildcardPairs) { (left:String, right:String) =>
      getMatches(patternFun3(left, right), "ABC DEF") shouldBe empty
    }
  }
  it must "not match the word: 'XYZ'" in {
    forAll(allWildcardPairs) { (left:String, right:String) =>
      getMatches(patternFun3(left, right), "XYZ") shouldBe empty
    }
  }
  it must "not match this sentence ending in XYZ: 'ABC XYZ'." in {
    forAll(allWildcardPairs) { (left:String, right:String) =>
      getMatches(patternFun3(left, right), "ABC XYZ") shouldBe empty
    }
  }
  it must "match this sentence ending in XYZ: 'ABC DEF XYZ'." in {
    forAll(allWildcardPairs) { (left:String, right:String) =>
      getMatches(patternFun3(left, right), "ABC DEF XYZ") should not be empty
    }
  }
  it must "match this sentence ending in XYZ: 'ABC DEF GHI XYZ'." in {
    forAll(allWildcardPairs) { (left:String, right:String) =>
      getMatches(patternFun3(left, right), "ABC DEF GHI XYZ") should not be empty
    }
  }
  it must "not match a starting with in XYZ: 'XYZ ABC', 'XYZ ABC DEF'" in {
    forAll(allWildcardPairs) { (left:String, right:String) =>
      getMatches(patternFun3(left, right), "XYZ ABC") shouldBe empty
      getMatches(patternFun3(left, right), "XYZ ABC DEF") shouldBe empty
    }
  }
  it must "not match a simple sentence containing XYZ: 'ABC XYZ DEF'" in {
    forAll(allWildcardPairs) { (left:String, right:String) =>
      getMatches(patternFun3(left, right), "ABC XYZ DEF") shouldBe empty
    }
  }
  it must "not match a complex sentence containing XYZ: 'ABC DEF XYZ GHI JKL'" in {
    forAll(allWildcardPairs) { (left:String, right:String) =>
      getMatches(patternFun3(left, right), "ABC DEF XYZ GHI JKL") shouldBe empty
    }
  }

  /* --------------------------- */
  behavior of "The pattern: 'XYZ (wildcard) (otherWildcard)'"
  val patternFun4 = (left:String, right:String) => s"XYZ $left $right"

  it must "not match an empty string: ''" in {
    forAll(allWildcardPairs) { (left:String, right:String) =>
      getMatches(patternFun4(left, right), "") shouldBe empty
    }
  }
  it must "not match a single word: 'ABC'" in {
    forAll(allWildcardPairs) { (left:String, right:String) =>
      getMatches(patternFun4(left, right), "ABC") shouldBe empty
    }
  }
  it must "not match two random words: 'ABC DEF'" in {
    forAll(allWildcardPairs) { (left:String, right:String) =>
      getMatches(patternFun4(left, right), "ABC DEF") shouldBe empty
    }
  }
  it must "not match the word: 'XYZ'" in {
    forAll(allWildcardPairs) { (left:String, right:String) =>
      getMatches(patternFun4(left, right), "XYZ") shouldBe empty
    }
  }
  it must "not match this sentence ending in XYZ: 'ABC XYZ'." in {
    forAll(allWildcardPairs) { (left:String, right:String) =>
      getMatches(patternFun4(left, right), "ABC XYZ") shouldBe empty
    }
  }
  it must "not match this sentence ending in XYZ: 'ABC DEF XYZ'." in {
    forAll(allWildcardPairs) { (left:String, right:String) =>
      getMatches(patternFun4(left, right), "ABC DEF XYZ") shouldBe empty
    }
  }
  it must "not match this sentence starting with in XYZ: 'XYZ ABC'." in {
    forAll(allWildcardPairs) { (left:String, right:String) =>
      getMatches(patternFun3(left, right), "XYZ ABC") shouldBe empty
    }
  }
  it must "match this sentence starting with in XYZ: 'XYZ ABC DEF'." in {
    forAll(allWildcardPairs) { (left:String, right:String) =>
      getMatches(patternFun4(left, right), "XYZ ABC DEF") should not be empty
    }
  }
  it must "match this sentence starting with in XYZ: 'XYZ ABC DEF GHI'." in {
    forAll(allWildcardPairs) { (left:String, right:String) =>
      getMatches(patternFun4(left, right), "XYZ ABC DEF GHI") should not be empty
    }
  }
  it must "not match a simple sentence containing XYZ: 'ABC XYZ DEF'" in {
    forAll(allWildcardPairs) { (left:String, right:String) =>
      getMatches(patternFun4(left, right), "ABC XYZ DEF") shouldBe empty
    }
  }
  it must "not match a complex sentence containing XYZ: 'ABC DEF XYZ GHI JKL'" in {
    forAll(allWildcardPairs) { (left:String, right:String) =>
      getMatches(patternFun4(left, right), "ABC DEF XYZ GHI JKL") shouldBe empty
    }
  }
}
