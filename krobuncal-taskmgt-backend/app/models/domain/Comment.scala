package models.domain

import java.util.UUID
import java.util.Date
import play.api.libs.json._


final case class Comment(
    id: UUID,
    comment: String,
    userId: UUID,
    taskId: UUID,
)

object Comment {

  def unapply(
    comment: Comment
  ): Option[(UUID, String, UUID, UUID)] = {
    Some(
      comment.id,
      comment.comment,
      comment.userId,
      comment.taskId,
    )
  }
  implicit val writeComment: Writes[Comment] = (comment: Comment) => {
    Json.obj(
      "id" -> comment.id.toString,
      "comment" -> comment.comment,
      "userId" -> comment.userId.toString,
      "taskId" -> comment.taskId.toString
    )
  }
}