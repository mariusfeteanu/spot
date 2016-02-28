package com.spotai
package pattern

/*
The * wildcar pattern element.
*/
class WildStar extends PatternElement {
  override def equals(that:Any) = {
    that match {
      case _:WildStar => true
      case _ => false
    }
  }
}

object WildStar {
  def apply() = {
    new WildStar()
  }
}
