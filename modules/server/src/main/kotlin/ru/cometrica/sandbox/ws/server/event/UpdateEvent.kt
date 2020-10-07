package ru.cometrica.sandbox.ws.server.event

data class UpdateEvent (
    val relateToClient: Long,
    val newValue: String
)