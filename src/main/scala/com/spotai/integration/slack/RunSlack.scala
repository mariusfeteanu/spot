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
package com.spotai.integration
package slack

import com.spotai.Bot
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener
import com.ullink.slack.simpleslackapi.SlackSession
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory

object RunSlack {

  def main(args:Array[String]):Unit = {

    val bot = Bot(getClass.getResourceAsStream("/test.aiml"))

    val slackMessagePostedListener = new SlackMessagePostedListener() {
      override def onEvent(event: SlackMessagePosted, session: SlackSession): Unit = {
        if(event.getSender.getUserName != sys.env("SLACK_BOT_NAME"))
          session.sendMessage(event.getChannel, bot ask event.getMessageContent)
      }
    }

    val session = SlackSessionFactory.createWebSocketSlackSession(sys.env("SLACK_AUTH_TOKEN"))
    session.connect()
    println("Connected to slack.")

    session.addMessagePostedListener(slackMessagePostedListener)

    while(true){
      Thread sleep 1000
    }

  }
}
