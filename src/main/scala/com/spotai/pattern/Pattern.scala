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

import com.spotai.pattern.state.PatternContext
import com.spotai.Bot

/*
Represents a pattern that has to be matched by user input.
*/
case class Pattern(patternElements:List[PatternElement]) extends Ordered[Pattern]{

  def this(stringPattern:String) = {this(
   stringPattern.split(" ").filter(_.size>0).map({
      case "*" => WildStar()
      case "_" => WildUnder()
      case patternWord:String => PatternWord(Bot.normalize(patternWord))}).toList
  )}

  /*
  Result of comparing this with operand that.
  returns x where
    x < 0 iff this < that
    x == 0 iff this == that
    x > 0 iff this > that
  */
  override def compare(that:Pattern) = {
    def compPattern(left:List[PatternElement], right:List[PatternElement]):Int = {
      if (left.isEmpty)
        if (right.isEmpty) 0
        else 1 // The longer pattern will be mached first (TODO: is this correct)
      else
        if (right.isEmpty) -1 // The longer pattern will be mached first (TODO: is this correct)
        else {
          if (left.head>right.head) 1
          if (left.head<right.head) -1
          else compPattern(left.tail, right.tail)
        }
    }
    compPattern(this.patternElements, that.patternElements)
  }

  /*
  Main pattern method
  - checks that the input matches
  - return None if it doesn't match
  - returns the match context if matched
  TODO: this nukes the context on success
  */
  def matches(input:Seq[String], context:PatternContext):Option[PatternContext] = {
    /* Look at the current input to see if it matches */
    input match {
      // We reached the end of the user input
      case Nil => patternElements match {
        // If we are also out of pattern then it means everything matched
        case Nil => Some(context)
        // If the input is consumed, but there is still patern to match then we don't have a match
        case _ => None
      }
      // We still have some input to process
      case _ => patternElements match {
        // If we are out out pattern elements and we still have some input then we don't have a match
        case Nil => None
        // Check that the word actually matches
        case _ => patternElements.head match {
          // The underscore wildcard has highes priority
          case _:WildUnder /* We try to match the wild card on the tail*/
            => Pattern(patternElements.tail).matches(input.tail, context.withStar(input.head)) match {
              /* and if it doesn't work, try the full remaining pattern again */
              case None => Pattern(patternElements).matches(input.tail, context.withStar(input.head))
              /* Return the match we found */
              case found => found
            }
          // If the current word matches the current pattern we can continue
          case patternWord:PatternWord if patternWord.word == input.head
            => Pattern(patternElements.tail).matches(input.tail, context)
          // The start wildcard has lowest priority
          case _:WildStar /* We try to match the wild card on the tail*/
            => Pattern(patternElements.tail).matches(input.tail, context.withStar(input.head)) match {
              /* and if it doesn't work, try the full remaining pattern again */
              case None => Pattern(patternElements).matches(input.tail, context.withStar(input.head))
              /* Return the match we found */
              case found => found
            }
          // If the current word does not match the current pattern (and no wild cards were matched) then we give up
          case patternWord:PatternWord
            => None
        }
      }
    }
  }
}
