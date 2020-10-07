package ru.cometrica.sandbox.ws.server.processing

data class UpdateRequest(
    val clientId: Long,
    val newValue: String
)