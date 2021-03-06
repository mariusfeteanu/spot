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
package com.spotai.sample
package basic

import scala.io.StdIn
import scala.util.Random
import math.max

import com.spotai.Bot

object RunBasic {

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

    val bot = Bot(getClass.getResourceAsStream("/test.aiml"))

    var bye = false
    prettypln(s"a:talking to spot now (in basic mode)")

    do{
      val userLine = StdIn.readLine("q:")
      userLine match {
        case null => bye = true
        case "bye" => bye = true
        case question:String if question.startsWith("ask ") => {
          prettypln(s"a:in basic mode, only spot is available")
        }
        case question:String => {
          val t1 = System.nanoTime()
          val response = (bot ask question)
          val t2 = System.nanoTime()
          val duration = java.text.NumberFormat.getIntegerInstance().format((t2-t1)/1000)
          prettypln(s"a:$response [$duration mcs]")
        }
      }
    } while (!bye)
    prettypln("bye")
  }
}
