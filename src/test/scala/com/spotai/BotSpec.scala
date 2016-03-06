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

class BotSpec extends FeatureSpec with GivenWhenThen with Matchers{

  feature("Simple pattern matching") {
    scenario("A brain with a single simple category (/bot/simple1.aiml)"){
      Given("a bot with a single simple category")
      val bot = Bot(getClass.getResourceAsStream("/bot/simple1.aiml"))

      When("it is given a simple question: 'HI'")
      val response = bot ask "HI"
      Then("the bot should give a simple response: 'HELLO'")
      response shouldBe "HELLO"

      When("it is given a simple unknown question: 'BYE'")
      val response2 = bot ask "BYE"
      Then("the bot should give an empty response: ''")
      response2 shouldBe ""
    }
    scenario("A brain with two simple categories (/bot/simple2.aiml)"){
      Given("a bot with two simple categories")
      val bot = Bot(getClass.getResourceAsStream("/bot/simple2.aiml"))

      When("it is given one simple question: 'HI'")
      val response1 = bot ask "HI"
      Then("the bot should give a simple response: 'HELLO'")
      response1 shouldBe "HELLO"

      When("it is given one simple question: 'HI THERE'")
      val response2 = bot ask "HI THERE"
      Then("the bot should give a simple response: 'HELLO THERE'")
      response2 shouldBe "HELLO THERE"

      When("it is given one simple unknown question: 'BYE'")
      val response3 = bot ask "BYE"
      Then("the bot should give an empty response: ''")
      response3 shouldBe ""

      When("it is given one simple unknown question: 'BYE THERE'")
      val response4 = bot ask "BYE THERE"
      Then("the bot should give an empty response: ''")
      response4 shouldBe ""

      When("it is given one simple unknown question: 'HI HERE'")
      val response5 = bot ask "HI HERE"
      Then("the bot should give an empty response: ''")
      response5 shouldBe ""

      When("it is given one simple unknown question: 'GOODBYE HERE'")
      val response6 = bot ask "GOODBYE HERE"
      Then("the bot should give an empty response: ''")
      response6 shouldBe ""
    }
  }

  feature("Simple wild card pattern matching") {
    scenario("A brain with a few simple wildcard (*) patterns (/bot/simple_wildcard1.aiml)"){
      Given("a bot with a few wildcard patterns (*)")
      val bot = Bot(getClass.getResourceAsStream("/bot/simple_wildcard1.aiml"))

      When("it is given a specific question: 'HI THERE'")
      val response1 = bot ask "HI THERE"
      Then("the bot should give the specific response: 'THIS IS THE FIRST RESPONSE.'")
      response1 shouldBe "THIS IS THE FIRST RESPONSE."

      When("it is given another question: 'HI AGAIN THERE'")
      val response2 = bot ask "HI AGAIN THERE"
      Then("the bot should give the response: 'THIS IS THE SECOND RESPONSE.'")
      response2 shouldBe "THIS IS THE SECOND RESPONSE."

      When("it is given another question: 'THIS IS A HI'")
      val response3 = bot ask "THIS IS A HI"
      Then("the bot should give the response: 'THIS IS THE THIRD RESPONSE.'")
      response3 shouldBe "THIS IS THE THIRD RESPONSE."

      When("it is given any question: 'ijofshuge;o dbjdsaoih dasbdai'")
      val response4 = bot ask "ijofshuge;o dbjdsaoih dasbdai"
      Then("the bot should give the default response: 'THIS IS THE FOURTH RESPONSE.'")
      response4 shouldBe "THIS IS THE FOURTH RESPONSE."

      When("it is given a question that matches after *: 'BYE THERE'")
      val response5 = bot ask "BYE THERE"
      Then("the bot should give the default response: 'THIS IS THE FOURTH RESPONSE.'")
      response5 shouldBe "THIS IS THE FOURTH RESPONSE."
    }

    scenario("A brain with a few simple wildcard (_) patterns (/bot/simple_wildcard2.aiml)"){
      Given("a bot with a few wildcard patterns (_)")
      val bot = Bot(getClass.getResourceAsStream("/bot/simple_wildcard2.aiml"))

      When("it is given a specific question: 'HI THERE'")
      val response1 = bot ask "HI THERE"
      Then("the bot should give the specific response: 'THIS IS THE FIRST RESPONSE.'")
      response1 shouldBe "THIS IS THE FIRST RESPONSE."

      When("it is given another question: 'HI AGAIN THERE'")
      val response2 = bot ask "HI AGAIN THERE"
      Then("the bot should give the response: 'THIS IS THE SECOND RESPONSE.'")
      response2 shouldBe "THIS IS THE SECOND RESPONSE."

      When("it is given another question: 'THIS IS A HI'")
      val response3 = bot ask "THIS IS A HI"
      Then("the bot should give the response: 'THIS IS THE THIRD RESPONSE.'")
      response3 shouldBe "THIS IS THE THIRD RESPONSE."

      When("it is given any question: 'ijofshuge;o dbjdsaoih dasbdai'")
      val response4 = bot ask "ijofshuge;o dbjdsaoih dasbdai"
      Then("the bot should give the default response: 'THIS IS THE FOURTH RESPONSE.'")
      response4 shouldBe "THIS IS THE FOURTH RESPONSE."

      When("it is given a question that matches after *: 'BYE THERE'")
      val response5 = bot ask "BYE THERE"
      Then("the bot should give the default response: 'THIS IS THE FOURTH RESPONSE.'")
      response5 shouldBe "THIS IS THE FOURTH RESPONSE."
    }
  }

}