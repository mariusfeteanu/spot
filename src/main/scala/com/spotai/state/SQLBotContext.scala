/*
Spot is a bot, implementing a subset of AIML, and some extensions.
Copyright (C) 2016  Marius Feteanu

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.spotai
package state

import scala.language.postfixOps // This is so we can write 30 seconds for duration
import scala.concurrent.{Future, Await}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

import slick.backend.DatabasePublisher
import slick.driver.SQLiteDriver.api._
import slick.lifted.ProvenShape.proveShapeOf

class SQLBotContext(botInstanceId:String) extends BotContext {


  class BotLastResponse(tag: Tag) extends Table[(String, String)](tag, "bot_last_response") {
      def id = column[String]("bot_id", O.PrimaryKey)
      def lastResponse = column[String]("last_response")
      def * = proveShapeOf((id, lastResponse))
    }

  val botLastResponse = (TableQuery[BotLastResponse])

  class Predicate(tag: Tag) extends Table[(String, String, String)](tag, "bot_predicate") {
      def id = column[String]("bot_id")
      def predicateName = column[String]("name")
      def predicateValue = column[String]("value")
      def pk = primaryKey("pk_bot_predicate", (id, predicateName))
      def * = proveShapeOf((id, predicateName, predicateValue))
    }

  val predicate = (TableQuery[Predicate])

  Class.forName("org.sqlite.JDBC")

  def setup() = {
    val db = Database.forConfig("botSQL")
    try {
      val setupAction = DBIO.seq((botLastResponse.schema ++ predicate.schema).create)
      val setupFuture: Future[Unit] = db.run(setupAction)
      Await.result(setupFuture, 30 seconds)
    } finally db.close
  }

  override def lastResponse:Option[String] = {
    val db = Database.forConfig("botSQL")
    try {
      val futureResult = db.run(botLastResponse.filter(_.id === botInstanceId).map(_.lastResponse).result.headOption)
      Await.result(futureResult, 30 seconds)
    } finally db.close
  }

  override def lastResponse_=(lastResponse:Option[String]) = {
    val db = Database.forConfig("botSQL")
    try {
      lastResponse match {
        case Some(someResponse:String) => Await.ready(db.run(botLastResponse.insertOrUpdate((botInstanceId, someResponse))), 30 seconds)
        case None => Await.ready(db.run(botLastResponse.filter(_.id === botInstanceId).delete), 30 seconds)
      }
    } finally db.close
  }

  override def predicates:Map[String,String] = {
    val db = Database.forConfig("botSQL")
    try {
      val futureResult = db.run(predicate.filter(_.id === botInstanceId).map(row => (row.predicateName -> row.predicateValue)).result)
      Await.result(futureResult, 30 seconds).toMap
    } finally db.close
  }
  override def predicates_=(predicates:Map[String,String]) = {
    val db = Database.forConfig("botSQL")
    try {
      predicates.map({case (name, value) =>
        Await.ready(db.run(predicate.insertOrUpdate((botInstanceId, name, value))), 30 seconds)
      })
    } finally db.close
  }
}

object SQLBotContext{
  def apply(botInstanceId:String):SQLBotContext = {
    new SQLBotContext(botInstanceId)
  }
  def setup() = {
    (SQLBotContext("")).setup()
  }
}
