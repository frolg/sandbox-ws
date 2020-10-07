package ru.cometrica.sandbox.ws.server.auth

import mu.KotlinLogging
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.cast
import reactor.kotlin.core.publisher.switchIfEmpty

@Component
class TestingAuthenticationManager: ReactiveAuthenticationManager {

    private companion object {
        val logger = KotlinLogging.logger {}
    }

    private val userIds = listOf("1001", "1002")

    override fun authenticate(authentication: Authentication): Mono<Authentication> {
        logger.debug { "About to authenticate [$authentication]......." }
        val token: TestingAuthenticationToken = authentication as? TestingAuthenticationToken ?: return Mono.empty()
        return Mono.just(token)
            .filter {
                it.name in userIds
            }
            .cast<Authentication>()
            .switchIfEmpty {
                logger.debug { "Can't authenticate [$token]" }
                Mono.empty()
            }
            .doOnError {
                logger.warn { "Error authenticate [$token]: $it" }
            }
            .doOnNext {
                logger.trace { "Success authenticate [$it]" }
            }
    }

}