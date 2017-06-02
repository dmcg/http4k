package org.http4k.routing

import org.http4k.core.ContentType
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.UriTemplate
import org.http4k.core.UriTemplate.Companion.from

data class Route(val method: Method, val template: UriTemplate, val handler: HttpHandler)

interface RoutingHttpHandler : Router, HttpHandler {
    fun withBasePath(basePath: String): RoutingHttpHandler
}

fun routes(vararg routes: Route): RoutingHttpHandler = GroupRoutingHttpHandler(null, routes.asList())

fun routes(first: Router, vararg then: Router): HttpHandler = then.fold(first) { memo, next -> memo.then(next) }.toHttpHandler()

fun static(resourceLoader: ResourceLoader = ResourceLoader.Classpath(), vararg extraPairs: Pair<String, ContentType>): RoutingHttpHandler =
    StaticRouter(StaticHttpHandler("", resourceLoader, extraPairs.asList().toMap()))

fun Request.path(name: String): String? = uriTemplate().extract(uri.toString())[name]

infix fun Pair<Method, String>.by(action: HttpHandler): Route = Route(first, from(second), action)

infix fun String.by(router: RoutingHttpHandler): RoutingHttpHandler = router.withBasePath(this)
