package models.domain

import java.util.UUID
import play.api.libs.json._

case class User(
  id: UUID,
  name: String,
  username: String,
  password: String,
)

object User {

  def unapply(
    user: User
  ): Option[(UUID, String, String, String)] = {
    Some(
      user.id,
      user.name,
      user.username,
      user.password,
    )
  }
  implicit val writes: Writes[User] = (user: User) => {
    Json.obj(
      "id" -> user.id.toString,
      "name" -> user.name,
      "username" -> user.username,
    )
  }
}
