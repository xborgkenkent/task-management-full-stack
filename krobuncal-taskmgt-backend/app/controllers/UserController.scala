package controllers

import java.util.UUID
import javax.inject._
import models.actors.UserActor
import models.domain.User
import models.repo.UserRepo
import org.apache.pekko.actor.ActorSystem
import org.apache.pekko.stream.Materializer
import play.api._
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json._
import play.api.libs.streams.ActorFlow
import play.api.mvc._
import scala.concurrent._
import security.Authenticator
import security.UserRequest

@Singleton
class UserController @Inject() (
  authenticator: Authenticator,
  val userRepo: UserRepo,
  val cc: ControllerComponents,
)(implicit val ec: ExecutionContext, m: Materializer, system: ActorSystem)
    extends AbstractController(cc) {

  val registerForm = Form(
    mapping(
      "id" -> ignored(UUID.randomUUID()),
      "name" -> nonEmptyText,
      "username" -> nonEmptyText,
      "password" -> nonEmptyText,
    )(User.apply)(User.unapply)
  )

  val loginForm = Form(
    tuple(
      "username" -> nonEmptyText,
      "password" -> nonEmptyText,
    )
  )
  def register() = Action.async { implicit request =>
    registerForm
      .bindFromRequest()
      .fold(
        forWithErrors => {
          Future.successful(BadRequest)
        },
        user => {
          userRepo.addUser(user.copy(id = UUID.randomUUID())).map(_ => Ok)
        },
      )
  }

  def login() = Action.async { implicit request =>
    loginForm
      .bindFromRequest()
      .fold(
        forWithErrors => {
          Future.successful(BadRequest)
        },
        user => {
          userRepo
            .findUserByUsernameAndPassword(user._1, user._2)
            .map(u => {
              u match {
                case Some(u) =>
                  Ok(Json.toJson(u)).withSession("userId" -> u.id.toString)
                case None => Unauthorized
              }
            })
        },
      )
  }

  def logout() = Action.async { implicit request =>
    Future.successful(Ok.withNewSession)
  }

  def getUsers() = authenticator.async { implicit request =>
    request.session.get("userId") match {
      case Some(userId) => {
        userRepo
          .getAllUsersExcept(UUID.fromString(userId))
          .map(users => Ok(Json.toJson(users)))
      }
      case None => {
        Future.successful(Unauthorized)
      }
    }
  }
  def socket() = WebSocket.accept[JsValue, JsValue] { request =>
    ActorFlow.actorRef { out =>
      UserActor.props(out /*, postService */ )
    }
  }
}
