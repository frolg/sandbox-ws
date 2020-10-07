package ru.cometrica.sandbox.ws.server.event

import org.springframework.stereotype.Component
import reactor.core.publisher.EmitterProcessor
import reactor.core.publisher.Flux

@Component
class EventService {
    private val events: EmitterProcessor<UpdateEvent> = EmitterProcessor.create(100, true)

    fun onNext(event: UpdateEvent) {
        events.onNext(event)
    }

    fun getEvents(): Flux<UpdateEvent> =
        Flux.from(events)
}