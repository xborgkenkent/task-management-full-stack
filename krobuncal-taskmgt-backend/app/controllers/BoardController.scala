package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import models.repo.UserRepo
import models.repo.BoardRepo
import models.domain.Board
import models.repo.TaskRepo
import models.repo.BoardMembersRepo
import models.domain.BoardMembers
import models.service.BoardService
import scala.concurrent._
import play.api.libs.json._
import models.domain.User
import play.api.data.Form
import play.api.data.Forms._
import java.util.UUID
import security.Authenticator
import security.UserRequest

@Singleton
class BoardController @Inject()(
  authenticator: Authenticator,
  val boardRepo: BoardRepo,
  val boardMemberRepo: BoardMembersRepo,
  val boardService: BoardService,
  val cc: ControllerComponents
)(implicit val ec: ExecutionContext) extends AbstractController(cc) {

    val formBoard = Form("name" -> nonEmptyText)
    val formBoardMember = Form(
        tuple(
            "boardId" -> nonEmptyText,
            "memberId" -> nonEmptyText,
            )
        )

    def addBoard = authenticator.async { implicit request =>
        request.session.get("userId") match {
            case Some(userId) => {
                formBoard.bindFromRequest().fold(
                    formWithErrors => {
                        Future.successful(BadRequest)
                    },
                    board => {
                        boardService.insertBoard(Board(UUID.randomUUID(), board, UUID.fromString(userId)), UUID.fromString(userId))
                        Future.successful(Ok)
                    }
                )
            }
            case None => {
                Future.successful(Unauthorized)
            }
        }
    }

    def addMember() = authenticator.async { implicit request =>
        request.session.get("userId") match {
            case Some(userId) => {
                formBoardMember.bindFromRequest().fold(
                    formWithErrors => {
                        Future.successful(BadRequest)
                    },
                    boardMember => {
                        boardMemberRepo.addBoardMembers(BoardMembers(UUID.fromString(boardMember._1), UUID.fromString(boardMember._2))).map(_ => Ok)
                    }
                )
            }
            case None => {
                Future.successful(Unauthorized)
            }
        }
    }

    def getBoards() = authenticator.async { implicit request =>
        request.session.get("userId") match {
            case Some(userId) => {
                boardRepo.getBoardByOwner(UUID.fromString(userId)).map(boards => Ok(Json.toJson(boards)))
            }
            case None => {
                Future.successful(Unauthorized)
            }
        }
    }

    def getBoardMembers(id: String) = authenticator.async { implicit request =>
        request.session.get("userId") match {
            case Some(userId) => {
                boardMemberRepo.getBoardMemberById(UUID.fromString(id), UUID.fromString(userId)).map(bm => Ok(Json.toJson(bm)))
            }
            case None => Future.successful(Unauthorized)
        }
    }
    def isOwner(id: String) = authenticator.async { implicit request =>
        request.session.get("userId") match {
            case Some(userId) => {
                boardRepo.authorization(UUID.fromString(id), UUID.fromString(userId)).map(user => {
                    user match {
                        case Some(u) => Ok
                        case None => BadRequest
                    }
                })
            }
            case None => Future.successful(Unauthorized)
        }
    }

    def deleteBoard(id: String) = authenticator.async { implicit request =>
        request.session.get("userId") match {
            case Some(userId) => {
                boardService.deleteBoard(UUID.fromString(id), UUID.fromString(userId))
                Future.successful(Ok)
            }
            case None => Future.successful(Unauthorized)
        }
    }

    def editBoard(id: String) = authenticator.async { implicit request =>
        request.session.get("userId") match {
            case Some(userId) => {
                formBoard.bindFromRequest().fold(
                    forWithErrors => {
                        Future.successful(BadRequest)
                    },
                    board => {
                        boardService.editBoard(UUID.fromString(id), board, UUID.fromString(userId))
                        Future.successful(Ok)
                    }
                )
            }
            case None => Future.successful(Unauthorized)
        }
    }
}
