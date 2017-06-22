package com.flipkart.sabjiwala

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.flipkart.sabjiwala.api.Routes
import com.flipkart.sabjiwala.directives.CORSDirectives

/**
  * Created by kinshuk.bairagi on 22/06/17.
  */
object SabjiWala extends CORSDirectives {

  implicit val actorSystem = ActorSystem("system")
  implicit val actorMaterializer = ActorMaterializer()

  def main(args: Array[String]): Unit = {

    println(
      """
        |     _______.     ___      .______          __   __  ____    __    ____  ___       __          ___
        |    /       |    /   \     |   _  \        |  | |  | \   \  /  \  /   / /   \     |  |        /   \
        |   |   (----`   /  ^  \    |  |_)  |       |  | |  |  \   \/    \/   / /  ^  \    |  |       /  ^  \
        |    \   \      /  /_\  \   |   _  <  .--.  |  | |  |   \            / /  /_\  \   |  |      /  /_\  \
        |.----)   |    /  _____  \  |  |_)  | |  `--'  | |  |    \    /\    / /  _____  \  |  `----./  _____  \
        ||_______/    /__/     \__\ |______/   \______/  |__|     \__/  \__/ /__/     \__\ |_______/__/     \__\
        |
    """.stripMargin)

    Http().bindAndHandle(cors(new Routes().route),"0.0.0.0",8080)
  }


}
