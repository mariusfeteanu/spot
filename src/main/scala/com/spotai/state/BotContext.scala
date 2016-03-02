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

trait BotContext{
  // The last response of the bot, before this one
  var lastResponse:Option[String]
  // The custom predicates of this bot
  var predicates:Map[String,String]
}

//TODO: This does NOT work well with Akka, rewrite
object BotContext{
  var memoryBots:scala.collection.mutable.Map[String, MemoryBotContext] = scala.collection.mutable.Map.empty

  def apply(botContextType:BotContextType, botInstanceId:String):BotContext = {
    botContextType match {
      case MemoryContext => {
        if(memoryBots.contains(botInstanceId)){
          memoryBots(botInstanceId)
        }
        else{
          val memoryBot = MemoryBotContext()
          memoryBots(botInstanceId) = memoryBot
          memoryBot
        }
      }
      case SQLContext => SQLBotContext(botInstanceId)
    }
  }
}
