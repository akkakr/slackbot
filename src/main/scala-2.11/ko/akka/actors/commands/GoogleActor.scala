package ko.akka.actors.commands

import akka.actor._
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethods, HttpRequest}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import ko.akka.actors.IncomingWebhook.Message
import ko.akka.actors.commands.GoogleActor.Search

object GoogleActor {
  val name : String = "Google"
  val description : String = "search engine"

  case class Search(query: String)

  def props(): Props = {
    Props(classOf[GoogleActor])
  }
}

class GoogleActor extends Actor with ActorLogging{

  import GoogleActor.Search

  import context.dispatcher
  implicit val system = context.system
  implicit val materializer = ActorMaterializer()

  override def receive: Receive = {
    case Search(query) =>
      // 1. 구글 데이터를 긁어서
      val url: String = s"https://www.google.co.kr/?gws_rd=ssl#newwindow=1&q=$query"
      Http().singleRequest(HttpRequest(uri = url, method = HttpMethods.GET)).map { response =>
        // 2. incomimng Webhook Actor에게 전달
        // parent에 전달
        Unmarshal(response.entity).to[String].map { responseString =>
          log.info(responseString)
          context.parent ! Message(responseString)
        }
      }


  }
}
