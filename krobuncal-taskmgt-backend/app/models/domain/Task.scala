package models.domain

import java.util.UUID
import play.api.libs.json._


final case class Task(
    id: UUID,
    caption: String,
    boardId: UUID,
    creatorId: UUID,
    status: String,
    assign: UUID
)

object Task {

  def unapply(
    task: Task
  ): Option[(UUID, String, UUID, UUID, String, UUID)] = {
    Some(
      task.id,
      task.caption,
      task.boardId,
      task.creatorId,
      task.status,
      task.assign
    )
  }
  implicit val writeTask: Writes[Task] = (task: Task) => {
    Json.obj(
      "id" -> task.id.toString,
      "caption" -> task.caption,
      "boardId" -> task.boardId.toString,
      "creatorId" -> task.creatorId.toString,
      "status" -> task.status,
      "assign" -> task.assign
    )
  }
}