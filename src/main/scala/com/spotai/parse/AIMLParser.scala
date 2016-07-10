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
package parse

import com.spotai.pattern._
import com.spotai.template._

import scala.xml.{XML, Node, Elem, Text}
import java.io.InputStream

class AIMLParser(xmlAIML:Elem) {
    // The node name ("label") indicates the type template element to create
    private def parseTemplateSimpleElement(wordNode:Node) = {
      wordNode.label match {
        // A <star/> node indicates that the element should be replaced by the star pattern
        case "star" => List(TemplateStar())
        case "get"  => List(TemplateGetName((wordNode \ "@name").text))
        // Otherwise we assume it's text
        case _ => wordNode.text match {
          case text if text.trim =="" => Nil
          case text => List(TemplateWord(text))
        }
      }
    }

    // The name of the node indicates which type of advanced element we create
    private def parseTemplateCompoundElement(nodeElem:Node) = {
      nodeElem.label match {
        // The srai is a template itself so we pass it as such now
        case "srai"   => List(Srai(parseTemplate( nodeElem )))
        case "set"    => List(TemplateSetName((nodeElem \ "@name").text, parseTemplate( nodeElem )))
        case "random" => List(TemplateRandom((nodeElem \ "li").map({
          case li => Template(parseTemplate(li))
        }).toList))
        // This means the element type is not implemented
        case _ => Nil
      }
    }

    /* Parse a template xml node */
    private def parseTemplate(template:Node):List[TemplateElement] = {
      // Go though all the children of the template node, each one contains a type of template
      template.nonEmptyChildren.flatMap({
        // If the current node has no children then it gets parsed itself
        case wordNode:Node if wordNode.nonEmptyChildren.size == 0
          => parseTemplateSimpleElement(wordNode)
        // If the current node has children then it needs to be instantied to a complex template element
        case nodeElem:Node
          => parseTemplateCompoundElement(nodeElem)
        // Not sure if this ever happens
        case _ => Nil
        }).map({ // We go once more through the template elements to make sure they have the right class
          case templateElement:TemplateElement => templateElement
          case _ => throw new RuntimeException("this never happens")
        }).toList // to List because it's easier to qork qith than some Seq
    }

    /* Loads a category xml node */
    private def parseCategory(category:Node) = {
      Category(
        new Pattern( (category \ "pattern").text),
        new Template(parseTemplate( (category \ "template").head)),
        ((category \ "that").text) match {
          case "" => None
          case that:String => Some(new Pattern(that))},
        None
      )
    }

    /*
    Parses an XML and loads categories from it
    */
    def apply():List[Category] = {
      /* Categories without topics */
    (xmlAIML \ "category").map({case category => parseCategory(category)}).toList :::
    /* Cateories with topics */
    (xmlAIML \ "topic").flatMap({topic =>
      (topic  \ "category").map({case category => parseCategory(category)})
    }).toList
    }
}

object AIMLParser {
  def apply(xmlAIML:Elem) =
    (new AIMLParser(xmlAIML))()

  def apply(fileName:String) =
    (new AIMLParser(XML.loadFile(fileName)))()

  def apply(inputStream:InputStream) =
    (new AIMLParser(XML.load(inputStream)))()
}
