## Description

This is spot the bot. Spot implements a subset of AIML, and some extensions.

It is implemented in Scala and licensed under the GPL v3.

A small subset is implemented at the moment:
- simple patters (i.e. exact sentences)
- star and underscore patterns
- srai
- random elements
- predicates (i.e. storing variables)
- that

Implemented, but no tests yet:
- topic

## Using

Spot is a library that you should use to build your own programs. You can do this using Scala and Java.

You just need to add the [dependency](https://mvnrepository.com/artifact/com.github.mariusfeteanu/spot_2.11/1.0.0) for you environment and you are ready to go.

### Quickstart

The simplest way to start a bot is to give it a filename containing aiml markup:

`val bot = Bot("/test.aiml")`

You can then ask questions of the bot:

`bot ask "HI"`

That's it. As usual the hard part is in building the aiml files.

See `com.spotai.RunBasic` for a simple example.

### Bot context

Certain features require the bot to keep state: last response, predicates (variables) etc.

All context in spot are kept in "context" objects, specifically com.spotai.state.BotContext.

It might seem overkill to factor this out but it is needed to support cases like keeping state in a database or in cookies, to enable long running conversations.

There are two implementations:

- MemoryBotContext - takes no parameters and just keeps the variables in memory. This is the fastest, but also the least useful (program stops - all memory lost).
- SQLBotContext - takes the bot _instance id_ as a parameter. This identifies the current bot, so we can keep multiple instance in one database. You can configure the connection parameters in application.conf, but you have to run `SQLBotContext.setup` to create tables before you use it. We only support sqlite at the moment but support should be easy to add for any major database so please tell me if you need one.

Once you have your context you can just pass it the Bot constructor (MemoryBotContext is the default, so there is no need to ever manually instantiate one).

### Actors

If you need to manage more then one bot at the same time, the easiest way to do so is to use the `com.spotai.actor.BotActor` class. This, used together with the SQLBotContext, can enable you to easily add bots to your responsive system.

This is slightly more complicated than the basic example.

You need to set up an actor system, and give it the bot context as props (this is the state we are controlling access to).

`val botActorSystem = ActorSystem("botActorSystem")`
`val botActor = botActorSystem.actorOf(BotActor.props(MemoryContext), "BotActor")`

You then send a question and instance id in the form of a `BotQuestion` to the actor:

`val responseFuture = botActor?BotQuestion(question, botInstanceId)`

We use BotQuestion objects because in this case the actor represents *ALL* bots in the context, not just a particular instance.

We now have a response future that we can pass to our responsive system, or we can just wait for the response here:

`val response = Await.result(responseFuture, 5 second)`

See `com.spotai.RunActor` for a simple example.

### Slack integration

Basic slack integration is available. The bot can just answer text questions in channels where it's allowed to post.

You need to set the following two environment variables:

- SLACK_BOT_NAME: name of the bot (I might find a way to get this from the token in the future)
- SLACK_AUTH_TOKEN: the slack [authentication token](https://api.slack.com/bot-users).

You then need to create a `com.spotai.integration.slack.SlackAdapter` object. You invoke its `listen` method to start talking, and its `stop` method when you are done.

There is a sample available in the samples directory.

## Playing with it

If you just want to take it for a test drive, a very small aiml file is built-in and an example of Main function are included.

It is easier to just clone the repository for this.

You need:

1. Scala: http://www.scala-lang.org/download/
2. Sbt: http://www.scala-sbt.org/download.html

Steps to test:

1. Edit build.sbt to include the sample code by uncommenting this line:

`unmanagedSourceDirectories in Compile += baseDirectory.value / "src" / "sample"`

1. Include one of the available main classes by uncommenting one of the following:

  1. For basic functionality: `mainClass in Compile  := Some("com.spotai.sample.basic.RunBasic")`

  1. For actor based (try `ask new_bot_name` at the console when testing): `mainClass in Compile  := Some("com.spotai.sample.basic.RunActor")`

  1. For slack integration (credentials need to be supplied): `mainClass in Compile  := Some("com.spotai.sample.integration.RunSlack")`

2. ```sbt run```
3. Chat with spot (it only knows how to say hi and remember a variable called X, if you don't add more aiml files)
4. It will also report the time the response took, in microseconds
5. Say bye to close

## Contributing

As above you need scala and sbt to develop spot. Once you have that setup and the repository cloned, you should run the tests:

`sbt test`

The main need we have for the foreseeable future are _tests_.

If you are a scala developer then just do a pull request with tests (even if they don't pass at the moment).

Otherwise just send me a message with what is not working, with the following info:
- aiml files you used
- question you asked
- response given
- response expected and why, preferably a reference to a spec

## License

The license is GPL v3, see LICENSE.TXT file.

If you need the code released under any other license please contact me.
