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
package actor

import akka.actor.{Actor, Props}
import com.spotai.actor.BotActor.BotChannelAction
import scala.xml.XML
import java.io.InputStream

import com.spotai.state.{BotContext, BotContextType}
import com.spotai.parse.AIMLParser

class BotActor(botContextType:BotContextType, categories:List[Category]) extends Actor{
  import BotActor.BotQuestion

  val bot =  Bot(categories)

  var botContexts:scala.collection.mutable.Map[String, BotContext] = scala.collection.mutable.Map.empty

  def receive = {
    case BotQuestion(question:String, botInstanceId:String) => {
      bot.context = botContexts.getOrElseUpdate(botInstanceId, BotContext(botContextType, botInstanceId))
      sender ! (bot ask question)
    }
    case BotChannelAction(question:String, botInstanceId:String, channel:String, action:((String,String)=>Unit)) =>{
      bot.context = botContexts.getOrElseUpdate(botInstanceId, BotContext(botContextType, botInstanceId))
      action(channel, bot ask question)
    }
    case _ => ???
  }

}

object BotActor {
  case class BotQuestion(question:String, botInstanceId:String)
  case class BotChannelAction(question:String, botInstanceId:String, channel:String, action:(String,String)=>Unit)

  def props(botContextType:BotContextType, categories:List[Category]):Props =
    Props(new BotActor(botContextType, categories))

  // TODO: this duplicates code from Bot, can it not?
  def props(botContextType:BotContextType, fileName:String):Props =
    Props(new BotActor(botContextType, AIMLParser(XML.loadFile(fileName))))

  def props(botContextType:BotContextType, inputStream:InputStream):Props =
    Props(new BotActor(botContextType, AIMLParser(XML.load(inputStream))))
}
