package ru.cometrica.sandbox.ws.server.processing

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/rest/update")
class UpdateController(
    val updateService: UpdateService
) {

    @RequestMapping(
        value = ["/", ""],
        method = [RequestMethod.POST],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.TEXT_PLAIN_VALUE]
    )
    fun create(@RequestBody request: UpdateRequest): Mono<String> {
        return updateService.update(request)
    }
}