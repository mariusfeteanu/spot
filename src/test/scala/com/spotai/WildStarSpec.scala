import org.scalatest.FlatSpec

import com.spotai.pattern.WildStar

class WildStarSpec extends FlatSpec{
  behavior of "A WildStar."
  it must "must equal any other WildStar" in {
    assert(new WildStar() == new WildStar())
  }
}
