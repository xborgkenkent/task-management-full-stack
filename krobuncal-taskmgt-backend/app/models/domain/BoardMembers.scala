package models.domain

import java.util.UUID
import play.api.libs.json._


final case class BoardMembers(
    boardId: UUID,
    memberId: UUID
)

object BoardMembers {

  def unapply(
    boardMembers: BoardMembers
  ): Option[(UUID, UUID)] = {
    Some(
      boardMembers.boardId,
      boardMembers.memberId,
    )
  }
  implicit val writeBoardMembers: Writes[BoardMembers] = (boardMembers: BoardMembers) => {
    Json.obj(
      "boardId" -> boardMembers.boardId.toString,
      "memberId" -> boardMembers.memberId.toString
    )
  }
}