package com.github.galaxychat

import java.io.File

import akka.actor._
import com.typesafe.config.ConfigFactory

import scala.io.StdIn.readLine

case class User(val nick: String, val addr: ActorRef)
case class Connection(val user: User)

class Client extends Actor {

  val server = context.actorSelection("akka.tcp://GalaxyChatServerSystem@127.0.0.1:5150/user/server")
  var nick: String = ""

  def receive = {

    case Connection(User(n, addr)) => {
      nick = n
      server ! Connection(User(nick, addr))
    }

  }

}

object Main {

  def main(args: Array[String]) {

    val configFile = getClass.getClassLoader.getResource("application.conf").getFile
    val config = ConfigFactory.parseFile(new File(configFile))
    val system = ActorSystem("GalaxyChatClientSystem", config)
    val client = system.actorOf(Props[Client])

    println("\n\nGalaxy Chat\n")
    print("Write your nick: ")
    val nick = readLine
    println("")

    client ! Connection(User(nick, client))

  }

}
