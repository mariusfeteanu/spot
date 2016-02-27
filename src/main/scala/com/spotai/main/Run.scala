package com.spotai
package main

import scala.io.StdIn

import com.spotai.state.SQLBotContext

object Run {
  def main(args:Array[String]):Unit = {

    // https://code.google.com/archive/p/aiml-en-us-foundation-alice/wikis/AIMLFileLoadingOrder.wiki
    val bot = Bot.fromFileNames(List(
      // "reduction.names.aiml",
      // "reduction0.safe.aiml",
      // "reduction1.safe.aiml",
      // "reduction2.safe.aiml",
      // "reduction3.safe.aiml",
      // "reduction4.safe.aiml",
      // "reductions-update.aiml",
      //
      // "mp0.aiml",
      // "mp1.aiml",
      // "mp2.aiml",
      // "mp3.aiml",
      // "mp4.aiml",
      // "mp5.aiml",
      // "mp6.aiml",
      //
      // "ai.aiml",
      // "alice.aiml",
      // "astrology.aiml",
      // "atomic.aiml",
      // "badanswer.aiml",
      // "biography.aiml",
      // "bot.aiml",
      // "bot_profile.aiml",
      // "client.aiml",
      // "client_profile.aiml",
      // "computers.aiml",
      // "continuation.aiml",
      // "date.aiml",
      "default.aiml"//,
      // "drugs.aiml",
      // "emotion.aiml",
      // "food.aiml",
      // "geography.aiml",
      // "gossip.aiml",
      // "history.aiml",
      // "humor.aiml",
      // "imponderables.aiml",
      // "inquiry.aiml",
      // "interjection.aiml",
      // "iu.aiml",
      // "knowledge.aiml",
      // "literature.aiml",
      // "loebner10.aiml",
      // "money.aiml",
      // "movies.aiml",
      // "music.aiml",
      // "numbers.aiml",
      // "personality.aiml",
      // "phone.aiml",
      // "pickup.aiml",
      // "politics.aiml",
      // "primeminister.aiml",
      // "primitive-math.aiml",
      // "psychology.aiml",
      // "pyschology.aiml",
      // "religion.aiml",
      // "salutations.aiml",
      // "science.aiml",
      // "sex.aiml",
      // "sports.aiml",
      // "stack.aiml",
      // "stories.aiml",
      // "that.aiml",
      // "wallace.aiml",
      // "xfind.aiml",
      // "update1.aiml"
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
