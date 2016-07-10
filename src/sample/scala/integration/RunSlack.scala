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
package integration

import com.spotai.state.MemoryContext
import com.spotai.integration.slack.SlackAdapter

object RunSlack {

  def main(args:Array[String]):Unit = {

    val slackAdapter = SlackAdapter(MemoryContext,
      getClass.getResourceAsStream("/test.aiml"))

    slackAdapter.listen()

    println("Connected to slack now.")

    while(true){
      Thread sleep 1000
    }

  }
}
