package ko.akka

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import spray.json.DefaultJsonProtocol
//import com.madhukaraphatak.akkahttp.Models.{Customer, ServiceJsonProtoocol}

//import ko.akka.actors.YahooFinance
//import ko.akka.actors.YahooFinance.WonDollar

/**
  * Created by before30 on 16. 4. 11..
  */
case class SlackMessage(msg: String)

object ServiceJsonProtoocol extends DefaultJsonProtocol {
  implicit val customerProtocol = jsonFormat1(SlackMessage)
}
object Application extends App {
  println("hello world")

  import ServiceJsonProtoocol._
  implicit val system = ActorSystem()

  implicit val materializer = ActorMaterializer()
  // needed for the future map/flatmap in the end
  implicit val executionContext = system.dispatcher

  val route =
    path("bot") {
      post {
        entity(as[SlackMessage]) {
          customer => complete {
            s"got customer with name ${customer.msg}"
          }
        }
      }
    }



  Http().bindAndHandle(route, "0.0.0.0", 8080)
  println(s"Server online at http://0.0.0.0:8080/\nPress RETURN to stop...")

}
