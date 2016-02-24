package com.spotai
package state

abstract class BotContext(){
  var lastResponse:Option[String]
  var predicates:Map[String,String]
}
