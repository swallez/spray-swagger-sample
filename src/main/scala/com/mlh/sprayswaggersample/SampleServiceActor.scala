package com.mlh.sprayswaggersample

import akka.actor.ActorLogging
import com.github.swagger.spray.SwaggerHttpService
import com.github.swagger.spray.model.Info
import spray.routing._

import scala.reflect.runtime.universe._

class SampleServiceActor
  extends HttpServiceActor
  with ActorLogging {

    override def actorRefFactory = context

    val pets = new PetHttpService {
      def actorRefFactory = context
    }

    val users = new UserHttpService {
      def actorRefFactory = context
    }

    val persons = new PersonHttpService {
      def actorRefFactory = context
    }

    def receive = runRoute(pets.routes ~ users.routes ~ persons.routes ~ swaggerService.routes ~
      get {
        pathPrefix("") { pathEndOrSingleSlash {
            getFromResource("swagger-ui/index.html")
          }
        } ~
        getFromResourceDirectory("swagger-ui")
      })

  val swaggerService = new SwaggerHttpService {

    override val apiTypes = Seq(typeOf[PetHttpService], typeOf[UserHttpService], typeOf[PersonHttpService])
    //override val basePath = "/" // let swagger-ui determine the host and port
    //override val apiDocsPath = "api-docs"
    override def actorRefFactory = context
    override val info = Info(
      title="Spray-Swagger Sample",
      description="A sample petstore service using spray and spray-swagger.",
      version = "2.0",
      termsOfService = "Terms"

      //"TOC Url", "Michael Hamrah @mhamrah", "Apache V2", "http://www.apache.org/licenses/LICENSE-2.0"
    )

    //authorizations, not used
  }
}
