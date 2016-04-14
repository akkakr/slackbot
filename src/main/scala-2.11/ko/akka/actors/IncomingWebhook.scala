package ko.akka.actors

import akka.actor.Actor.Receive
import akka.actor.{ActorSystem, Actor, Props}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import ko.akka.actors.IncomingWebhook.Message
import spray.json.DefaultJsonProtocol

import scala.concurrent.ExecutionContext
import scala.util.parsing.json.{JSONObject, JSONArray}
import spray.json._

/**
  * Created by before30 on 16. 4. 11..
  */

object IncomingWebhook {
  sealed trait IncomingWebhookMessage

  case class Message(text: String) extends IncomingWebhookMessage


  def props(url: String) = {
    Props(classOf[IncomingWebhook], url)
  }
}


trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val messageFormat = jsonFormat1(Message)
}

class IncomingWebhook(url: String) extends Actor with JsonSupport {
  import akka.http.scaladsl.Http
  import akka.http.scaladsl.model._
  import akka.stream.ActorMaterializer
  import scala.concurrent.Future
  import ExecutionContext.Implicits.global

  implicit val system = context.system
  implicit val materializer = ActorMaterializer()

  override def receive: Receive = {
    case message: Message => {
      val jsValue = messageFormat.write(message)

      val entity = HttpEntity.apply(ContentType(MediaTypes.`application/json`), jsValue.compactPrint)
      val responseFuture: Future[HttpResponse] =
        Http().singleRequest(HttpRequest(entity = entity ,uri = url, method = HttpMethods.POST))

      responseFuture.onSuccess({
        case s => println(s)
        case _ =>
      })
    }
    case _ => {

    }
  }
}

