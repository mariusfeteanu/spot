package com.spotai
package pattern
package state

/*
The context in which a pattern was matched.
*/
case class PatternContext(
  // The value of the matched star wildcard
  star:String
)
