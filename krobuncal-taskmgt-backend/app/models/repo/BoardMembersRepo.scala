package models.repo

import java.util.UUID
import javax.inject._
import models.domain.BoardMembers
import models.repo.BoardMembersRepo
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import scala.concurrent.ExecutionContext
import slick.jdbc.JdbcProfile

@Singleton
final class BoardMembersRepo @Inject() (
  val boardRepo: BoardRepo,
  protected val dbConfigProvider: DatabaseConfigProvider,
  implicit val ec: ExecutionContext,
) extends HasDatabaseConfigProvider[JdbcProfile] {

  import slick.jdbc.PostgresProfile.api._

  protected class BoardMembersTable(tag: Tag)
      extends Table[BoardMembers](tag, "BOARD_MEMBERS") {
    def boardId = column[UUID]("BOARD_ID")
    def memberId = column[UUID]("MEMBER_ID")
    def permission = column[String]("PERMISSION")

    def * =
      (boardId, memberId, permission).mapTo[BoardMembers]
  }

  val boardMembersTable = TableQuery[BoardMembersTable]

  def createBoardMembersTable() =
    db.run(boardMembersTable.schema.createIfNotExists)

  def getAllBoardMembersRepos() = db.run(boardMembersTable.result)

  def addBoardMembers(boardMember: BoardMembers) =
    db.run(boardMembersTable += boardMember)

  def getBoardMembersByMemberId(memberId: UUID) = {
    db.run(
      boardMembersTable
        .join(boardRepo.boardTable)
        .on(_.boardId === _.id)
        .result
    )
  }

  def getBoardMemberById(boardId: UUID, memberId: UUID) = db.run(
    boardMembersTable
      .filter(bm => bm.memberId === memberId && bm.boardId === boardId)
      .result
      .headOption
  )
}
