package models.repo

import java.util.UUID
import javax.inject._
import models.domain.BoardMembers
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import scala.concurrent.ExecutionContext
import slick.jdbc.JdbcProfile

@Singleton
final class BoardMembersRepo @Inject() (
  protected val dbConfigProvider: DatabaseConfigProvider,
  implicit val ec: ExecutionContext,
) extends HasDatabaseConfigProvider[JdbcProfile] {

  import slick.jdbc.PostgresProfile.api._

  // implicit val appointmentStatusColumnType: BaseColumnType[Date] =
  //   MappedColumnType.base[Date, String](
  //     date =>
  //       new java.text.SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy")
  //         .format(date),
  //     str =>
  //       new java.text.SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy")
  //         .parse(str),
  //   )

  protected class BoardMembersTable(tag: Tag) extends Table[BoardMembers](tag, "BOARD_MEMBERS") {
    def boardId = column[UUID]("BOARD_ID")
    def memberId = column[UUID]("MEMBER_ID")

    def * =
      (boardId, memberId).mapTo[BoardMembers]
  }

  val boardMembersTable = TableQuery[BoardMembersTable]

  def createBoardMembersTable() = db.run(boardMembersTable.schema.createIfNotExists)

  def getAllBoardMembersRepos() = db.run(boardMembersTable.result)

  def addBoardMembers(boardMember: BoardMembers) =
    db.run(boardMembersTable += boardMember)

  def getBoardMembersByMemberId(memberId: UUID) = db.run(boardMembersTable.filter(_.memberId===memberId).result)
  
  def getBoardMemberById(boardId: UUID, memberId: UUID) = db.run(boardMembersTable.filter( bm => bm.memberId===memberId && bm.boardId===boardId).result.headOption)
}
