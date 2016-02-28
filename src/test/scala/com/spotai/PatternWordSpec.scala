import org.scalatest.FlatSpec

import com.spotai.pattern.PatternWord

class PatternWordSpec extends FlatSpec{
  behavior of "A PatternWord."
  it must "equal if from equal String" in {
    assert(PatternWord("XYZ") == PatternWord("XYZ"))
  }
  it must "different if from different String" in {
    assert(PatternWord("XYZ") != PatternWord("ZYX"))
  }
  it must "have a content equal to the string it was created from" in {
    assert(PatternWord("XYZ").word == "XYZ")
  }
}
