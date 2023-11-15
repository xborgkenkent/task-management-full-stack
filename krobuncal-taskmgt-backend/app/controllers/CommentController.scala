package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import models.repo.UserRepo
import models.repo.BoardRepo
import models.repo.TaskRepo
import models.domain.Comment
import models.repo.BoardMembersRepo
import models.service.BoardService
import models.repo.CommentRepo
import scala.concurrent._
import play.api.libs.json._
import models.domain.User
import play.api.data.Form
import play.api.data.Forms._
import java.util.UUID
import security.Authenticator
import security.UserRequest

@Singleton
class CommentController @Inject()(
  authenticator: Authenticator,
  val commentRepo: CommentRepo,
  val boardService: BoardService,
  val cc: ControllerComponents
)(implicit val ec: ExecutionContext) extends AbstractController(cc) {

  val formComment = Form(
    tuple(
        "comment" -> nonEmptyText,
        "taskId" -> nonEmptyText
    )
  )

  def addComment() = authenticator.async { implicit request => 
    request.session.get("userId") match {
        case Some(userId) => {
            formComment.bindFromRequest().fold(
                formWithErrors => {
                    Future.successful(BadRequest)
                },
                comment => {
                    commentRepo.addComment(Comment(UUID.randomUUID(), comment._1, UUID.fromString(userId), UUID.fromString(comment._2))).map(_ => Ok)
                }
            )
        }
        case None => Future.successful(Unauthorized)
    }
  }

  def getComments() = authenticator.async { implicit request =>
    request.session.get("userId") match {
        case Some(userId) => {
            commentRepo.getAllCommentRepos().map(comment => Ok(Json.toJson(comment)))
        }
        case None => Future.successful(Unauthorized)
    }
  }
}
