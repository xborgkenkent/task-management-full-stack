package controllers

import java.util.UUID
import javax.inject._
import models.domain.User
import models.repo.UserRepo
import play.api._
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json._
import play.api.mvc._
import scala.concurrent._
import security.Authenticator
import security.UserRequest

@Singleton
class SessionController @Inject() (
  authenticator: Authenticator,
  val userRepo: UserRepo,
  val cc: ControllerComponents,
)(implicit val ec: ExecutionContext)
    extends AbstractController(cc) {

  def session() = authenticator.async { implicit request =>
    Future.successful(Ok(request.userId.toString))
  }
}
