package ko.akka

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import ko.akka.actors.IncomingWebhook
import ko.akka.actors.IncomingWebhook.Message
import ko.akka.actors.commands.GoogleActor.Search
import ko.akka.actors.commands.{GoogleActor, CommandSupervisor}
import ko.akka.actors.commands.CommandSupervisor.Help
import spray.json.DefaultJsonProtocol

//import com.madhukaraphatak.akkahttp.Models.{Customer, ServiceJsonProtoocol}

//import ko.akka.actors.YahooFinance
//import ko.akka.actors.YahooFinance.WonDollar

/**
  * Created by before30 on 16. 4. 11..
  */
case class SlackMessage(msg: String)
case class SlackCommand(user_name: String, text: String)

object ServiceJsonProtoocol extends DefaultJsonProtocol {
  implicit val customerProtocol = jsonFormat1(SlackMessage)
}
object Application extends App {
  implicit val system = ActorSystem()

  implicit val materializer = ActorMaterializer()
  // needed for the future map/flatmap in the end
  implicit val executionContext = system.dispatcher
  val incomingWebhook = system.actorOf(IncomingWebhook.props(ConfigFactory.load().getConfig("app").getString("incoming-slack-url")))

  val commandActor = system.actorOf(CommandSupervisor.props, "CommandSupervisor")
  val googleActor = system.actorOf(GoogleActor.props, "GoogleActor");

//  val route =
//    path("bot") {
//      post {
//        entity(as[SlackMessage]) {
//          message => complete {
//            incomingWebhook ! Message(message.msg)
//            s"got customer with name ${message.msg}"
//          }
//        }
//      }
//    }
  val route =
    path("command") {
      post {
        formFieldMap { fields =>
//          def formFieldString(formField: (String, String)): String =
//            s"""${formField._1} = '${formField._2}'"""
//          complete(s"The form fields are ${fields.map(formFieldString).mkString(", ")}")
          complete {

            val user_name = fields.getOrElse("user_name", "")
            val text = fields.getOrElse("text", "")

            val commands: List[String] = text.split(" ").toList

            commands.head match {
              case s: String if s == "help" =>
                commandActor ! Help
              case s: String if s == "search" =>
                commandActor ! Search(commands.tail.mkString(" "))
              case _ =>
            }

            incomingWebhook ! Message(text = text)
            s"user_name = $user_name\ntext = $text"
          }
        }
      }
    }

  val port = System.getenv("PORT").toInt
  print(port)

  Http().bindAndHandle(route, "0.0.0.0", port)
  println(s"Server online at http://0.0.0.0:" + port +"/\nPress RETURN to stop...")

}
