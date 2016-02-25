package com.spotai
package state

import scala.language.postfixOps // This is so we can write 30 seconds for duration
import scala.concurrent.{Future, Await}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

import slick.backend.DatabasePublisher
import slick.driver.SQLiteDriver.api._
import slick.lifted.ProvenShape.proveShapeOf

class SQLBotContext(dbConnectionString:String,
                    dbDriver:String,
                    botInstanceId:String) extends BotContext {


  class BotLastResponse(tag: Tag) extends Table[(String, String)](tag, "bot_last_response") {
      def id = column[String]("bot_id", O.PrimaryKey)
      def lastResponse = column[String]("last_response")
      def * = proveShapeOf(id, lastResponse)
    }

  val botLastResponse = (TableQuery[BotLastResponse])

  class Predicate(tag: Tag) extends Table[(String, String, String)](tag, "bot_predicate") {
      def id = column[String]("bot_id", O.PrimaryKey)
      def predicateName = column[String]("name")
      def predicateValue = column[String]("value")
      def * = proveShapeOf(id, predicateName, predicateValue)
    }

  val predicate = (TableQuery[Predicate])

  val db = Database.forConfig("botSQL")
  try {
    val setupAction = DBIO.seq((botLastResponse.schema ++ predicate.schema).create)
    val setupFuture: Future[Unit] = db.run(setupAction)
    Await.result(setupFuture, 30 seconds)
  } finally db.close

  override def lastResponse:Option[String] = {
    val db = Database.forConfig("botSQL")
    try {
      val q = botLastResponse.filter(_.id === botInstanceId).map(_.lastResponse).take(1)
      val action = q.result
      val result = db.run(action)
      val returned = action.statements.headOption
      returned
    } finally db.close
  }

  override def lastResponse_=(lastResponse:Option[String]) = {
    val db = Database.forConfig("botSQL")
    try {
      lastResponse match {
        case Some(someResponse:String) => botLastResponse.insertOrUpdate((botInstanceId, someResponse))
        case None => botLastResponse.filter(_.id === botInstanceId).delete
      }
    } finally db.close
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
