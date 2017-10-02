package kong4s.free.algebra

import cats.InjectK
import cats.free.Free
import kong4s.KongResponses.KResponse
import kong4s.free.domain.{Api, Pagination}

sealed trait ApiOp[A]

final case class GetApiByName(apiName: String) extends ApiOp[KResponse[Api]]

final case class GetApiById(id: String) extends ApiOp[KResponse[Api]]

final case class GetApis(since: Int, pagination: Option[Pagination] = None) extends ApiOp[KResponse[List[Api]]]

class ApiOps[F[_]](implicit I: InjectK[ApiOp, F]) {
  def getApiByName(name: String): Free[F, KResponse[Api]] =
    Free.inject[ApiOp, F](GetApiByName(name))

  def getApiById(id: String): Free[F, KResponse[Api]] =
    Free.inject[ApiOp, F](GetApiById(id))

  def getApis(since: Int, pagination: Option[Pagination] = None): Free[F, KResponse[List[Api]]] =
    Free.inject[ApiOp, F](GetApis(since, pagination))
}

object ApiOps {
  implicit def instance[F[_]](implicit I: InjectK[ApiOp, F]): ApiOps[F] = new ApiOps[F]
}