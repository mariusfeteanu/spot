package com.spotai

import scala.xml.{XML, Node, Elem, Text}

/*
This class implements part of the AIML language.
You can build from an xml specification, and the interrogate the both with the apply metod.
botInstance("your question here")
*/
class Bot(categories:List[Category]){
  val context = BotContext()

  /*
  Main bot method, provides a response to a stimulus
  */
  def apply(input:String):String = {
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
        case Some(topic:Pattern) => topic(input.split(" "), this.context, patternContext).isDefined
      }) &&
      // We check that the actual pattern matches
      (category.stimulus(input.split(" "), this.context, patternContext) match {
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
        case Some(that:Pattern) => context.lastResponse match {
          // But we have no last response to check against (maybe first question)
          case None => false
          // Check that the last response actually matches the <that/> filter
          case Some(someResponse:String) => that(someResponse.split(" "), this.context, patternContext).isDefined
        }
      })

    }) match {
      case Some(category:Category) =>{
        // We save the current response, and use it to match <that/> next time
        val response = category.response(this, patternContext)
        context.lastResponse = Some(response)
        response
      }
      case None => ""
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
              // Otherwise we assume it's text
              case _ => wordNode.text.split(" ").map({case templateWord:String => TemplateWord(templateWord)}).toList}
          // If the current node has children then it needs to be instantied to a complex template element
          case nodeElem:Node
            // The name of the node indicates which type of advanced element we create
            => nodeElem.label match {
              // The srai is a template itself so we pass it as such now
              case "srai" => List(new Srai(parseTemplate( nodeElem )))
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
