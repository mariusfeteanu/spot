// scalastyle:off
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
package main

import scala.io.StdIn
import scala.concurrent.duration._
import scala.concurrent.Await
import scala.language.postfixOps
import scala.util.Random
import math.max

import akka.actor.{ActorSystem, Props, Actor}
import akka.pattern.ask
import akka.util.Timeout

import com.spotai.actor.BotActor
import com.spotai.actor.BotActor.BotQuestion
import com.spotai.state.{MemoryContext, SQLContext, BotContextType}

object RunActor {

  def prettypln(line:String) = {
    val rand = new Random()
    for(c <- line){
      print(c)
      val delay = (max((1.0 + rand.nextGaussian()), 0.0)*50).toLong
      Thread.sleep(delay)
    }
    print("\n")
  }

  def main(args:Array[String]):Unit = {

    if(args.length == 1 && args(0) == "setup"){
      com.spotai.state.SQLBotContext.setup()
      return
    }

    implicit val timeout = Timeout(5 seconds)
    val botActorSystem = ActorSystem("botActorSystem")
    val botActor = botActorSystem.actorOf(BotActor.props(MemoryContext, getClass.getResourceAsStream("/test.aiml")), "BotActor")

    var bye = false
    var botInstanceId = "Spot".toLowerCase
    prettypln(s"a:talking to $botInstanceId now (in actor mode)")

    do{
      val userLine = StdIn.readLine("q:")
      userLine match {
        case null => bye = true
        case "bye" => bye = true
        case question:String if question.startsWith("ask ") => {
          botInstanceId = question.drop(4).toLowerCase
          prettypln(s"a:talking to $botInstanceId now (in actor mode)")
        }
        case question:String => {
          val t1 = System.nanoTime()
          val responseFuture = botActor?BotQuestion(question, botInstanceId)
          val response = Await.result(responseFuture, 5 second)
          val t2 = System.nanoTime()
          val duration = java.text.NumberFormat.getIntegerInstance().format((t2-t1)/1000)
          prettypln(s"a:$response [$duration mcs]")
        }
      }
    } while (!bye)
    prettypln("bye")
    botActorSystem.terminate()
  }
}
