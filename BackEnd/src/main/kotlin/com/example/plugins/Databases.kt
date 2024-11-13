package com.example.plugins

import com.example.mapping.Brands
import com.example.mapping.BrandsCategories
import com.example.mapping.Categories
import com.example.mapping.Products
import io.ktor.server.application.*
import io.ktor.server.config.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection
import java.sql.DriverManager

fun Application.configureDatabases(config: ApplicationConfig) {
    // Dados devem estar batendo com os do docker-compose.yml
    val url = config.property("storage.jdbcURL").getString()
    val user = config.property("storage.user").getString()
    val password = config.property("storage.password").getString()

    Database.connect(
        url,
        user = user,
        password = password
    )

    // Transaction é executado no momento que o servidor é iniciado
    // Da pra fazer muitas coisas legais, como verificar o estado do BD e inserir dados iniciais
    transaction {
        // Criação das tabelas
        SchemaUtils.create(Brands)
        SchemaUtils.create(Categories)
        SchemaUtils.create(BrandsCategories)
        SchemaUtils.create(Products)
    }
}

// Função responsável por executar o "block" numa transação de banco de dados usando
// o Dispatchers.IO, que é um dispatcher otimizado para operações de I/O.
// Resolvi colocar ela aqui porque ela vai ser utilizado em todas as classes Repositories, então basta instanciar
// Kotlin Coroutine é lindo né rs
suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)

// Função padrão para conectar com o BD
fun Application.connectToPostgres(embedded: Boolean): Connection {
    Class.forName("org.postgresql.Driver")
    if (embedded) {
        return DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "root", "")
    } else {
        val url = environment.config.property("postgres.url").getString()
        val user = environment.config.property("postgres.user").getString()
        val password = environment.config.property("postgres.password").getString()

        return DriverManager.getConnection(url, user, password)
    }
}
