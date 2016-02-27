package com.spotai
package main

import scala.io.StdIn

import com.spotai.state.SQLBotContext

object Run {
  def main(args:Array[String]):Unit = {

    // https://code.google.com/archive/p/aiml-en-us-foundation-alice/wikis/AIMLFileLoadingOrder.wiki
    val bot = Bot.fromFileNames(List("test.aiml"
    ).map(line => "aiml" + const.sep +line))

    bot.context = SQLBotContext("test")

    var bye = false

    do{
      val userLine = StdIn.readLine("q:")
      userLine match {
        case null => bye = true
        case "bye" => bye = true
        case question:String => println("a:"+bot(question))
      }
    } while (!bye)
    println("bye")
  }
}
