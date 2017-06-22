import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.{complete, get, pathSingleSlash}
import akka.stream.ActorMaterializer

/**
  * Created by kinshuk.bairagi on 22/06/17.
  */
object SabjiWala {

  println("Hello World")

  implicit val actorSystem = ActorSystem("system")
  implicit val actorMaterializer = ActorMaterializer()

  def main(args: Array[String]): Unit = {
    Http().bindAndHandle(new Routes().route,"0.0.0.0",8080)
  }


}
