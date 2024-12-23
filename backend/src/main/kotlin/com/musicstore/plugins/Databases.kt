package com.musicstore.plugins

import com.musicstore.mapping.BrandCategoryTable
import com.musicstore.mapping.BrandTable
import com.musicstore.mapping.CategoryTable
import com.musicstore.mapping.ProductCategoryTable
import com.musicstore.mapping.ProductTable
import com.musicstore.mapping.RoleTable
import com.musicstore.mapping.UserRoleTable
import com.musicstore.mapping.UserTable
import io.ktor.server.application.*
import io.ktor.server.config.*
import kotlinx.coroutines.Dispatchers
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabases(config: ApplicationConfig) {
    val url = config.property("storage.jdbcURL").getString()
    val user = config.property("storage.user").getString()
    val password = config.property("storage.password").getString()

    Database.connect(
        url,
        user = user,
        password = password
    )

    transaction {
        // Criação das tabelas
        SchemaUtils.create(
            BrandTable,
            CategoryTable,
            BrandCategoryTable,
            ProductTable,
            ProductCategoryTable,
            UserTable,
            RoleTable,
            UserRoleTable,
        )

        val initializeData = config.propertyOrNull("storage.initializeData")?.getString()?.toBoolean() != false

        if (initializeData) {
            intializeData()
        }


    }
}

