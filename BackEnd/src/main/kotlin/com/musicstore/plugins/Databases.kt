package com.musicstore.plugins

import com.musicstore.mapping.Brands
import com.musicstore.mapping.BrandsCategories
import com.musicstore.mapping.Categories
import com.musicstore.mapping.Products
import com.musicstore.mapping.ProductsCategories
import com.musicstore.mapping.Roles
import com.musicstore.mapping.Users
import com.musicstore.mapping.UsersRoles
import io.ktor.server.config.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

fun configureDatabases(config: ApplicationConfig) {
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
            UsersRoles,
            ProductsCategories
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

//    if (Products.selectAll().empty()) {
//        for (i in 10..30) {
//            Products.insert {
//                it[id_brand] = 1
//                it[product_name] = "Produto Exemplo $i"
//                it[product_main_photo] = "vinil_ariana.jpeg"
//                it[product_short_desc] = "Descrição curta para produto $i"
//                it[product_long_desc] = "Descrição longa para produto $i"
//                it[product_price] = (100 + i * 5).toFloat()
//                it[product_discount] = (5 * i).toFloat()
//                it[product_status] = if (i % 2 == 0) "Disponível" else "Indisponível"
//                it[product_has_stocks] = if (i % 2 == 0) "Sim" else "Não"
//                it[product_width] = (10 + i).toFloat()
//                it[product_lenght] = (20 + i).toFloat()
//                it[product_height] = (5 + i).toFloat()
//                it[product_cost] = (40 + i * 2).toFloat()
//                it[product_creation_time] = "2024-01-01T12:00:${i * 2}"
//                it[product_update_time] = "2024-01-02T15:00:${i * 3}"
//            }
//        }
//    }
}

// Função responsável por executar o "block" numa transação de banco de dados usando
// o Dispatchers.IO, que é um dispatcher otimizado para operações de I/O.
// Resolvi colocar ela aqui porque ela vai ser utilizado em todas as classes Repositories, então basta instanciar
// Kotlin Coroutine é lindo né rs
suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)

