package models.service

import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.UUID
import javax.inject._
import models.actors.UserActor
import models.domain.Board
import models.domain.BoardMembers
import models.domain.CombinedBoardTask
import models.repo.BoardMembersRepo
import models.repo.BoardRepo
import models.repo.CommentRepo
import models.repo.TaskRepo
import play.api.libs.json.Json
import play.api.mvc.MultipartFormData
import scala.concurrent.ExecutionContext
import scala.concurrent.Future

@Singleton
class BoardService @Inject() (
  val boardRepo: BoardRepo,
  val boardMembersRepo: BoardMembersRepo,
  val taskRepo: TaskRepo,
  val commentRepo: CommentRepo,
  implicit val ec: ExecutionContext,
) {

  def insertBoard(board: Board, userId: UUID) = {
    boardRepo
      .addBoard(board)
      .map(userId =>
        boardMembersRepo.addBoardMembers(
          BoardMembers(board.id, userId, "owner")
        )
      )
    getAll(userId)
  }

  def editBoard(id: UUID, name: String, userId: UUID) = {
    boardRepo.editBoard(id, name)
    getAll(userId)
  }

  def deleteBoard(id: UUID, userId: UUID) = {
    boardRepo.deleteBoardById(id)
    getAll(userId)
  }

  def editTask(id: UUID, caption: String, userId: UUID) = {
    taskRepo.editTaskById(id, caption)
    getAll(userId)
  }

  def getAll(userId: UUID) = {
    val all = for {
      boards <- boardRepo.getAllBoardRepos()
      boardMembers <- boardMembersRepo.getBoardMembersByMemberId(userId)
      tasks <- taskRepo.getAllTaskRepos()
      comments <- commentRepo.getAllCommentRepos()
    } yield (CombinedBoardTask(boards, boardMembers, tasks, comments))

    all.map(fc => UserActor.sockets.foreach(socket => socket ! Json.toJson(fc)))
  }
}
