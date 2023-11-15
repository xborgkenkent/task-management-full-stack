package models.service

import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.UUID
import javax.inject._
import models.actors.UserActor
import models.repo.BoardRepo
import models.domain.Board
import models.repo.TaskRepo
import models.domain.CombinedBoardTask
import models.repo.BoardMembersRepo
import models.repo.CommentRepo
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
    boardRepo.addBoard(board)
    
    val futureCombined = for {
        boards <- boardRepo.getAllBoardRepos()
        boardMembers <- boardMembersRepo.getBoardMembersByMemberId(userId)
        tasks <- taskRepo.getAllTaskRepos()
        comments <- commentRepo.getAllCommentRepos()
    }yield(CombinedBoardTask(boards, boardMembers, tasks, comments))

    futureCombined.map(fc => UserActor.sockets.foreach(socket => socket ! Json.toJson(fc)))
    //UserActor.sockets.foreach(socket => socket ! Json.toJson(board))
  }

//   def getAll() = {
//     val x = for {
//         boards <- boardRepo.getBoardByOwner(userId)
//         boardMembers <- boardMembersRepo.getAllBoardMembersRepos()
//         tasks <- taskRepo.getAllTaskRepos()
//     }yield(CombinedBoardTask(boards, boardMembers, tasks))
//      println(x)
//   }

  def editBoard(id: UUID, name: String, userId: UUID) = {
    boardRepo.editBoard(id, name)

    val futureCombined = for {
        boards <- boardRepo.getAllBoardRepos()
        boardMembers <- boardMembersRepo.getBoardMembersByMemberId(userId)
        tasks <- taskRepo.getAllTaskRepos()
        comments <- commentRepo.getAllCommentRepos()
    }yield(CombinedBoardTask(boards, boardMembers, tasks, comments))

    futureCombined.map(fc => UserActor.sockets.foreach(socket => socket ! Json.toJson(fc)))
  }

  def deleteBoard(id: UUID, userId: UUID) = {
    boardRepo.deleteBoardById(id)

    val futureCombined = for {
        boards <- boardRepo.getAllBoardRepos()
        boardMembers <- boardMembersRepo.getBoardMembersByMemberId(userId)
        tasks <- taskRepo.getAllTaskRepos()
        comments <- commentRepo.getAllCommentRepos()
    }yield(CombinedBoardTask(boards, boardMembers, tasks, comments))
    futureCombined.map(fc => UserActor.sockets.foreach(socket => socket ! Json.toJson(fc)))
  }

  def editTask(id: UUID, caption: String, userId: UUID) = {
    taskRepo.editTaskById(id, caption)

    val futureCombined = for {
        boards <- boardRepo.getAllBoardRepos()
        boardMembers <- boardMembersRepo.getBoardMembersByMemberId(userId)
        tasks <- taskRepo.getAllTaskRepos()
        comments <- commentRepo.getAllCommentRepos()
    }yield(CombinedBoardTask(boards, boardMembers, tasks, comments))

    futureCombined.map(fc => UserActor.sockets.foreach(socket => socket ! Json.toJson(fc)))
  }
}