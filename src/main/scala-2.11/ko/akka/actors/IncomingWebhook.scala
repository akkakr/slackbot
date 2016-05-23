package ko.akka.actors

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.RequestEntity
import akka.http.scaladsl.unmarshalling.Unmarshal
import spray.json.DefaultJsonProtocol._
import spray.json._

import akka.actor.Actor.Receive
import akka.actor.{ActorSystem, Actor, Props}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import ko.akka.actors.IncomingWebhook._
import spray.json.DefaultJsonProtocol

import scala.concurrent.ExecutionContext
import scala.util.parsing.json.{JSONObject, JSONArray}
import spray.json._

/**
  * Created by before30 on 16. 4. 11..
  */


object IncomingWebhook {
  sealed trait IncomingWebhookMessage

  case class Message(text: String, username: String = "bot") extends IncomingWebhookMessage
  case class HelpMessage(text: String, username: String = "bot", fields: List[(String,String)]) extends IncomingWebhookMessage
  case class Root(text:String, attachments: List[Attachment])
  case class Attachment( fields: List[Field])
  case class Field(title:String, value: String, short: Boolean = true)


  def props(url: String) = {
    Props(classOf[IncomingWebhook], url)
  }
}


trait JsonSupport extends DefaultJsonProtocol {
  implicit val messageFormat = jsonFormat2(Message)
  implicit val fieldFormat = jsonFormat3(Field)
  implicit val attachmentFormat = jsonFormat1(Attachment)
  implicit val rootFormat = jsonFormat2(Root)
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
      })
    }

    case msg @ HelpMessage(name, text, fields) =>
/*
{
    "text": "I am a test message http://slack.com",
    "attachments": [
        {
      "fields": [
        {
          "title": "And here's ",
          "value": "asdfsdf",
          "short": true
        },
        {
          "title": "And here's ",
          "value": "asdfsdf",
          "short": true
        }
        ]
        }
    ]
}
 */
      val msg = Root(
        "I am a test message http://slack.com",
        List(
          Attachment(
            fields.map(x => Field(x._1, x._2))
            )
        )
      )

      val jsValue = rootFormat.write(msg)

      val entity = HttpEntity.apply(ContentType(MediaTypes.`application/json`), jsValue.compactPrint)
      val responseFuture: Future[HttpResponse] =
        Http().singleRequest(HttpRequest(entity = entity ,uri = url, method = HttpMethods.POST))

      responseFuture.onSuccess({
        case s => println(s)
      })




    case _ => {

    }
  }
}
