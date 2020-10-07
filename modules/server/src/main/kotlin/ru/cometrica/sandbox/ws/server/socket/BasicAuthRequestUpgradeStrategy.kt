package ru.cometrica.sandbox.ws.server.socket

import mu.KotlinLogging
import org.springframework.core.io.buffer.NettyDataBufferFactory
import org.springframework.http.server.reactive.AbstractServerHttpResponse
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.http.server.reactive.ServerHttpResponseDecorator
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.HandshakeInfo
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.adapter.ReactorNettyWebSocketSession
import org.springframework.web.reactive.socket.server.upgrade.ReactorNettyRequestUpgradeStrategy
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import reactor.netty.http.server.HttpServerResponse
import reactor.netty.http.websocket.WebsocketInbound
import reactor.netty.http.websocket.WebsocketOutbound
import ru.cometrica.sandbox.ws.server.auth.TestingAuthenticationManager
import ru.cometrica.sandbox.ws.server.retrieveTestingToken
import java.util.function.Supplier

@Component
class BasicAuthRequestUpgradeStrategy(
    private val authenticationManager: TestingAuthenticationManager
) : ReactorNettyRequestUpgradeStrategy() {

    private companion object {
        val logger = KotlinLogging.logger {}
    }

    override fun upgrade(
        exchange: ServerWebExchange,
        handler: WebSocketHandler,
        subProtocol: String?,
        handshakeInfoFactory: Supplier<HandshakeInfo>
    ): Mono<Void> {
        val response = exchange.response
        val reactorResponse: HttpServerResponse = getNativeResponse(response)
        val handshakeInfo: HandshakeInfo = handshakeInfoFactory.get()
        val bufferFactory = response.bufferFactory() as NettyDataBufferFactory
        val headers = handshakeInfo.headers
        logger.debug { "About a WebSocket session upgrading, headers [$headers]...." }
        val token =
            retrieveTestingToken(exchange.request)?.let {
                Mono.just(it)
            } ?: Mono.empty()
        return token
            .doOnNext {
                logger.debug { "token: $it" }
            }
            // todo сюда попадаем после аутентифицикации. но если уберем вебсокеты из фильтра, то можно здесь аутертифицировать
            //.flatMap {
            // authenticationManager.authenticate(it)
            //}
            .flatMap { authToken ->
                response.setComplete()
                    .then(Mono.defer {
                        reactorResponse.sendWebsocket(subProtocol, maxFramePayloadLength)
                        { `in`: WebsocketInbound, out: WebsocketOutbound ->
                            val session = ReactorNettyWebSocketSession(
                                `in`, out, handshakeInfo, bufferFactory, maxFramePayloadLength
                            )
                            session.attributes["userId"] = authToken.name
                            handler.handle(session)
                        }
                    })
            }

    }

    /**
     * @see [ReactorNettyRequestUpgradeStrategy]
     */
    private fun getNativeResponse(response: ServerHttpResponse): HttpServerResponse =
        when (response) {
            is AbstractServerHttpResponse -> response.getNativeResponse()
            is ServerHttpResponseDecorator -> getNativeResponse(response.delegate)
            else -> throw IllegalArgumentException("Couldn't find native response in [$response]")
        }

}
