package controllers

import java.util.UUID
import javax.inject._
import models.domain.Comment
import models.domain.User
import models.repo.BoardMembersRepo
import models.repo.BoardRepo
import models.repo.CommentRepo
import models.repo.TaskRepo
import models.repo.UserRepo
import models.service.BoardService
import play.api._
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json._
import play.api.mvc._
import scala.concurrent._
import security.Authenticator
import security.UserRequest

@Singleton
class CommentController @Inject() (
  authenticator: Authenticator,
  val commentRepo: CommentRepo,
  val boardService: BoardService,
  val cc: ControllerComponents,
)(implicit val ec: ExecutionContext)
    extends AbstractController(cc) {

  val formComment = Form(
    tuple(
      "comment" -> nonEmptyText,
      "taskId" -> nonEmptyText,
    )
  )

  def addComment() = authenticator.async { implicit request =>
    formComment
      .bindFromRequest()
      .fold(
        formWithErrors => {
          Future.successful(BadRequest)
        },
        comment => {
          commentRepo
            .addComment(
              Comment(
                UUID.randomUUID(),
                comment._1,
                request.userId,
                UUID.fromString(comment._2),
              )
            )
            .map(_ => Ok)
        },
      )
  }

  def getComments() = authenticator.async { implicit request =>
    commentRepo.getAllCommentRepos().map(comment => Ok(Json.toJson(comment)))
  }
}
