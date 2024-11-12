package com.example.plugins

import com.example.plugins.routing.brandRoutes
import com.example.repositories.brand.PostgresBrandRepository
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*

fun Application.configureSerialization(
    brandRepository: PostgresBrandRepository
) {
    install(ContentNegotiation) {
        json()
    }
    routing {
        brandRoutes(brandRepository)
    }
}
