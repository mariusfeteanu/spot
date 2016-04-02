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

  feature("Wild card pattern matching") {
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

      When("it is given a specific question: 'BYE THERE'")
      val response5 = bot ask "BYE THERE"
      Then("the bot should give the default response: 'THIS IS THE NEVER RESPONSE.'")
      response5 shouldBe "THIS IS THE NEVER RESPONSE."
    }

    scenario("A brain with a few simple wildcard (_) patterns (/bot/simple_wildcard2.aiml)"){
      Given("a bot with a few wildcard patterns (_)")
      val bot = Bot(getClass.getResourceAsStream("/bot/simple_wildcard2.aiml"))

      When("it is given a specific question (matched by as well): 'HI THERE'")
      val response1 = bot ask "HI THERE"
      Then("the bot should give the specific response: 'THIS IS THE SECOND RESPONSE.'")
      response1 shouldBe "THIS IS THE SECOND RESPONSE."

      When("it is given another question: 'HI AGAIN THERE'")
      val response2 = bot ask "HI AGAIN THERE"
      Then("the bot should give the response: 'THIS IS THE SECOND RESPONSE.'")
      response2 shouldBe "THIS IS THE SECOND RESPONSE."

      When("it is given another question: 'THIS IS A HI'")
      val response3 = bot ask "THIS IS A HI"
      Then("the bot should give the response: 'THIS IS THE THIRD RESPONSE.'")
      response3 shouldBe "THIS IS THE THIRD RESPONSE."

      When("it is given a specific question: 'BYE THERE'")
      val response5 = bot ask "BYE THERE"
      Then("the bot should give the default response: 'THIS IS THE NEVER RESPONSE.'")
      response5 shouldBe "THIS IS THE NEVER RESPONSE."
    }
    // http://www.vrconsulting.it/VHF/topic.asp?ARCHIVE=true&TOPIC_ID=829
    // http://aiml.1580448.n4.nabble.com/wildcard-vs-td2019299.html
    scenario("A brain with a mix wildcard (_,*) patterns (/bot/wildcard_precedence1.aiml)"){
      Given("a bot with a mix wildcard (_,*) patterns")
      val bot = Bot(getClass.getResourceAsStream("/bot/wildcard_precedence1.aiml"))

      When("A question matching both * and _: 'Science is just great'")
      val response1 = bot ask "Science is just great"
      Then("the bot should give the response: 'UNDSERSCORE MATCHED.'")
      response1 shouldBe "UNDSERSCORE MATCHED."

      When("A question matching exactly a pattern: 'Internet is just great'")
      val response2 = bot ask "Internet is just great"
      Then("the bot should give the response: 'UNDSERSCORE MATCHED.'")
      response2 shouldBe "UNDSERSCORE MATCHED."

      When("A question matching exactly a mixed pattern: 'My house is just to small'")
      val response3 = bot ask "My house is just to small"
      Then("the bot should give the response: 'MIXED PATTERN MATCHED.'")
      response3 shouldBe "MIXED PATTERN MATCHED."
    }
  }

  feature("Matching using the <THAT> element.") {
    scenario("A brain with a simple <that> redirect (/bot/that.aiml)"){
      Given("a bot with a <that> redirect")
      val bot = Bot(getClass.getResourceAsStream("/bot/that.aiml"))

      When("It is given a question not matched by <that>: 'YES'")
      val response1 = bot ask "YES"
      Then("it gives a default response: 'Yes, what?'")
      response1 shouldBe "Yes, what?"

      When("It is given a question matched by <that>: 'NO'")
      val that = bot ask "How about pizza?" // setup the <that> element
      val response2 = bot ask "NO"
      Then("it gives a default response: 'That's fine, we can have something other than pepperoni'")
      that shouldBe "Do you like pepperoni?"
      response2 shouldBe "That's fine, we can have something other than pepperoni"
    }
  }

  feature("Remembering variables with <set> and <get>.") {
    scenario("A brain with a simple <that> redirect (/bot/set_get.aiml)"){
      Given("a bot with <set> and <get> elements.")
      val bot = Bot(getClass.getResourceAsStream("/bot/set_get.aiml"))

      When("Asked the value of a predicate it doesn't know: 'What is x?'")
      val response1 = bot ask "What is x?"
      Then("it gives a default response: 'X is .'")
      response1 shouldBe "X is ."

      When("When told the value of a variable: 'x is blue'")
      val response2 = bot ask "x is blue"
      Then("it should aknowledge the set value: 'Okay, X is blue.'")
      response2 shouldBe "Okay, X is blue."

      When("When told the value of a variable: 'y is red'")
      val response3 = bot ask "y is blue"
      Then("it should aknowledge the set value: 'Okay, Y is red.'")
      response3 shouldBe "Okay, Y is blue."
      Then("it should still remember the value set the first time")
      val response4 = bot ask "what is x?"
      response4 shouldBe "X is blue."
      Then("it should also know the one set now")
      val response5 = bot ask "what is y?"
      response5 shouldBe "Y is blue."
    }
  }

}
