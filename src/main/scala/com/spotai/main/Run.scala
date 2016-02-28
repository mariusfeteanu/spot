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

import akka.actor.{ActorSystem, Props, Actor}
import akka.pattern.ask
import akka.util.Timeout

import com.spotai.actor.BotActor

object Run {
  def main(args:Array[String]):Unit = {

    implicit val timeout = Timeout(5 seconds)
    val botActorSystem = ActorSystem("botActorSystem")
    val botActor = botActorSystem.actorOf(Props[BotActor], "BotActor")

    var bye = false

    do{
      val userLine = StdIn.readLine("q:")
      userLine match {
        case null => bye = true
        case "bye" => bye = true
        case question:String => {
          val response = Await.result(botActor?question, 5 second)
          println("a:"+response)
        }
      }
    } while (!bye)
    println("bye")
    botActorSystem.shutdown()
  }
}
