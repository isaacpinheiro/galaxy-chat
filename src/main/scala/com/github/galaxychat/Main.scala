package com.github.galaxychat

import java.io.File

import akka.actor._
import com.typesafe.config.ConfigFactory

import scala.io.StdIn.readLine

case class User(val nick: String, val addr: ActorRef)
case class Connection(val user: User)
case class Message(val nick: String, val content: String)
case class Response(val content: String)
case object ListUsers
case class Nick(val oldNick: String, val newNick: String)
case class Quit(val nick: String)
case class Input(val content: String)

class Client extends Actor {

  val server = context.actorSelection("akka.tcp://GalaxyChatServerSystem@127.0.0.1:5150/user/server")
  var nick: String = ""

  def receive = {

    case Connection(User(n, addr)) => {
      nick = n
      server ! Connection(User(nick, addr))
    }

    case Response(msg) => {
      println(msg)
    }

    case Input(msg) => {

      msg.split(" ").toList match {

        case (h :: _) if h == "/list" => {
          server ! ListUsers
        }

        case (h :: _) if h == "/quit" => {
          server ! Quit(nick)
        }

        case (h :: t) if h == "/nick" => {
          server ! Nick(nick, t.head)
          nick = t.head
        }

        case (h :: t) if h == "/kill" => {

        }

        case _ => {
          server ! Message(nick, msg)
        }

      }

    }

  }

}

object Main {

  def main(args: Array[String]) {

    val configFile = getClass.getClassLoader.getResource("application.conf").getFile
    val config = ConfigFactory.parseFile(new File(configFile))
    val system = ActorSystem("GalaxyChatClientSystem", config)
    val client = system.actorOf(Props[Client])

    println("\nGalaxy Chat\n")
    print("Write your nick: ")
    val nick = readLine

    client ! Connection(User(nick, client))

    def loop() {

      val msg = readLine
      client ! Input(msg)

      if (msg != "/quit") loop()
      else {
        system.stop(client)
        system.terminate()
      }

    }

    loop()

  }

}
