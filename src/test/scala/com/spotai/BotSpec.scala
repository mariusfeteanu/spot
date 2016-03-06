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

import org.scalatest._
import org.scalatest.prop.TableDrivenPropertyChecks._

import com.spotai.state.MemoryBotContext

class BotSpec extends FeatureSpec with GivenWhenThen with Matchers{
  feature("Simple pattern matching") {
    scenario("A brain with a single simple category"){
      Given("a bot with single simple category")
      val bot = Bot(getClass.getResourceAsStream("/brains/simple1.aiml"))

      When("it is given a simple question: 'HI'")
      val response = bot ask "HI"

      Then("the bot should give a simple response: 'HELLO'")
      response shouldBe "HELLO"

      When("it is given a simple unknown question: 'BYE'")
      val response2 = bot ask "BYE"

      Then("the bot should give an empty response: ''")
      response2 shouldBe ""
    }
  }
}
