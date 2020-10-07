package ru.cometrica.sandbox.ws.server.socket

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.cometrica.sandbox.ws.server.event.EventService

@Component
class ReactiveWebSocketHandler(
    private val mapper: ObjectMapper,
    private val eventService: EventService
) : WebSocketHandler {

    private companion object {
        val logger = KotlinLogging.logger {}
    }

    override fun handle(session: WebSocketSession): Mono<Void> {
        logger.debug { "[${session.id}] About websocket handling: session attributes: ${session.attributes}........" }
        val messages: Flux<WebSocketMessage> =
            eventService.getEvents()
                .filter {
                    val requiresSending = it.relateToClient == session.attributes["userId"]?.toString()?.toLong()
                    logger.debug { "[${session.id}] Incoming event [$it], Should be sent: $requiresSending" }
                    requiresSending
                }
                .map { event ->
                    mapper.writeValueAsString(event)
                }
                .map { s: String ->
                    session.textMessage(s)
                }
        return session.send(messages)
    }
}