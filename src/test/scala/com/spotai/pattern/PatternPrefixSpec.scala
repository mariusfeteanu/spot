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

class PatternPrefixSpec extends FlatSpec with Matchers {
  import PatternBasicSpec.getMatches
  import PatternBasicSpec.allWildcards
  /* -------------------------------------------------- */
  val prePatternFun = (wildcard:String) => s"$wildcard XYZ"
  behavior of s"The pattern: '(wildcard) XYZ'"
  it must "not match an empty string: ''" in {
    forAll(allWildcards) { (wildcard:String) =>
      val prePattern = prePatternFun(wildcard)
      getMatches(s"$prePattern", "") shouldBe empty
    }
  }
  it must "not match the single word: 'XYZ'" in {
    forAll(allWildcards) { (wildcard:String) =>
      val prePattern = prePatternFun(wildcard)
      getMatches(s"$prePattern", "XYZ") shouldBe empty
    }
  }
  it must "not match some other word: 'ABC'" in {
    forAll(allWildcards) { (wildcard:String) =>
      val prePattern = prePatternFun(wildcard)
      getMatches(s"$prePattern", "ABC") shouldBe empty
    }
  }
  it must "not match a word preceded by XYZ: 'XYZ ABC'" in {
    forAll(allWildcards) { (wildcard:String) =>
      val prePattern = prePatternFun(wildcard)
      getMatches(s"$prePattern", "XYZ ABC") shouldBe empty
    }
  }
  it must "not match a sentence containing XYZ: 'ABC XYZ DEF'" in {
    forAll(allWildcards) { (wildcard:String) =>
      val prePattern = prePatternFun(wildcard)
      getMatches(s"$prePattern", "ABC XYZ DEF") shouldBe empty
    }
  }
  it must "match a word followed by XYZ (we assume it matches any): 'ABC XYZ'" in {
    forAll(allWildcards) { (wildcard:String) =>
      val prePattern = prePatternFun(wildcard)
      getMatches(s"$prePattern", "ABC XYZ") should not be empty
      getMatches(s"$prePattern", "ABC XYZ").get.star shouldBe "ABC"
    }
  }
  it must "match a sentence followed by XYZ (we assume it matches any): 'ABC DEF XYZ'" in {
    forAll(allWildcards) { (wildcard:String) =>
      val prePattern = prePatternFun(wildcard)
      getMatches(s"$prePattern", "ABC DEF XYZ") should not be empty
      getMatches(s"$prePattern", "ABC DEF XYZ").get.star shouldBe "ABC DEF"
    }
  }
}
