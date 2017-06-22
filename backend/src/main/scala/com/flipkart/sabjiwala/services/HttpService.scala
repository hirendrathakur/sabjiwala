package com.flipkart.sabjiwala.services

import akka.actor.ActorSystem

/**
  * Created by saurabh.mimani on 22/06/17.
  */
object HttpService {
  implicit val actorSystem = ActorSystem("http")
}
