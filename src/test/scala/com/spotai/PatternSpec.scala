import org.scalatest.FlatSpec

import com.spotai.{Pattern, WildStar, WildUnder, PatternWord}

class PatternSpec extends FlatSpec{
  behavior of "An empty Pattern (from empty list)."
  it must "have no elements" in {
    val pattern = Pattern(Nil)
    assert(pattern.patternElements.size == 0)
  }

  behavior of "An empty Pattern (from empty string)."
  it must "have no elements" in {
    val pattern = new Pattern("")
    assert(pattern.patternElements.size == 0)
  }
  it must "parse * to a list of one WildStar element." in {
    val pattern = new Pattern("*")
    assert(pattern.patternElements.size == 1)
    assert(pattern.patternElements(0) == WildStar())
  }
  it must "parse _ to a list of one WildUnder element." in {
    val pattern = new Pattern("_")
    assert(pattern.patternElements.size == 1)
    assert(pattern.patternElements(0) == WildUnder())
  }
  it must "parse word XYZ to a list of one PatternWord element containing just XYZ." in {
    val pattern = new Pattern("XYZ")
    assert(pattern.patternElements.size == 1)
    assert(pattern.patternElements(0) == PatternWord("XYZ"))
  }
  it must "parse a string of words into a list of identical PatternWords." in {
    val pattern = new Pattern("XX YY ZZ")
    assert(pattern.patternElements.size == 3)
    assert(pattern.patternElements == List(
      PatternWord("XX"),
      PatternWord("YY"),
      PatternWord("ZZ")
    ))
  }
  it must "parse a complex string into a list of identical PatternWords." in {
    val pattern = new Pattern("XX YY * _ ZZ")
    assert(pattern.patternElements.size == 5)
    assert(pattern.patternElements == List(
      PatternWord("XX"),
      PatternWord("YY"),
      WildStar(),
      WildUnder(),
      PatternWord("ZZ")
    ))
  }
}
