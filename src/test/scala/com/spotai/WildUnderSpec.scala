import org.scalatest.FlatSpec

import com.spotai.WildUnder

class WildUnderSpec extends FlatSpec{
  behavior of "A WildUnder."
  it must "must equal any other WildUnder" in {
    assert(new WildUnder() == new WildUnder())
  }
}
