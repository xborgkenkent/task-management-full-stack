package security

import javax.inject._
import play.api._
import play.api.mvc._
import scala.concurrent._
import scala.concurrent.Future

class UserRequest[A](val userId: Option[String], request: Request[A])
    extends WrappedRequest[A](request)

@Singleton
class Authenticator @Inject() (val parser: BodyParsers.Default)(implicit
  ec: ExecutionContext
) extends ActionBuilder[UserRequest, AnyContent] {
  val logger = Logger(this.getClass)

  protected def executionContext: scala.concurrent.ExecutionContext = ec
  override def invokeBlock[A](
    request: Request[A],
    block: UserRequest[A] => Future[Result],
  ): Future[Result] = {
    request.session.get("userId") match {
      case Some(userId) =>
        block(new UserRequest(Some(userId), request))
      case None =>
        block(new UserRequest(None, request))
    }
  }
}