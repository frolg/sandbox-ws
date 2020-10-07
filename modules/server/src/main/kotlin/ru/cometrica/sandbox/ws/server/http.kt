package ru.cometrica.sandbox.ws.server

import mu.KotlinLogging
import org.springframework.http.HttpHeaders
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.security.authentication.TestingAuthenticationToken

private val logger = KotlinLogging.logger {}

/** Retrieve session id from request and create intermediate Authentication object */
fun retrieveTestingToken(request: ServerHttpRequest): TestingAuthenticationToken? {
    logger.debug { "headers: [${request.headers}]" }
    logger.debug { "cookies: [${request.cookies}]" }

    // 1. Search for token on header
    request.headers.getFirst(HttpHeaders.AUTHORIZATION)?.let {
        logger.debug { "[${request.id}] Auth token found in Header" }
        return TestingAuthenticationToken(it, "password", "ROLE_USER")
    }

    // 2. Search for token on cookies
    request.cookies.getFirst(HttpHeaders.AUTHORIZATION)?.let {
        logger.debug { "[${request.id}] Auth token found in Cookie" }
        return TestingAuthenticationToken(it.value, "password", "ROLE_USER")
    }

    // 3. Search for token on 'Sec-WebSocket-Protocol' header
    request.headers.getFirst("Sec-WebSocket-Protocol")?.let {
        logger.debug { "[${request.id}] Auth token found in 'Sec-WebSocket-Protocol' Header" }
        return TestingAuthenticationToken(it, "password", "ROLE_USER")
    }

    logger.debug { "[${request.id}] No SessionId found in request" }

    return null
}