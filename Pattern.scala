case class Pattern(patternElements:List[PatternElement]){

  def this(stringPattern:String) = {this(
   stringPattern.split(" ").map({
      case "*" => WildStar()
      case "_" => WildUnder()
      case patternWord:String => PatternWord(patternWord)}).toList
  )}

  def cleanString(word:String):String = word.toUpperCase.replaceAll("[^a-zA-Z 0-9]", "").replaceAll("\\s", " ")

  /*
  Main pattern method
  - checks that the input matches
  - return None if it doesn't match
  - returns the match context if matched
  */
  def apply(input:Seq[String], context:Option[PatternContext]):Option[PatternContext] = {
    /* Look at the current input to see if it matches */
    input match {
      // We reached the end of the user input
      case Nil => patternElements match {
        // If we are also out of pattern then it means everything matched
        case Nil => context
        // If the input is consumed, but there is still patern to match then we don't have a match
        case _ => None
      }
      // We still have some input to process
      case _ => patternElements match {
        // If we are out out pattern elements and we still have some input then we don't have a match
        case Nil => None
        // Check that the word actually matches
        case _ => patternElements.head match{
          // The underscore wildcard has highes priority
          case _:WildUnder
          //TODO: This not tail recursive, but making it so is ugly, what do?
            => Pattern(patternElements.tail)(input.tail, context)
          // If the current word matches the current pattern we can continue
          case patternWord:PatternWord if this.cleanString(patternWord.word) == this.cleanString(input.head)
            => Pattern(patternElements.tail)(input.tail, context)
          // The start wildcard has lowest priority
          case _:WildStar
            => Pattern(patternElements.tail)(input.tail, Some(PatternContext(input.head)))
          // If the current word does not match the current pattern (and no wild cards were matched) then we give up
          case patternWord:PatternWord
            => None
        }
      }
    }
  }
}
