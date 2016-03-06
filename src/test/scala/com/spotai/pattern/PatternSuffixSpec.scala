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

class PatternSuffixSpec extends FlatSpec with Matchers {
  import PatternBasicSpec.getMatches
  import PatternBasicSpec.allWildcards
  /* ----------------------- */
  val postPatternFun = (wildcard:String) => s"XYZ $wildcard"
  behavior of s"The pattern: 'XYZ (wildcard)'"
  it must "not match an empty string: ''" in {
    forAll(allWildcards) { (wildcard:String) =>
      val postPattern = postPatternFun(wildcard)
      getMatches(s"$postPattern", "") shouldBe empty
    }
  }
  it must "not match the single word: 'XYZ'" in {
    forAll(allWildcards) { (wildcard:String) =>
      val postPattern = postPatternFun(wildcard)
      getMatches(s"$postPattern", "XYZ") shouldBe empty
    }
  }
  it must "not match some other word: 'ABC'" in {
    forAll(allWildcards) { (wildcard:String) =>
      val postPattern = postPatternFun(wildcard)
      getMatches(s"$postPattern", "ABC") shouldBe empty
    }
  }
  it must "match a word preceded by XYZ: 'XYZ ABC'" in {
    forAll(allWildcards) { (wildcard:String) =>
      val postPattern = postPatternFun(wildcard)
      getMatches(s"$postPattern", "XYZ ABC") should not be empty
      getMatches(s"$postPattern", "XYZ ABC").get.star shouldBe "ABC"
    }
  }
  it must "not match a sentence containing XYZ: 'ABC XYZ DEF'" in {
    forAll(allWildcards) { (wildcard:String) =>
      val postPattern = postPatternFun(wildcard)
      getMatches(s"$postPattern", "ABC XYZ DEF") shouldBe empty
    }
  }
  it must "not match a word followed by XYZ (we assume it matches any): 'ABC XYZ'" in {
    forAll(allWildcards) { (wildcard:String) =>
      val postPattern = postPatternFun(wildcard)
      getMatches(s"$postPattern", "ABC XYZ") shouldBe empty
    }
  }
  it must "match a sentence preceded by XYZ (we assume it matches any): 'XYZ ABC DEF'" in {
    forAll(allWildcards) { (wildcard:String) =>
      val postPattern = postPatternFun(wildcard)
      getMatches(s"$postPattern", "XYZ ABC DEF") should not be empty
      getMatches(s"$postPattern", "XYZ ABC DEF").get.star shouldBe "ABC DEF"
    }
  }
}
