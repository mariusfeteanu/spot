package com.spotai
package state

class SQLLiteBotContext() extends BotContext {
  private var _lastResponse:Option[String] = None
  private var _predicates:Map[String,String] = Map.empty

  override def lastResponse:Option[String] = {_lastResponse}
  override def lastResponse_=(lastResponse:Option[String]) = {_lastResponse = lastResponse}

  override def predicates:Map[String,String] = {_predicates}
  override def predicates_=(predicates:Map[String,String]) = {_predicates = predicates}
}

object SQLLiteBotContext{
  def apply():SQLLiteBotContext = {
    new SQLLiteBotContext()
  }
}
