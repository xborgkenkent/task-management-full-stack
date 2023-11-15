package models.domain


import java.util.UUID
import play.api.libs.json._


final case class Board(
    id: UUID,
    name: String,
    ownerId: UUID,
)

object Board {

  def unapply(
    board: Board
  ): Option[(UUID, String, UUID)] = {
    Some(
      board.id,
      board.name,
      board.ownerId,
    )
  }
  implicit val writeBoard: Writes[Board] = (board: Board) => {
    Json.obj(
      "id" -> board.id.toString,
      "name" -> board.name,
      "ownerId" -> board.ownerId.toString
    )
  }
}