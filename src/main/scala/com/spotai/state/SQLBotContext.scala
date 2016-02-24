package com.spotai
package state

import slick.driver.SQLiteDriver.api._
import scala.concurrent.ExecutionContext.Implicits.global
import slick.lifted.ProvenShape.proveShapeOf

class SQLBotContext(dbConnectionString:String,
                    dbDriver:String,
                    botInstanceId:String) extends BotContext {


  class BotInstance(tag: Tag) extends Table[(String, String)](tag, "bot_instance") {
      def id = column[String]("bot_id", O.PrimaryKey)
      def lastResponse = column[String]("last_response")
      def * = proveShapeOf(id, lastResponse)
    }

  class Predicate(tag: Tag) extends Table[(String, String, String)](tag, "bot_predicate") {
      def id = column[String]("bot_id", O.PrimaryKey)
      def predicateName = column[String]("name")
      def predicateValue = column[String]("value")
      def * = proveShapeOf(id, predicateName, predicateValue)
    }

  override def lastResponse:Option[String] = {
    Some("?")
  }
  override def lastResponse_=(lastResponse:Option[String]) = {
  }

  override def predicates:Map[String,String] = {
    Map.empty
  }
  override def predicates_=(predicates:Map[String,String]) = {
  }
}

object SQLBotContext{
  def apply(dbConnectionString:String,
            dbDriver:String,
            botInstanceId:String):SQLBotContext = {
    new SQLBotContext(dbConnectionString, dbDriver, botInstanceId)
  }
}
