package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import models.repo.UserRepo
import models.repo.BoardRepo
import models.repo.TaskRepo
import models.repo.BoardMembersRepo
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
class HomeController @Inject()(
  authenticator: Authenticator,
  val userRepo: UserRepo,
  val boardRepo: BoardRepo,
  val taskRepo: TaskRepo,
  val boardMembers: BoardMembersRepo,
  val commentRepo: CommentRepo,
  val cc: ControllerComponents
)(implicit val ec: ExecutionContext) extends AbstractController(cc) {

  

  def index() = Action.async { implicit request: Request[AnyContent] =>
    for{
      _ <- userRepo.createUserTable()
      - <- boardRepo.createBoardTable()
      - <- taskRepo.createTaskTable()
      _ <- boardMembers.createBoardMembersTable()
      _ <- commentRepo.createCommentTable()
    }yield(Ok)
  }
}
