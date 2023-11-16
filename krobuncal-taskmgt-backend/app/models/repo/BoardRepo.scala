package models.repo

import java.util.UUID
import javax.inject._
import models.domain.Board
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import scala.concurrent.ExecutionContext
import slick.jdbc.JdbcProfile

@Singleton
final class BoardRepo @Inject() (
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

  protected class BoardTable(tag: Tag) extends Table[Board](tag, "BOARDS") {
    def id = column[UUID]("ID", O.PrimaryKey)
    def name = column[String]("NAME", O.Length(255, true))
    def ownerId = column[UUID]("OWNER_ID")

    def * =
      (id, name, ownerId).mapTo[Board]
  }

  val boardTable = TableQuery[BoardTable]

  def createBoardTable() = db.run(boardTable.schema.createIfNotExists)

  def getAllBoardRepos() = db.run(boardTable.result)

  def addBoard(board: Board) =
    db.run(boardTable returning boardTable.map(_.id) += board)

  def getBoardByOwner(id: UUID) =
    db.run(boardTable.filter(_.ownerId === id).result)

  def authorization(id: UUID, ownerId: UUID) = db.run(
    boardTable
      .filter(b => b.id === id && b.ownerId === ownerId)
      .result
      .headOption
  )

  def deleteBoardById(id: UUID) = db.run(boardTable.filter(_.id === id).delete)

  def editBoard(id: UUID, name: String) =
    db.run(boardTable.filter(_.id === id).map(_.name).update(name))
}
