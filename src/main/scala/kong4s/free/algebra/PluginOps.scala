package kong4s.free.algebra

import cats.InjectK

sealed trait PluginOp[A]

class PluginOps[F[_]](implicit I: InjectK[ApiOp, F]) {

}

object PluginOps {

  implicit def instance[F[_]](implicit I: InjectK[ApiOp, F]): PluginOps[F] = new PluginOps[F]

}