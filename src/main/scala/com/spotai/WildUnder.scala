package com.spotai

/*
The _ wildcar pattern element.
*/

class WildUnder extends PatternElement {
  override def equals(that:Any) = {
    that match {
      case _:WildUnder => true
      case _ => false
    }
  }
}

object WildUnder {
  def apply() = {
    new WildUnder()
  }
}
