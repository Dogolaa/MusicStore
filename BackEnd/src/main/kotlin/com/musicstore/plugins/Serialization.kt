package com.musicstore.plugins

import com.musicstore.repositories.brand.PostgresBrandRepository
import com.musicstore.repositories.product.PostgresProductRepository
import com.musicstore.repositories.role.PostgresRoleRepository
import com.musicstore.repositories.user.PostgresUserRepository
import com.musicstore.routes.brandRoute
import com.musicstore.routes.productRoute
import com.musicstore.routes.roleRoute
import com.musicstore.routes.userRoute
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

        brandRoute(brandRepository)
        productRoute(productRepository)
        userRoute(userRepository)
        roleRoute(roleRepository)
    }
}
