package cookbook.nanoservices

import org.http4k.chaos.ChaosBehaviours.Latency
import org.http4k.chaos.ChaosTriggers.Always
import org.http4k.chaos.appliedWhen
import org.http4k.chaos.withChaosControls
import org.http4k.client.JavaHttpClient
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.then
import org.http4k.filter.RequestFilters.ProxyHost
import org.http4k.filter.RequestFilters.ProxyProtocolMode.Https
import org.http4k.server.SunHttp
import org.http4k.server.asServer

fun `latency injection proxy (between 100ms-500ms)`() =
        ProxyHost(Https)
                .then(JavaHttpClient())
                .withChaosControls(Latency().appliedWhen(Always))
                .asServer(SunHttp())
                .start()

fun main(args: Array<String>) {
    System.setProperty("http.proxyHost", "localhost")
    System.setProperty("http.proxyPort", "8000")
    System.setProperty("http.nonProxyHosts", "localhost")

    `latency injection proxy (between 100ms-500ms)`().use {
        println(JavaHttpClient()(Request(POST, "http://localhost:8000/chaos/activate")))
        println(JavaHttpClient()(Request(GET, "http://github.com/")).header("X-http4k-chaos"))
    }
}
