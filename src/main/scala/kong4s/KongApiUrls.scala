package kong4s

case class KongApiUrls(baseUrl: String)

object KongDefaultUrls {

  implicit val defaultUrls: KongApiUrls = KongApiUrls(
    "http://192.168.99.100:8001/"
  )
}
