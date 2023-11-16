package security

import jakarta.inject.Inject
import jakarta.inject.Singleton
import java.util.UUID
import play.api.mvc.*
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.util.Try

protected class UserRequest[A](
  val userId: UUID,
  request: Request[A],
) extends WrappedRequest[A](request)

@Singleton
final class Authenticator @Inject() (
  val parser: BodyParsers.Default
)(using val executionContext: ExecutionContext)
    extends ActionBuilder[UserRequest, AnyContent]
    with ActionRefiner[Request, UserRequest]:
  override protected def refine[A](
    request: Request[A]
  ): Future[Either[Result, UserRequest[A]]] =
    val userIdOpt: Option[UUID] =
      request.session
        .get("userId")
        .flatMap: userIdString =>
          Try:
            UUID.fromString(userIdString)
          .toOption

    userIdOpt match
      case None =>
        Future.successful(Left(Results.Unauthorized))
      case Some(userId) =>
              Future.successful(Right(new UserRequest(userId, request)))
  end refine
end Authenticator