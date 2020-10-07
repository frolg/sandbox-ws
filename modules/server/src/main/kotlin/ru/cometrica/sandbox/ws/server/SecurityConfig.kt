package ru.cometrica.sandbox.ws.server

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import ru.cometrica.sandbox.ws.server.auth.TestingAuthenticationConverter
import ru.cometrica.sandbox.ws.server.auth.TestingAuthenticationManager

@EnableWebFluxSecurity
class SecurityConfig {
    @Autowired
    lateinit var authenticationManager: TestingAuthenticationManager

    @Autowired
    lateinit var testingAuthenticationConverter: TestingAuthenticationConverter

    @Bean
    fun configure(http: ServerHttpSecurity): SecurityWebFilterChain {
        http
            .csrf().disable()
            .requestCache().disable()
            .addFilterBefore(authenticationWebFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
            .authorizeExchange()
            .anyExchange()
            .authenticated()
        return http.build()
    }

    fun authenticationWebFilter(): AuthenticationWebFilter =
        AuthenticationWebFilter(authenticationManager).apply {
            this.setServerAuthenticationConverter(testingAuthenticationConverter)
        }

}