package ru.cometrica.sandbox.ws.server.processing

import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import ru.cometrica.sandbox.ws.server.event.EventService
import ru.cometrica.sandbox.ws.server.event.UpdateEvent

@Component
class UpdateService(
    private val eventService: EventService
) {

    fun update(request: UpdateRequest): Mono<String> {
        eventService.onNext(UpdateEvent(request.clientId, request.newValue))
        return Mono.just("updated value: ${request.newValue}")
    }
}