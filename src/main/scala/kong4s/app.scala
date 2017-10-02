package kong4s

import cats.data.EitherK
import kong4s.free.algebra.{ApiOp, PluginOp}

object app {
  type Kong4s[A] = EitherK[ApiOp, PluginOp, A]
}
