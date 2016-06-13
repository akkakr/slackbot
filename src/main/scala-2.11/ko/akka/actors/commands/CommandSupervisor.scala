package ko.akka.actors.commands

import java.util


import com.typesafe.config.ConfigFactory
import ko.akka.actors.IncomingWebhook
import ko.akka.actors.IncomingWebhook.{Message, HelpMessage}
import ko.akka.actors.commands.CommandSupervisor.Help
import ko.akka.actors.commands.GoogleActor.Search

import reflect._
import reflect.runtime.universe._
import reflect.runtime.currentMirror
import scala.collection.JavaConverters._
import java.util.ServiceLoader

import akka.actor.{ActorLogging, ActorRef, Actor, Props}
import scala.collection.mutable.Map

object CommandSupervisor {

  case object Help

  def props = {
    Props(classOf[CommandSupervisor])
  }
}

class CommandSupervisor extends Actor with ActorLogging {

  import CommandSupervisor.Help

  val commands: Map[String, (ActorRef, String)] = Map.empty;

  val incomingWebhook = context.actorOf(IncomingWebhook.props(ConfigFactory.load().getConfig("app").getString("incoming-slack-url")))

  override def preStart(): Unit = {
    // create Children
    val echo: ActorRef = context.actorOf(EchoActor.props, "Echo")
    val google: ActorRef = context.actorOf(GoogleActor.props, "Google")
    /*
  val ws = (ServiceLoader load classOf[Widget]).asScala
  for (w <- ws) {
    Console println s"Turn a ${w.getClass} by ${w.turn}"
  }

    val ws = (ServiceLoader.load(classOf[ko.akka.actors.commands.Command])).asScala
    for (w <- ws) {
      Console println s"Turn a ${w.getClass}"
    }
     */

    commands.put(EchoActor.name, (echo, EchoActor.description))
    commands.put(GoogleActor.name, (google, GoogleActor.description))
  }

  override def receive: Receive = {
    case msg @ Help =>
      val commandList = commands.map(x => (x._1, x._2._2)).toList
      incomingWebhook ! HelpMessage("amugena", "", commandList)
    case msg @ Search(_) =>
      commands.get("Google") match {
        case Some((ref, desc)) =>
          ref forward msg
        case None =>
          log.info("match failed.")
      }


    case msg @ Message(_, _) =>
      incomingWebhook forward msg
  }
}
