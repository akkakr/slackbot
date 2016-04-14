package ko.akka

import akka.actor.ActorSystem
import ko.akka.actors.YahooFinance
import ko.akka.actors.YahooFinance.WonDollar

//import ko.akka.actors.YahooFinance
//import ko.akka.actors.YahooFinance.WonDollar

/**
  * Created by before30 on 16. 4. 11..
  */
object Application extends App {
  println("hello world")

  implicit val system = ActorSystem()
//  val incomingWebhook = system.actorOf(IncomingWebhook.props(ConfigFactory.load().getConfig("app").getString("incoming-slack-url")))
//  incomingWebhook ! Message("hello world!!!")

  val yahooFinance = system.actorOf(YahooFinance.props)
  yahooFinance ! WonDollar("1", "1")
}
