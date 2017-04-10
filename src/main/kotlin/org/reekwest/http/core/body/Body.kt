package org.reekwest.http.core.body

import org.reekwest.http.core.HttpMessage
import org.reekwest.http.core.Request
import org.reekwest.http.core.Response
import java.nio.ByteBuffer

typealias Body = ByteBuffer

fun Request.body(body: Body) = copy(body = body)
fun Response.body(body: Body) = copy(body = body)

interface BodyRepresentation<T> {
    fun from(body: Body?): T
    fun to(value: T): Body
}

fun <T> HttpMessage.extract(representation: BodyRepresentation<T>): T = representation.from(body)
