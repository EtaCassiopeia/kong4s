package kong4s.free.domain

case class Api(
                name: String,
                hosts: List[String],
                methods: List[String],
                upstreamUrl: String,
                stripUri: Boolean = true,
                preserveHost: Boolean = false,
                retries: Int = 5,
                upstreamConnectTimeout: Int = 60000,
                upstreamSendTimeout: Int = 60000,
                upstreamReadTimeout: Int = 60000,
                httpPnly: Boolean = false,
                httpIfTerminated: Boolean = true
              )