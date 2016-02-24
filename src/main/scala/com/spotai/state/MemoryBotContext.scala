package com.spotai
package state

class MemoryBotContext() extends BotContext {
  // The last response of the bot, before this one
  var lastResponse:Option[String] = None
  // The custom predicates of this bot
  var predicates:Map[String,String] = Map.empty
}

object MemoryBotContext{
  def apply():MemoryBotContext = {
    new MemoryBotContext()
  }
}
