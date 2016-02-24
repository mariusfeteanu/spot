package com.spotai
package state

abstract class BotContext(){
  // The last response of the bot, before this one
  var lastResponse:Option[String]
  // The custom predicates of this bot
  var predicates:Map[String,String]
}
