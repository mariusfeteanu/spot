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

class PatternContainsSpec extends FlatSpec with Matchers {
  import PatternBasicSpec.getMatches
  import PatternBasicSpec.allWildcards
  /* ------------------------------------------------------------ */
  val containsPatternFun = (wildcard:String) => s"UVW $wildcard XYZ"
  behavior of s"The pattern: 'UVW (wildcard) XYZ'"
  it must "not match an empty string: ''" in {
    forAll(allWildcards) { (wildcard:String) =>
      val containsPattern = containsPatternFun(wildcard)
      getMatches(s"$containsPattern", "") shouldBe empty
    }
  }
  /* Words not matching */
  it must "not match the single word: 'XYZ'" in {
    forAll(allWildcards) { (wildcard:String) =>
      val containsPattern = containsPatternFun(wildcard)
      getMatches(s"$containsPattern", "XYZ") shouldBe empty
    }
  }
  it must "not match the single word: 'UVW'" in {
    forAll(allWildcards) { (wildcard:String) =>
      val containsPattern = containsPatternFun(wildcard)
      getMatches(s"$containsPattern", "UVW") shouldBe empty
    }
  }
  it must "not match some other word: 'ABC'" in {
    forAll(allWildcards) { (wildcard:String) =>
      val containsPattern = containsPatternFun(wildcard)
      getMatches(s"$containsPattern", "ABC") shouldBe empty
    }
  }
  it must "not match a word preceded by XYZ: 'XYZ ABC'" in {
    forAll(allWildcards) { (wildcard:String) =>
      val containsPattern = containsPatternFun(wildcard)
      getMatches(s"$containsPattern", "XYZ ABC") shouldBe empty
    }
  }
  it must "not match a word preceded by UVW: 'UVW ABC'" in {
    forAll(allWildcards) { (wildcard:String) =>
      val containsPattern = containsPatternFun(wildcard)
      getMatches(s"$containsPattern", "UVW ABC") shouldBe empty
    }
  }
  it must "not match a word followed by XYZ: 'ABC XYZ'" in {
    forAll(allWildcards) { (wildcard:String) =>
      val containsPattern = containsPatternFun(wildcard)
      getMatches(s"$containsPattern", "ABC XYZ") shouldBe empty
    }
  }
  it must "not match a word followed by UVW: 'ABC UVW'" in {
    forAll(allWildcards) { (wildcard:String) =>
      val containsPattern = containsPatternFun(wildcard)
      getMatches(s"$containsPattern", "ABC UVW") shouldBe empty
    }
  }

  /* Sentence not matching */
  it must "not match a sentence containing XYZ: 'ABC XYZ DEF'" in {
    forAll(allWildcards) { (wildcard:String) =>
      val containsPattern = containsPatternFun(wildcard)
      getMatches(s"$containsPattern", "ABC XYZ DEF") shouldBe empty
    }
  }
  it must "not match a sentence containing UVW: 'ABC UVW DEF'" in {
    forAll(allWildcards) { (wildcard:String) =>
      val containsPattern = containsPatternFun(wildcard)
      getMatches(s"$containsPattern", "ABC UVW DEF") shouldBe empty
    }
  }

  it must "not match a sentence ending in XYZ (and not starting with UVW): 'ABC DEF XYZ'" in {
    forAll(allWildcards) { (wildcard:String) =>
      val containsPattern = containsPatternFun(wildcard)
      getMatches(s"$containsPattern", "ABC DEF XYZ") shouldBe empty
    }
  }
  it must "not match a sentence ending in UVW: 'ABC DEF UVW'" in {
    forAll(allWildcards) { (wildcard:String) =>
      val containsPattern = containsPatternFun(wildcard)
      getMatches(s"$containsPattern", "ABC DEF UVW") shouldBe empty
    }
  }

  it must "not match a sentence starting with XYZ: 'XYZ ABC DEF'" in {
    forAll(allWildcards) { (wildcard:String) =>
      val containsPattern = containsPatternFun(wildcard)
      getMatches(s"$containsPattern", "XYZ ABC DEF") shouldBe empty
    }
  }
  it must "not match a sentence starting with UVW (and not ending in XYZ): 'UVW ABC DEF'" in {
    forAll(allWildcards) { (wildcard:String) =>
      val containsPattern = containsPatternFun(wildcard)
      getMatches(s"$containsPattern", "UVW ABC DEF") shouldBe empty
    }
  }
  it must "not match a complex sentence with a different prefix: 'DEF UVW ABC XYZ'" in {
    forAll(allWildcards) { (wildcard:String) =>
      val containsPattern = containsPatternFun(wildcard)
      getMatches(s"$containsPattern", "DEF UVW ABC XYZ") shouldBe empty
    }
  }
  it must "not match a complex sentence with a different suffix: 'UVW ABC XYZ DEF'" in {
    forAll(allWildcards) { (wildcard:String) =>
      val containsPattern = containsPatternFun(wildcard)
      getMatches(s"$containsPattern", "UVW ABC XYZ DEF") shouldBe empty
    }
  }
  it must "not match a complex sentence with different suffix and prefix: 'GHI UVW ABC XYZ DEF'" in {
    forAll(allWildcards) { (wildcard:String) =>
      val containsPattern = containsPatternFun(wildcard)
      getMatches(s"$containsPattern", "GHI UVW ABC XYZ DEF") shouldBe empty
    }
  }

  /* Word matching */
  it must "match a simple sentence: 'UVW ABC XYZ'" in {
    forAll(allWildcards) { (wildcard:String) =>
      val containsPattern = containsPatternFun(wildcard)
      getMatches(s"$containsPattern", "UVW ABC XYZ") should not be empty
    }
  }

  /* Sentence matching */
  it must "match a complex sentence: 'UVW ABC DEF XYZ'" in {
    forAll(allWildcards) { (wildcard:String) =>
      val containsPattern = containsPatternFun(wildcard)
      getMatches(s"$containsPattern", "UVW ABC DEF XYZ") should not be empty
    }
  }
  it must "match a complex sentence (lower case): 'uvw abc def xyz'" in {
    forAll(allWildcards) { (wildcard:String) =>
      val containsPattern = containsPatternFun(wildcard)
      getMatches(s"$containsPattern", "uvw abc def xyz") should not be empty
    }
  }
}
