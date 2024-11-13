package com.example.plugins

import com.example.plugins.routing.brandRoutes
import com.example.plugins.routing.productRoutes
import com.example.repositories.brand.PostgresBrandRepository
import com.example.repositories.product.PostgresProductRepository
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.http.content.staticResources
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*

fun Application.configureSerialization(
    brandRepository: PostgresBrandRepository,
    productRepository: PostgresProductRepository
) {
    install(ContentNegotiation) {
        json()
    }
    routing {
        staticResources("/static", "static")

        brandRoutes(brandRepository)
        productRoutes(productRepository)
    }
}
