package models.domain

import models.domain.Board
import models.domain.BoardMembers
import models.domain.Comment
import models.domain.Task
import play.api.libs.json._

final case class CombinedBoardTask(
  board: Seq[Board],
  boardMember: Seq[(BoardMembers, Board)],
  task: Seq[Task],
  comment: Seq[Comment],
)

object CombinedBoardTask {
  implicit val writeComment: Writes[CombinedBoardTask] =
    Json.writes[CombinedBoardTask]
}
