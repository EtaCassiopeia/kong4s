package kong4s

import io.circe.Decoder

import scala.language.higherKinds
import kong4s.KongResponses.KResponse

trait HttpRequestBuilderExtension[C, M[_]] {
  def run[A](rb: HttpRequestBuilder[C, M])(implicit D: Decoder[A]): M[KResponse[A]]

  def runEmpty(rb: HttpRequestBuilder[C, M]): M[KResponse[Unit]]
}
