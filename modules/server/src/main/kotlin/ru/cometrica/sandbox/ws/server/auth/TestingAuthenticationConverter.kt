package ru.cometrica.sandbox.ws.server.auth

import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.cast
import ru.cometrica.sandbox.ws.server.retrieveTestingToken

/**
 *  ServerAuthenticationConverter used by [org.springframework.security.web.server.authentication.AuthenticationWebFilter]
 *  to convert [ServerWebExchange] into [Authentication].
 *
 *  This converter use [retrieveTestingToken] to extract [TestingAuthenticationToken].
 */
@Component
class TestingAuthenticationConverter : ServerAuthenticationConverter {

    override fun convert(exchange: ServerWebExchange): Mono<Authentication> =
        retrieveRequestSession(exchange.request)
            .cast()

    private fun retrieveRequestSession(request: ServerHttpRequest): Mono<TestingAuthenticationToken> =
        Mono.justOrEmpty(retrieveTestingToken(request))
}