fun intializeData() {
    if (UserTable.selectAll().empty()) {
        UserTable.insert {
            it[user_email] = "lucas@gmail.com"
            it[user_name] = "Lucas"
            it[user_last_name] = "Souza"
            it[user_password] = "senha123".hashPassword()
            it[user_status] = 1
            it[user_ph_content] = "teste1"
        }
        UserTable.insert {
            it[user_email] = "caio@gmail.com"
            it[user_name] = "Caio"
            it[user_last_name] = "Souza"
            it[user_password] = "senha123".hashPassword()
            it[user_status] = 1
            it[user_ph_content] = "teste2"
        }
        UserTable.insert {
            it[user_email] = "ana@gmail.com"
            it[user_name] = "Ana"
            it[user_last_name] = "Souza"
            it[user_password] = "senha123".hashPassword()
            it[user_status] = 1
            it[user_ph_content] = "teste3"
        }
        UserTable.insert {
            it[user_email] = "julia@gmail.com"
            it[user_name] = "Julia"
            it[user_last_name] = "Souza"
            it[user_password] = "senha123".hashPassword()
            it[user_status] = 1
            it[user_ph_content] = "teste4"
        }
        UserTable.insert {
            it[user_email] = "joao@gmail.com"
            it[user_name] = "João"
            it[user_last_name] = "Souza"
            it[user_password] = "senha123".hashPassword()
            it[user_status] = 1
            it[user_ph_content] = "teste5"
        }
//        UserTable.insert {
//            it[user_email] = ""
//            it[user_name] = "Postman"
//            it[user_last_name] = ""
//            it[user_password] = "postman".hashPassword()
//            it[user_status] = 1
//            it[user_ph_content] = ""
//        }
    }

    if (RoleTable.selectAll().empty()) {
        RoleTable.insert {
            it[role_name] = "ADMIN"
            it[role_description] = "Admin"
        }
        RoleTable.insert {
            it[role_name] = "SELLER"
            it[role_description] = "Seller"
        }
        RoleTable.insert {
            it[role_name] = "SHIPPER"
            it[role_description] = "Shipper"
        }
        RoleTable.insert {
            it[role_name] = "CUSTOMER"
            it[role_description] = "Customer"
        }
    }

    if (UserRoleTable.selectAll().empty()) {
        UserRoleTable.insert {
            it[id_user] = 1
            it[id_role] = 1
        }
        UserRoleTable.insert {
            it[id_user] = 2
            it[id_role] = 2
        }
        UserRoleTable.insert {
            it[id_user] = 3
            it[id_role] = 3
        }
        UserRoleTable.insert {
            it[id_user] = 4
            it[id_role] = 4
        }
        UserRoleTable.insert {
            it[id_user] = 5
            it[id_role] = 2
        }
        UserRoleTable.insert {
            it[id_user] = 5
            it[id_role] = 3
        }
    }

    if (BrandTable.selectAll().empty()) {
        BrandTable.insert {
            it[brand_name] = "Universal Music"
            it[brand_ph_content] = "fotouniversal"
        }
        BrandTable.insert {
            it[brand_name] = "Sony Music"
            it[brand_ph_content] = "fotosony"
        }
        BrandTable.insert {
            it[brand_name] = "Warner Music"
            it[brand_ph_content] = "fotowarner"
        }
        BrandTable.insert {
            it[brand_name] = "EMI"
            it[brand_ph_content] = "fotoemi"
        }
    }

    if (CategoryTable.selectAll().empty()) {
        CategoryTable.insert {
            it[category_name] = "CD"
            it[category_ph_content] = "cd"
        }
        CategoryTable.insert {
            it[category_name] = "DVD"
            it[category_ph_content] = "dvd"
        }
        CategoryTable.insert {
            it[category_name] = "Vinil"
            it[category_ph_content] = "vinil"
        }
        CategoryTable.insert {
            it[category_name] = "Multimidia"
            it[category_ph_content] = "multimidia"
        }
    }

    if (BrandCategoryTable.selectAll().empty()) {
        BrandCategoryTable.insert {
            it[id_brand] = 1
            it[id_category] = 1
        }
        BrandCategoryTable.insert {
            it[id_brand] = 2
            it[id_category] = 2
        }
        BrandCategoryTable.insert {
            it[id_brand] = 3
            it[id_category] = 3
        }
        BrandCategoryTable.insert {
            it[id_brand] = 4
            it[id_category] = 4
        }
    }

    if (ProductTable.selectAll().empty()) {
        ProductTable.insert {
            it[id_brand] = 1
            it[product_name] = "Vinil - Dark Side of the Moon"
            it[product_main_photo] = "1732013987835-5b70cafb-61cb-43db-8ba1-b50061e4e2c6.png"
            it[product_short_desc] = "Clássico do Pink Floyd"
            it[product_long_desc] =
                "Vinil remasterizado do álbum 'Dark Side of the Moon' do Pink Floyd, um dos álbuns mais icônicos da história da música."
            it[product_price] = 49.99f
            it[product_discount] = 5.0f
            it[product_status] = 1
            it[product_has_stocks] = 1
            it[product_width] = 31.0f
            it[product_lenght] = 31.0f
            it[product_height] = 0.5f
            it[product_cost] = 20.0f
            it[product_creation_time] = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        }

        ProductTable.insert {
            it[id_brand] = 2
            it[product_name] = "DVD - The Beatles Anthology"
            it[product_main_photo] = "1732013983394-57f785bb-b587-410c-9db8-85a89159e69e.png"
            it[product_short_desc] = "Documentário sobre os Beatles"
            it[product_long_desc] =
                "Box com 5 DVDs que conta a história dos Beatles, com entrevistas, músicas e imagens raras."
            it[product_price] = 69.99f
            it[product_discount] = 10.0f
            it[product_status] = 1
            it[product_has_stocks] = 1
            it[product_width] = 19.0f
            it[product_lenght] = 13.5f
            it[product_height] = 3.0f
            it[product_cost] = 30.0f
            it[product_creation_time] = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        }

        ProductTable.insert {
            it[id_brand] = 3
            it[product_name] = "Vinil - Thriller (Michael Jackson)"
            it[product_main_photo] = "1732014000199-3cf5d4ce-f280-4dc9-b826-f9ead47c5086.png"
            it[product_short_desc] = "O álbum mais vendido de todos os tempos"
            it[product_long_desc] =
                "Edição especial do vinil de 'Thriller', de Michael Jackson, com áudio remasterizado e encarte."
            it[product_price] = 59.99f
            it[product_discount] = 0.0f
            it[product_status] = 1
            it[product_has_stocks] = 1
            it[product_width] = 31.0f
            it[product_lenght] = 31.0f
            it[product_height] = 0.5f
            it[product_cost] = 25.0f
            it[product_creation_time] = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        }

        ProductTable.insert {
            it[id_brand] = 4
            it[product_name] = "DVD - Nirvana Live at Reading"
            it[product_main_photo] = "1732013978264-81bf89b8-24ab-42c0-a789-a6c8bf69d67d.png"
            it[product_short_desc] = "Show histórico do Nirvana"
            it[product_long_desc] =
                "Gravação ao vivo do lendário show do Nirvana no festival de Reading, em 1992, com qualidade digital."
            it[product_price] = 39.99f
            it[product_discount] = 15.0f
            it[product_status] = 1
            it[product_has_stocks] = 1
            it[product_width] = 19.0f
            it[product_lenght] = 13.5f
            it[product_height] = 1.5f
            it[product_cost] = 18.0f
            it[product_creation_time] = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        }
    }

    if (ProductCategoryTable.selectAll().empty()) {
        ProductCategoryTable.insert {
            it[id_product] = 1
            it[id_category] = 1
        }
        ProductCategoryTable.insert {
            it[id_product] = 2
            it[id_category] = 2
        }
        ProductCategoryTable.insert {
            it[id_product] = 3
            it[id_category] = 3
        }
        ProductCategoryTable.insert {
            it[id_product] = 4
            it[id_category] = 4
        }
    }
}

suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)

