package com.github.galaxychat

import java.io.File

import akka.actor._
import com.typesafe.config.ConfigFactory

class Client extends Actor {

  val server = context.actorSelection("akka.tcp://GalaxyChatServerSystem@127.0.0.1:5150/user/server")

  def receive = {
    case _ => // TODO
  }

}

object Main {

  def main(args: Array[String]) {

    val configFile = getClass.getClassLoader.getResource("application.conf").getFile
    val config = ConfigFactory.parseFile(new File(configFile))
    val system = ActorSystem("GalaxyChatClientSystem", config)
    val client = system.actorOf(Props[Client])

  }

}
