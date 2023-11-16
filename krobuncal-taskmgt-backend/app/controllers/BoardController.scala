package controllers

import java.util.UUID
import javax.inject._
import models.domain.Board
import models.domain.BoardMembers
import models.domain.User
import models.repo.BoardMembersRepo
import models.repo.BoardRepo
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
class BoardController @Inject() (
  authenticator: Authenticator,
  val boardRepo: BoardRepo,
  val boardMemberRepo: BoardMembersRepo,
  val boardService: BoardService,
  val cc: ControllerComponents,
)(implicit val ec: ExecutionContext)
    extends AbstractController(cc) {

  val formBoard = Form("name" -> nonEmptyText)
  val formBoardMember = Form(
    tuple(
      "boardId" -> nonEmptyText,
      "memberId" -> nonEmptyText,
    )
  )

  def addBoard = authenticator.async { implicit request =>
    formBoard
      .bindFromRequest()
      .fold(
        formWithErrors => {
          Future.successful(BadRequest)
        },
        board => {
          boardService.insertBoard(
            Board(UUID.randomUUID(), board, request.userId),
            request.userId,
          )
          Future.successful(Ok)
        },
      )
  }

  def addMember() = authenticator.async { implicit request =>
    formBoardMember
      .bindFromRequest()
      .fold(
        formWithErrors => {
          Future.successful(BadRequest)
        },
        boardMember => {
          boardMemberRepo
            .addBoardMembers(
              BoardMembers(
                UUID.fromString(boardMember._1),
                UUID.fromString(boardMember._2),
                "member",
              )
            )
            .map(_ => Ok)
        },
      )
  }

  def getBoards() = authenticator.async { implicit request =>
    boardMemberRepo
      .getBoardMembersByMemberId(request.userId)
      .map(boards => Ok(Json.toJson(boards)))
  }

  def getBoardMembers(id: String) = authenticator.async { implicit request =>
    boardMemberRepo
      .getBoardMemberById(UUID.fromString(id), request.userId)
      .map(bm => Ok(Json.toJson(bm)))
  }

  def deleteBoard(id: String) = authenticator.async { implicit request =>
    boardService.deleteBoard(UUID.fromString(id), request.userId)
    Future.successful(Ok)
  }

  def editBoard(id: String) = authenticator.async { implicit request =>
    formBoard
      .bindFromRequest()
      .fold(
        forWithErrors => {
          Future.successful(BadRequest)
        },
        board => {
          boardService
            .editBoard(UUID.fromString(id), board, request.userId)
          Future.successful(Ok)
        },
      )
  }
}
