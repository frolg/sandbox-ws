package ru.cometrica.sandbox.ws.client

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.HttpHeaders
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient
import java.net.URI

@SpringBootApplication
class WsClientApplication

fun main(args: Array<String>) {
    val client = ReactorNettyWebSocketClient()
    val headers = HttpHeaders()
    headers[HttpHeaders.AUTHORIZATION] = "1001"
    client
        .execute(URI.create("ws://localhost:8080/ws/chat/push"), headers) { session ->
            session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .log()
                .then()
        }
        .subscribe()
    runApplication<WsClientApplication>(*args)
}