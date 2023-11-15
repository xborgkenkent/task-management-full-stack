package models.repo

import java.util.UUID
import javax.inject._
import models.domain.Comment
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import scala.concurrent.ExecutionContext
import slick.jdbc.JdbcProfile

@Singleton
final class CommentRepo @Inject() (
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

  protected class CommentTable(tag: Tag) extends Table[Comment](tag, "COMMENTS") {
    def id = column[UUID]("ID", O.PrimaryKey)
    def comment = column[String]("COMMENT")
    def userId = column[UUID]("USER_ID")
    def taskId = column[UUID]("TASK_ID") 

    def * =
      (id, comment, userId, taskId).mapTo[Comment]
  }

  val commentTable = TableQuery[CommentTable]

  def createCommentTable() = db.run(commentTable.schema.createIfNotExists)

  def getAllCommentRepos() = db.run(commentTable.result)

  def addComment(comment: Comment) =
    db.run(commentTable returning commentTable.map(_.id) += comment)

  def getCommentById(taskId: UUID) = db.run(commentTable.filter(_.taskId===taskId).result)
}
