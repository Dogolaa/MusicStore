package com.musicstore

import com.musicstore.plugins.configureDatabases
import com.musicstore.plugins.configureSecurity
import com.musicstore.plugins.configureSerialization
import com.musicstore.repositories.brand.PostgresBrandRepository
import com.musicstore.repositories.product.PostgresProductRepository
import com.musicstore.repositories.role.PostgresRoleRepository
import com.musicstore.repositories.user.PostgresUserRepository
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
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
