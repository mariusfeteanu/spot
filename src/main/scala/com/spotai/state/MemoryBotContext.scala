package com.spotai
package state

class MemoryBotContext() extends BotContext {
  var lastResponse:Option[String] = None
  var predicates:Map[String,String] = Map.empty
}

object MemoryBotContext{
  def apply():MemoryBotContext = {
    new MemoryBotContext()
  }
}
