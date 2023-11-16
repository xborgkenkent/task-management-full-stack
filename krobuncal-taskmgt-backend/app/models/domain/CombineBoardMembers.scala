package models.domain

import java.util.UUID
import play.api.libs.json._

final case class CombineBoardMembers(
  id: UUID,
  name: String,
  ownerId: UUID,
  boardId: UUID,
  memberId: UUID,
  permission: String,
)

object CombineBoardMembers {
  def unapply(
    combineBoardMembers: CombineBoardMembers
  ): Option[(UUID, String, UUID, UUID, UUID, String)] = {
    Some(
      combineBoardMembers.id,
      combineBoardMembers.name,
      combineBoardMembers.ownerId,
      combineBoardMembers.boardId,
      combineBoardMembers.memberId,
      combineBoardMembers.permission,
    )
  }

  implicit val writeCombineBoardMembers: Writes[CombineBoardMembers] =
    Json.writes[CombineBoardMembers]
}
