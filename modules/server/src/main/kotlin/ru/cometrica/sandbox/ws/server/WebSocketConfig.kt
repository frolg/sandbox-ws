package ru.cometrica.sandbox.ws.server

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.server.WebSocketService
import org.springframework.web.reactive.socket.server.support.HandshakeWebSocketService
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter
import ru.cometrica.sandbox.ws.server.socket.BasicAuthRequestUpgradeStrategy
import ru.cometrica.sandbox.ws.server.socket.ReactiveWebSocketHandler

@Configuration
class WebSocketConfig {

    @Autowired
    lateinit var reactiveWebSocketHandler: ReactiveWebSocketHandler

    @Autowired
    lateinit var upgradeStrategy: BasicAuthRequestUpgradeStrategy

    @Bean
    fun handlerMapping(): HandlerMapping {
        val map = mapOf("/ws/chat/push" to reactiveWebSocketHandler)
        val order = -1 // before annotated controllers
        return SimpleUrlHandlerMapping(map, order)
    }

    @Bean
    fun handlerAdapter() =  WebSocketHandlerAdapter(webSocketService())

    @Bean
    fun webSocketService(): WebSocketService {
        return HandshakeWebSocketService(upgradeStrategy)
    }

}