package com.spotai.integration.slack

import java.io.InputStream

import akka.actor.ActorSystem
import akka.util.Timeout
import akka.pattern.ask
import com.spotai.parse.AIMLParser
import com.ullink.slack.simpleslackapi.SlackSession
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener
import scala.concurrent.duration._
import scala.language.postfixOps

import com.spotai.Category
import com.spotai.integration.Adapter
import com.spotai.actor.BotActor
import com.spotai.actor.BotActor.BotChannelAction
import com.spotai.state.BotContextType

import scala.xml.XML

/**
 * Created by marius on 10/07/2016.
 */
class SlackAdapter(authToken:String,
                   botName:String,
                   botContextType:BotContextType,
                   categories:List[Category])
  extends Adapter {

  implicit val timeout = Timeout(5 seconds)

  val botActorSystem = ActorSystem("botActorSystem")
  val botActor = botActorSystem.actorOf(BotActor.props(botContextType, categories), "BotActor")

  val session = SlackSessionFactory.createWebSocketSlackSession(authToken)

  def action(channel:String, message:String):Unit = {
    session.sendMessage(session.findChannelById(channel), message)
  }

  val slackMessagePostedListener = new SlackMessagePostedListener() {
    override def onEvent(event: SlackMessagePosted, session: SlackSession): Unit = {
      if(event.getSender.getUserName != botName)
        botActor?BotChannelAction(event.getMessageContent,
          botName,
          event.getChannel.getId,
          action)
    }
  }

  def listen():Unit = {
    session.connect()
    session.addMessagePostedListener(slackMessagePostedListener)
  }

  def stop() = {
    session.disconnect()
  }

}

object SlackAdapter {
  val authToken = sys.env("SLACK_AUTH_TOKEN")
  val botName = sys.env("SLACK_BOT_NAME")

  def apply(botContextType:BotContextType, categories:List[Category]):SlackAdapter =
    new SlackAdapter(authToken, botName, botContextType, categories)

  // TODO: this duplicates code from Bot, can it not?
  def apply(botContextType:BotContextType, fileName:String):SlackAdapter =
    new SlackAdapter(authToken, botName, botContextType, AIMLParser(XML.loadFile(fileName)))

  def apply(botContextType:BotContextType, inputStream:InputStream):SlackAdapter =
    new SlackAdapter(authToken, botName, botContextType, AIMLParser(XML.load(inputStream)))
}
