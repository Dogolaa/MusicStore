package com.musicstore

import com.musicstore.plugins.configureDatabases
import com.musicstore.plugins.configureSecurity
import com.musicstore.plugins.configureSerialization
import com.musicstore.repositories.brand.PostgresBrandRepository
import com.musicstore.repositories.product.PostgresProductRepository
import com.musicstore.repositories.user.PostgresUserRepository
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    // Instâncias da pasta de plugins
    configureSecurity()
    configureSerialization(
        PostgresBrandRepository(),
        PostgresProductRepository(PostgresBrandRepository()),
        PostgresUserRepository()
    )
    // "enviroment.config" coleta os dados do arquivo application.yaml
    // Dentro de plugins/Database.kt é feita a conexão com o banco de dados
    configureDatabases(environment.config)
}
