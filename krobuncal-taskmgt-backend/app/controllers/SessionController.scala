package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import models.repo.UserRepo
import scala.concurrent._
import play.api.libs.json._
import models.domain.User
import play.api.data.Form
import play.api.data.Forms._
import java.util.UUID
import security.Authenticator
import security.UserRequest

@Singleton
class SessionController @Inject()(
  authenticator: Authenticator,
  val userRepo: UserRepo,
  val cc: ControllerComponents
)(implicit val ec: ExecutionContext) extends AbstractController(cc) {

    def session() = authenticator.async { implicit request =>
        request.session.get("userId") match {
            case Some(userId) => Future(Ok)
            case None => Future(Unauthorized)
        }
    }
}
