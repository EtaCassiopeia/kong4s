package kong4s

import kong4s.free.domain.Pagination
import io.circe.Decoder
import kong4s.KongResponses.KResponse
import kong4s.HttpClient._

object HttpClient {
  type Headers = Map[String, String]

  sealed trait HttpVerb {
    def verb: String
  }

  case object Get extends HttpVerb {
    def verb = "GET"
  }

  case object Post extends HttpVerb {
    def verb = "POST"
  }

  case object Put extends HttpVerb {
    def verb = "PUT"
  }

  case object Delete extends HttpVerb {
    def verb = "DELETE"
  }

  case object Patch extends HttpVerb {
    def verb = "PATCH"
  }

  sealed trait HttpStatus {
    def statusCode: Int
  }

  case object HttpCode200 extends HttpStatus {
    def statusCode = 200
  }

  case object HttpCode299 extends HttpStatus {
    def statusCode = 299
  }

}

class HttpRequestBuilder[C, M[_]](
                                   val url: String,
                                   val httpVerb: HttpVerb = Get,
                                   val data: Option[String] = None,
                                   val params: Map[String, String] = Map.empty[String, String],
                                   val headers: Map[String, String] = Map.empty[String, String]
                                 ) {

  def postMethod = new HttpRequestBuilder[C, M](url, Post, data, params, headers)

  def putMethod = new HttpRequestBuilder[C, M](url, Put, data, params, headers)

  def patchMethod = new HttpRequestBuilder[C, M](url, Patch, data, params, headers)

  def deleteMethod = new HttpRequestBuilder[C, M](url, Delete, data, params, headers)

  def withHeaders(headers: Map[String, String]) =
    new HttpRequestBuilder[C, M](url, httpVerb, data, params, headers)

  def withParams(params: Map[String, String]) =
    new HttpRequestBuilder[C, M](url, httpVerb, data, params, headers)

  def withData(data: String) =
    new HttpRequestBuilder[C, M](url, httpVerb, Option(data), params, headers)
}

object HttpRequestBuilder {
  def httpRequestBuilder[C, M[_]](
                                   url: String,
                                   httpVerb: HttpVerb = Get,
                                   data: Option[String] = None,
                                   params: Map[String, String] = Map.empty[String, String],
                                   headers: Map[String, String] = Map.empty[String, String]
                                 ) = new HttpRequestBuilder[C, M](url, httpVerb, data, params, headers)
}

class HttpClient[C, M[_]](
                           implicit urls: KongApiUrls,
                           httpRbImpl: HttpRequestBuilderExtension[C, M]) {

  import HttpRequestBuilder._

  val defaultPagination = Pagination(1, 1000)

  def get[A](
              accessToken: Option[String] = None,
              method: String,
              headers: Map[String, String] = Map.empty,
              params: Map[String, String] = Map.empty,
              pagination: Option[Pagination] = None
            )(implicit D: Decoder[A]): M[KResponse[A]] =
    httpRbImpl.run[A](
      httpRequestBuilder(buildURL(method))
        .withHeaders(headers)
        .withParams(params ++ pagination.fold(Map.empty[String, String])(p ⇒
          Map("page" → p.page.toString, "per_page" → p.per_page.toString))))

  def patch[A](
                accessToken: Option[String] = None,
                method: String,
                headers: Map[String, String] = Map.empty,
                data: String)(implicit D: Decoder[A]): M[KResponse[A]] =
    httpRbImpl.run[A](
      httpRequestBuilder(buildURL(method)).patchMethod
        .withHeaders(headers)
        .withData(data))

  def put[A](
              accessToken: Option[String] = None,
              url: String,
              headers: Map[String, String] = Map(),
              data: String)(implicit D: Decoder[A]): M[KResponse[A]] =
    httpRbImpl.run[A](
      httpRequestBuilder(buildURL(url)).putMethod
        .withHeaders(headers)
        .withData(data))

  def post[A](
               accessToken: Option[String] = None,
               url: String,
               headers: Map[String, String] = Map.empty,
               data: String
             )(implicit D: Decoder[A]): M[KResponse[A]] =
    httpRbImpl.run[A](
      httpRequestBuilder(buildURL(url)).postMethod
        .withHeaders(headers)
        .withData(data))

  def postAuth[A](
                   method: String,
                   headers: Map[String, String] = Map.empty,
                   data: String
                 )(implicit D: Decoder[A]): M[KResponse[A]] =
    httpRbImpl.run[A](
      httpRequestBuilder(buildURL(method)).postMethod.withHeaders(headers).withData(data))

  def postOAuth[A](
                    url: String,
                    headers: Map[String, String] = Map.empty,
                    data: String
                  )(implicit D: Decoder[A]): M[KResponse[A]] =
    httpRbImpl.run[A](
      httpRequestBuilder(url).postMethod
        .withHeaders(Map("Accept" → "application/json") ++ headers)
        .withData(data))

  def delete(
              accessToken: Option[String] = None,
              method: String,
              headers: Map[String, String] = Map.empty): M[KResponse[Unit]] =
    httpRbImpl.runEmpty(
      httpRequestBuilder(buildURL(method)).deleteMethod.withHeaders(headers))

  private def buildURL(method: String) = urls.baseUrl + method

  val defaultPage: Int = 1
  val defaultPerPage: Int = 30
}
