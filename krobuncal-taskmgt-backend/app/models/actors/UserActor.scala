package models.actors


import org.apache.pekko.actor.Actor
import org.apache.pekko.actor.ActorRef
import org.apache.pekko.actor.Props
import play.api.libs.json._
import scala.collection.mutable.Buffer
import scala.concurrent.ExecutionContext.Implicits.global

class UserActor(out: ActorRef/*, postService: PostImageService*/) extends Actor:
  override def preStart(): Unit =
    UserActor.sockets += out
    // TODO: Insert code on actor creation here
    // postService.getAllPost().map { posts =>
    //   val outMessage = Json.toJson(posts)
    //   UserActor.sockets.foreach(socket => socket ! outMessage)
    // }

  override def receive = {
    // TODO: Insert code on websocket message received here
    case message: JsValue =>
      val outMessage = message
      UserActor.sockets.foreach(socket =>
        socket ! Json.obj("Message:" -> outMessage)
      )
  }

  override def postStop(): Unit =
    // TODO: Insert code on actor termination here
    val me = UserActor.sockets.find(_ == out).get
    UserActor.sockets -= me

end UserActor

object UserActor:
  val sockets: Buffer[ActorRef] = Buffer.empty
  def props(out: ActorRef /*, postService: PostImageService */) = Props(
    new UserActor(out /* postService */)
  )