package com.example.plugins

import com.example.mapping.Brands
import com.example.mapping.BrandsCategories
import com.example.mapping.Categories
import com.example.mapping.Products
import com.example.mapping.Roles
import com.example.mapping.Users
import com.example.mapping.UsersRoles
import io.ktor.server.application.*
import io.ktor.server.config.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
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
        SchemaUtils.create(
            Brands,
            Categories,
            BrandsCategories,
            Products,
            Users,
            Roles,
            UsersRoles
        )

        intializeData()

    }
}

fun intializeData() {
    if (Users.selectAll().empty()) {
        Users.insert {
            it[user_email] = "fulano@gmail.com"
            it[user_name] = "fulano"
            it[user_last_name] = "Fulano de Souza"
            it[user_password] = "senha123"
            it[user_status] = 1
            it[user_ph_content] = "teste"
        }
    }

    if (Brands.selectAll().empty()) {
        Brands.insert {
            it[brand_name] = "Nome teste"
            it[brand_ph_content] = "D"
        }
    }

    if (Products.selectAll().empty()) {
        Products.insert {
            it[id_brand] = 1 // ID da marca de exemplo, que deve existir na tabela Brands
            it[product_name] = "Produto Teste"
            it[product_main_photo] = "D" // Substitua com os dados do blob de foto
            it[product_short_desc] = "Descrição curta de teste"
            it[product_long_desc] = "Descrição longa de teste"
            it[product_price] = 123.45f
            it[product_discount] = 10.0f
            it[product_status] = "Disponível"
            it[product_has_stocks] = "Sim"
            it[product_width] = 10.0f
            it[product_lenght] = 20.0f
            it[product_height] = 5.0f
            it[product_cost] = 50.0f
            it[product_creation_time] = "2024-01-01T12:00:00"
            it[product_update_time] = "2024-01-02T12:00:00"
        }

        Products.insert {
            it[id_brand] = 1 // Exemplo de outro ID da marca existente
            it[product_name] = "Produto Exemplo 2"
            it[product_main_photo] = "MMM" // Exemplo de dados de foto
            it[product_short_desc] = "Outra descrição curta"
            it[product_long_desc] = "Outra descrição longa para o produto exemplo 2"
            it[product_price] = 299.99f
            it[product_discount] = 15.0f
            it[product_status] = "Indisponível"
            it[product_has_stocks] = "Não"
            it[product_width] = 15.0f
            it[product_lenght] = 25.0f
            it[product_height] = 8.0f
            it[product_cost] = 120.0f
            it[product_creation_time] = "2024-02-01T14:30:00"
            it[product_update_time] = "2024-02-02T15:45:00"
        }
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
