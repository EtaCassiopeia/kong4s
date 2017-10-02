package kong4s

import cats.free.Free
import kong4s.app.Kong4s

object KongResponses {

  type KIO[A] = Free[Kong4s, A]

  type KResponse[A] = Either[KException, KResult[A]]

  case class KResult[A](result: A, statusCode: Int, headers: Map[String, IndexedSeq[String]])

  sealed abstract class KException(msg: String, cause: Option[Throwable] = None)
    extends Throwable(msg) {
    cause foreach initCause
  }

  case class JsonParsingException(
                                   msg: String,
                                   json: String
                                 ) extends KException(msg)

  case class UnexpectedException(msg: String) extends KException(msg)

}
