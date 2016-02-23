package com.spotai

class BotContext(){
  // The last response of the bot, before this one
  var lastResponse:Option[String] = None
  // The custom predicates of this bot
  var predicates:Map[String,String] = Map.empty
}

object BotContext{
  def apply():BotContext = {
    new BotContext()
  }
}
