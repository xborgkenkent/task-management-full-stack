package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import models.repo.UserRepo
import models.repo.TaskRepo
import models.domain.Task
import models.repo.TaskRepo
import models.service.BoardService
import models.domain.Task
import scala.concurrent._
import play.api.libs.json._
import models.domain.User
import play.api.data.Form
import play.api.data.Forms._
import java.util.UUID
import security.Authenticator
import security.UserRequest

@Singleton
class TaskController @Inject()(
  authenticator: Authenticator,
  val taskRepo: TaskRepo,
  val boardService: BoardService,
  val cc: ControllerComponents
)(implicit val ec: ExecutionContext) extends AbstractController(cc) {

    val formTask = Form(
        tuple(
            "caption" -> nonEmptyText,
            "boardId" -> nonEmptyText,
            "assignId" -> nonEmptyText
        )
    )

    val formEditTask = Form("caption" -> nonEmptyText)
    def addTask() = authenticator.async { implicit request =>
        request.session.get("userId") match {
            case Some(userId) => {
                formTask.bindFromRequest().fold(
                    formWithErrors => {
                        Future.successful(BadRequest)
                    },
                    task => {
                        taskRepo.addTask(Task(UUID.randomUUID, task._1, UUID.fromString(task._2), UUID.fromString(userId), "TODO", UUID.fromString(task._3))).map(_ => Ok)
                    }
                )
            }
            case None => {
                Future.successful(Unauthorized)
            }
        }
    }

    def getTask(id: String) = authenticator.async { implicit request =>
        request.session.get("userId") match {
            case Some(userId) => {
                taskRepo.getTaskByBoardId(UUID.fromString(id)).map(tasks => Ok(Json.toJson(tasks)))
            }
            case None => {
                Future.successful(Unauthorized)
            }
        }
    }

    def editTask(id: String) = authenticator.async { implicit request => 
        request.session.get("userId") match {
            case Some(userId) => {
                formEditTask.bindFromRequest().fold(
                    forWithErrors => {
                        Future.successful(BadRequest)
                    },
                    caption => {
                        boardService.editTask(UUID.fromString(id), caption, UUID.fromString(userId))
                        Future.successful(Ok)
                    }
                )
            }
            case None => Future.successful(Unauthorized)
        }
    }
}
