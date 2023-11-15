package models.repo

import java.util.UUID
import javax.inject._
import models.domain.Task
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import scala.concurrent.ExecutionContext
import slick.jdbc.JdbcProfile

@Singleton
final class TaskRepo @Inject() (
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

  protected class TaskTable(tag: Tag) extends Table[Task](tag, "TASKS") {
    def id = column[UUID]("ID", O.PrimaryKey)
    def caption = column[String]("CAPTION", O.Length(255, true))
    def boardId = column[UUID]("BOARD_ID")
    def creatorId = column[UUID]("CREATOR_ID")
    def status = column[String]("STATUS")
    def assign = column[UUID]("ASSIGN")
    def * =
      (id, caption, boardId, creatorId, status, assign).mapTo[Task]
  }

  val taskTable = TableQuery[TaskTable]

  def createTaskTable() = db.run(taskTable.schema.createIfNotExists)

  def getAllTaskRepos() = db.run(taskTable.result)

  def addTask(task: Task) =
    db.run(taskTable returning taskTable.map(_.id) += task)

  def getTaskByBoardId(id: UUID) = db.run(taskTable.filter(_.boardId===id).result)

  def editTaskById(id: UUID, caption: String) = db.run(taskTable.filter(_.id===id).map(_.caption).update(caption))
}
