package com.flipkart.sabjiwala.services

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

/**
  * Created by saurabh.mimani on 22/06/17.
  */
object HttpService {
  implicit val actorSystem = ActorSystem("http")
  implicit val materializer = ActorMaterializer()

}
