package com.example

import com.example.plugins.configureDatabases
import com.example.plugins.configureSecurity
import com.example.plugins.configureSerialization
import com.example.repositories.brand.PostgresBrandRepository
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    // Instâncias da pasta de plugins
    configureSecurity()
    configureSerialization(PostgresBrandRepository())
    // "enviroment.config" coleta os dados do arquivo application.yaml
    // Dentro de plugins/Database.kt é feita a conexão com o banco de dados
    configureDatabases(environment.config)
}
