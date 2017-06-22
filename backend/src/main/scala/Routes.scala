import akka.http.scaladsl.server.Directives.{complete, get, pathSingleSlash}

/**
  * Created by kinshuk.bairagi on 22/06/17.
  */
class Routes {

  val route =
    pathSingleSlash {
      get {
        complete {
          "Hello world"
        }
      }
    }



}
