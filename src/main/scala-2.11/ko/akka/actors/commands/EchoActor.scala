package ko.akka.actors.commands

import akka.actor.{Props, Actor, ActorLogging}

/**
  * @since : 2016. 5. 23.
  * @author : Lawrence
  * @note :
  */

object EchoActor {
  val name : String = "Echo"
  val description : String = "desc"

  def props: Props = {
    Props(classOf[EchoActor], name, description)
  }
}

class EchoActor(name: String, description: String) extends Actor with ActorLogging with Command{

  override def receive: Receive = {
    case _ =>
  }
}
