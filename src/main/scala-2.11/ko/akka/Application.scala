package ko.akka

import akka.actor.ActorSystem
import ko.akka.actors.IncomingWebhook
import ko.akka.actors.IncomingWebhook.Message

import scala.concurrent.ExecutionContext

/**
  * Created by before30 on 16. 4. 11..
  */
object Application extends App {
  println("hello world")


  implicit val system = ActorSystem()
  val incomingWebhook = system.actorOf(IncomingWebhook.props("https://hooks.slack.com/services/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"))
  incomingWebhook ! Message("hello world!!!")

}
