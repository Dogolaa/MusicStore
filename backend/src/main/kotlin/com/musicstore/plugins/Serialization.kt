package com.musicstore.plugins

import com.musicstore.repositories.brand.PostgresBrandRepository
import com.musicstore.repositories.category.PostgresCategoryRepository
import com.musicstore.repositories.product.PostgresProductRepository
import com.musicstore.repositories.role.PostgresRoleRepository
import com.musicstore.repositories.user.PostgresUserRepository
import com.musicstore.routes.brandRoute
import com.musicstore.routes.categoryRoute
import com.musicstore.routes.productRoute
import com.musicstore.routes.roleRoute
import com.musicstore.routes.userRoute
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.http.content.staticFiles
import io.ktor.server.http.content.staticResources
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import java.io.File

fun Application.configureSerialization(
    brandRepository: PostgresBrandRepository,
    productRepository: PostgresProductRepository,
    userRepository: PostgresUserRepository,
    roleRepository: PostgresRoleRepository,
    categoryRepository: PostgresCategoryRepository
) {
    install(ContentNegotiation) {
        json()
    }

    routing {
        staticFiles("/images", File("images"))
        staticResources("/static", "static", index = "index.html")


        brandRoute(brandRepository)
        productRoute(productRepository)
        userRoute(userRepository)
        roleRoute(roleRepository)
        categoryRoute(categoryRepository)
    }
}
