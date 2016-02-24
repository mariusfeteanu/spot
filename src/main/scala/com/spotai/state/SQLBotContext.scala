package com.spotai
package state

import scala.concurrent.ExecutionContext.Implicits.global

import slick.driver.SQLiteDriver.api._
import slick.lifted.ProvenShape.proveShapeOf

class SQLBotContext(dbConnectionString:String,
                    dbDriver:String,
                    botInstanceId:String) extends BotContext {


  class BotLastResponse(tag: Tag) extends Table[(String, String)](tag, "bot_last_respons") {
      def id = column[String]("bot_id", O.PrimaryKey)
      def lastResponse = column[String]("last_response")
      def * = proveShapeOf(id, lastResponse)
    }

  val botLastResponse = (TableQuery[BotLastResponse]).filter(_.id === botInstanceId)

  class Predicate(tag: Tag) extends Table[(String, String, String)](tag, "bot_predicate") {
      def id = column[String]("bot_id", O.PrimaryKey)
      def predicateName = column[String]("name")
      def predicateValue = column[String]("value")
      def * = proveShapeOf(id, predicateName, predicateValue)
    }

  val predicate = (TableQuery[Predicate]).filter(_.id === botInstanceId)

  DBIO.seq((botLastResponse.schema ++ predicate.schema).create)

  override def lastResponse:Option[String] = {
    val db = Database.forConfig("botSQL")
    try {
      val q = botLastResponse.map(_.lastResponse).take(1)
      val action = q.result
      val result = db.run(action)
      val returned = action.statements.headOption
      returned
    } finally {
      db.close
    }
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
