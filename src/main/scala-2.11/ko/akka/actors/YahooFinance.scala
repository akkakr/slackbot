package ko.akka.actors

import java.net.URLEncoder

import akka.actor.{Actor, ActorLogging, Props}
import com.github.nscala_time.time._
import com.github.nscala_time.time.Imports._
import ko.akka.actors.YahooFinance.{Gold, WonDollar, YahooFinanceRequest}

import scala.concurrent.ExecutionContext

/**
  * Created by before30 on 2016. 4. 14..
  */
object YahooFinance {
  sealed trait YahooFinanceRequest

  case class WonDollar(start: String, end: String) extends YahooFinanceRequest
  case class Gold(start:String, end: String) extends YahooFinanceRequest

  sealed trait YahooFinanceResponse

  def props() = {
    Props(classOf[YahooFinance])
  }
}

class YahooFinance extends Actor with ActorLogging {
  import akka.http.scaladsl.Http
  import akka.http.scaladsl.model._
  import akka.stream.ActorMaterializer

  import ExecutionContext.Implicits.global
  import scala.concurrent.Future


  implicit val system = context.system
  implicit val materializer = ActorMaterializer()

  override def receive: Receive = {
    case req: YahooFinanceRequest => {

      val queryString = req match {
        case x: WonDollar => {
          val startday = (LocalDateTime.now - 3.days).toString(StaticDateTimeFormat.forPattern("yyyy-MM-dd"))
          val endday = (LocalDateTime.now).toString(StaticDateTimeFormat.forPattern("yyyy-MM-dd"))

          "SELECT * FROM yahoo.finance.historicaldata " +
            "WHERE symbol = \"KRW=X\" AND startDate = \"" + startday+ "\" AND endDate = \"" + endday + "\""
        }
        case x: Gold => "select * from yahoo.finance.quotes where symbol = \"XAUUSD=X\""
      }


      val baseUrl = "https://query.yahooapis.com/v1/public/yql?q="
      val fullUrlStr = baseUrl + URLEncoder.encode(queryString, "UTF-8").replace("=", "%3D") +
      "&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys"

      val responseFuture: Future[HttpResponse] =
        Http().singleRequest(HttpRequest(uri = fullUrlStr, method = HttpMethods.GET))

      responseFuture.onSuccess({
        case s => println(s._3)
      })
    }
    case _ =>
  }
}
