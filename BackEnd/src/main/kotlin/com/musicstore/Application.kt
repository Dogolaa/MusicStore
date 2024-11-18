package com.musicstore

import com.musicstore.plugins.configureDatabases
import com.musicstore.plugins.configureSecurity
import com.musicstore.plugins.configureSerialization
import com.musicstore.repositories.brand.PostgresBrandRepository
import com.musicstore.repositories.product.PostgresProductRepository
import com.musicstore.repositories.role.PostgresRoleRepository
import com.musicstore.repositories.user.PostgresUserRepository
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.CORS

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)

        // Cabeçalhos permitidos
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        allowHeader("api-key")

        // Permitir credenciais (se necessário)
        anyHost()
    }

    // Instâncias da pasta de plugins
    configureSecurity(
        PostgresUserRepository(),
        PostgresRoleRepository()
    )

    configureSerialization(
        PostgresBrandRepository(),
        PostgresProductRepository(PostgresBrandRepository()),
        PostgresUserRepository(),
        PostgresRoleRepository()
    )

    configureDatabases(environment.config)
}
