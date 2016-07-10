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

import com.spotai.state.{BotContext, MemoryBotContext}
import com.spotai.pattern.state.PatternContext
import com.spotai.parse.AIMLParser
import com.spotai.pattern._
import com.spotai.template._

import java.io.InputStream

/*
This class implements part of the AIML language.
You can build from an xml specification, and the interrogate the both with the apply metod.
botInstance("your question here")
*/
class Bot(categories:List[Category]){
  var context:BotContext = MemoryBotContext()

  def this(categories:List[Category], context:BotContext){
    this(categories)
    this.context = context
  }

  private def getStimulus(category:Category) = {
    Pattern(
      category.stimulus.patternElements ++
      (ThatPlaceholder() ::
      (category.that match {case None => List[PatternElement](WildStar()); case Some(t:Pattern) => t.patternElements}) ++
      (TopicPlaceholder() ::
      (category.topic match {case None => List[PatternElement](WildStar()); case Some(t:Pattern) => t.patternElements})
      )))
  }

  private def getQuestion(input:String) = {
    Bot.split(input) ++
    ("<THAT>" ::
    (
      (this.context.lastResponse match {case None => List[String](""); case Some(that:String) => Bot.split(that)}) ++
      List[String]("<TOPIC>", "")
    )
    )
  }

  /*
  Main bot method, provides a response to a stimulus
  */
  def ask(input:String):String = {
    /*
    This is variable because we need to re-assign it based on the result of the match
    */
    var patternContext = PatternContext("", false)
    /* We look through all the configured categories to find one that maches, we check
      - topic
      - question (stimulus, input, pattern whatever)
      - that
      */
      categories.find({category =>
      // We check that the actual pattern matches
      val stimulus = getStimulus(category)
      val question = getQuestion(input)

      stimulus.matches(question, patternContext) match {
        case None => false
        case Some(matchPatternContext) => {
          patternContext = matchPatternContext
          true
        }
      }
    }) match {
      case Some(category:Category) =>{
        // We save the current response, and use it to match <that/> next time
        val response = category.response(this, patternContext)
        this.context.lastResponse = Some(response)
        response
      }
      case None => {
        this.context.lastResponse = None
        ""
      }
    }
  }
}

object Bot {
  def apply(categories:List[Category]):Bot = new Bot(categories.sorted)

  def apply(fileName:String):Bot = apply(AIMLParser(fileName))

  def apply(inputStream:InputStream):Bot = apply(AIMLParser(inputStream))

  def apply(categories:List[Category], context:BotContext):Bot = new Bot(categories, context)

  def fromFileNames(fileNames:List[String]):Bot = apply(fileNames.flatMap({case fileName =>
    AIMLParser(fileName)
  }))

  def normalize(input:String) = input.replaceAll("[^a-zA-Z 0-9]", "").replaceAll("\\s", " ")

  def split(input:String) = normalize(input).split(" ").filter(_.size>0).toList
}
