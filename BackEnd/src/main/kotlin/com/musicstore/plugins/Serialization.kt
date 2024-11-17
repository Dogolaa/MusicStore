package com.musicstore.plugins

import com.musicstore.routing.brandRoutes
import com.musicstore.routing.productRoutes
import com.musicstore.routing.userRoutes
import com.musicstore.repositories.brand.PostgresBrandRepository
import com.musicstore.repositories.product.PostgresProductRepository
import com.musicstore.repositories.role.PostgresRoleRepository
import com.musicstore.repositories.user.PostgresUserRepository
import com.musicstore.routing.roleRoutes
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.http.content.staticResources
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*

fun Application.configureSerialization(
    brandRepository: PostgresBrandRepository,
    productRepository: PostgresProductRepository,
    userRepository: PostgresUserRepository,
    roleRepository: PostgresRoleRepository
) {
    install(ContentNegotiation) {
        json()
    }

    routing {
        staticResources("/images", "images")
        staticResources("/static", "static")

        brandRoutes(brandRepository)
        productRoutes(productRepository)
        userRoutes(userRepository)
        roleRoutes(roleRepository)
    }
}
