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
import com.spotai.pattern._
import com.spotai.template._

import scala.xml.{XML, Node, Elem, Text}
import java.io.InputStream;

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

  /*
  Main bot method, provides a response to a stimulus
  */
  def apply(input:String):String = {
    /*
    This is variable because we need to re-assign it based on the result of the match
    */
    var patternContext = PatternContext("")
    /* We look through all the configured categories to find one that maches, we check
      - topic
      - question (stimulus, input, pattern whatever)
      - that
      */
      categories.find({category =>
      // TODO: Check that the topic matches, if topic is set
      (category.topic match {
        case None => true
        case Some(topic:Pattern) => topic(input.split(" "), patternContext).isDefined
      }) &&
      // We check that the actual pattern matches
      (category.stimulus(input.split(" "), patternContext) match {
        case None => false
        case Some(matchPatternContext) => {
          patternContext = matchPatternContext
          true
        }
      }) &&
      // If this category has <that> set, then check that it matches the last repsonse
      (category.that match {
        // No last response filter is set
        case None => true
        // We have some last pattern to check
        case Some(that:Pattern) => this.context.lastResponse match {
          // But we have no last response to check against (maybe first question)
          case None => false
          // Check that the last response actually matches the <that/> filter
          case Some(someResponse:String) => that(someResponse.split(" "), patternContext).isDefined
        }
      })

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
  /*
  Parses and XML and loads categories from it
  */
  def categoriesFromXML(xmlAIML:Elem):List[Category] = {
    /* Loads a category xml node */
    def parseCategory(category:Node) = {
      /* Parse a template xml node */
      def parseTemplate(template:Node):List[TemplateElement] = {
        // Go though all the children of the template node, each one contains a type of template
        template.nonEmptyChildren.flatMap({
          // If the current node has no children then it gets parsed itself
          case wordNode:Node if wordNode.nonEmptyChildren.size == 0
            // The node name ("label") indicates the type template element to create
            => wordNode.label match {
              // A <star/> node indicates that the element should be replaced by the star pattern
              case "star" => List(TemplateStar())
              case "get"  => List(TemplateGetName((wordNode \ "@name").text))
              // Otherwise we assume it's text
              case _ => wordNode.text.split(" ").map({case templateWord:String => TemplateWord(templateWord)}).toList}
          // If the current node has children then it needs to be instantied to a complex template element
          case nodeElem:Node
            // The name of the node indicates which type of advanced element we create
            => nodeElem.label match {
              // The srai is a template itself so we pass it as such now
              case "srai"   => List(Srai(parseTemplate( nodeElem )))
              case "set"    => List(TemplateSetName((nodeElem \ "@name").text, parseTemplate( nodeElem )))
              case "random" => List(TemplateRandom((nodeElem \ "li").map({
                case li => Template(parseTemplate(li))
              }).toList))
              // This means the element type is not implemented
              case _ => Nil
          }
          // Not sure if this ever happens
          case _ => Nil
          }).map({ // We go once more through the template elements to make sure they have the right class
            case templateElement:TemplateElement => templateElement
            case _ => throw new RuntimeException("this never happens")
          }).toList // to List because it's easier to qork qith than some Seq
      }

      Category(
        new Pattern( (category \ "pattern").text),
        new Template(parseTemplate( (category \ "template").head)),
        ((category \ "that").text) match {
          case "" => None
          case that:String => Some(new Pattern(that))},
        None
      )
    }
    /* Categories without topics */
  (xmlAIML \ "category").map({case category => parseCategory(category)}).toList :::
  /* Cateories with topics */
  (xmlAIML \ "topic").flatMap({topic =>
    (topic  \ "category").map({case category => parseCategory(category)})
  }).toList
  }

  def apply(categories:List[Category]):Bot = new Bot(categories)

  def apply(fileName:String):Bot = apply(categoriesFromXML(XML.loadFile(fileName)))

  def apply(inputStream:InputStream):Bot = apply(categoriesFromXML(XML.load(inputStream)))

  def apply(categories:List[Category], context:BotContext):Bot = new Bot(categories, context)

  def fromFileNames(fileNames:List[String]):Bot = apply(fileNames.flatMap({case fileName =>
    try {
      categoriesFromXML(XML.loadFile(fileName))
    } catch {
      case ex:Exception => {
        println("Failed to load file " + fileName + ", exception: " + ex.getMessage)
        Nil
      }
    }
  }))
}
