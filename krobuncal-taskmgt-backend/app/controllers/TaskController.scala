package controllers

import java.util.UUID
import javax.inject._
import models.domain.Task
import models.domain.User
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
class TaskController @Inject() (
  authenticator: Authenticator,
  val taskRepo: TaskRepo,
  val boardService: BoardService,
  val cc: ControllerComponents,
)(implicit val ec: ExecutionContext)
    extends AbstractController(cc) {

  val formTask = Form(
    tuple(
      "caption" -> nonEmptyText,
      "boardId" -> nonEmptyText,
      "assignId" -> nonEmptyText,
    )
  )

  val formEditTask = Form("caption" -> nonEmptyText)
  def addTask() = authenticator.async { implicit request =>
    formTask
      .bindFromRequest()
      .fold(
        formWithErrors => {
          Future.successful(BadRequest)
        },
        task => {
          taskRepo
            .addTask(
              Task(
                UUID.randomUUID,
                task._1,
                UUID.fromString(task._2),
                request.userId,
                "TODO",
                UUID.fromString(task._3),
              )
            )
            .map(_ => Ok)
        },
      )
  }

  def getTask(id: String) = authenticator.async { implicit request =>
    taskRepo
      .getTaskByBoardId(UUID.fromString(id))
      .map(tasks => Ok(Json.toJson(tasks)))
  }

  def editTask(id: String) = authenticator.async { implicit request =>
    formEditTask
      .bindFromRequest()
      .fold(
        forWithErrors => {
          Future.successful(BadRequest)
        },
        caption => {
          boardService
            .editTask(UUID.fromString(id), caption, request.userId)
          Future.successful(Ok)
        },
      )
  }
}
