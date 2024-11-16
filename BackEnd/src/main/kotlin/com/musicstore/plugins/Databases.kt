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

fun configureDatabases(config: ApplicationConfig) {
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
            Brands,
            Categories,
            BrandsCategories,
            Products,
            ProductsCategories,
            Users,
            Roles,
            UsersRoles,
        )

        intializeData()

    }
}

fun intializeData() {
    if (Users.selectAll().empty()) {
        Users.insert {
            it[user_email] = "lucas@gmail.com"
            it[user_name] = "Lucas"
            it[user_last_name] = "Souza"
            it[user_password] = "senha123".hashPassword()
            it[user_status] = 1
            it[user_ph_content] = "teste1"
        }
        Users.insert {
            it[user_email] = "caio@gmail.com"
            it[user_name] = "Caio"
            it[user_last_name] = "Souza"
            it[user_password] = "senha1234".hashPassword()
            it[user_status] = 1
            it[user_ph_content] = "teste2"
        }
        Users.insert {
            it[user_email] = "ana@gmail.com"
            it[user_name] = "Ana"
            it[user_last_name] = "Souza"
            it[user_password] = "senha12345".hashPassword()
            it[user_status] = 1
            it[user_ph_content] = "teste3"
        }
        Users.insert {
            it[user_email] = "julia@gmail.com"
            it[user_name] = "Julia"
            it[user_last_name] = "Souza"
            it[user_password] = "senha12345".hashPassword()
            it[user_status] = 1
            it[user_ph_content] = "teste3"
        }
    }

    if (Roles.selectAll().empty()){
        Roles.insert {
            it[role_name] = "Administrador"
            it[role_description] = "Administrador do sistema"
        }
        Roles.insert {
            it[role_name] = "Vendedor"
            it[role_description] = "Vendedor do sistema"
        }
        Roles.insert {
            it[role_name] = "Expedidor"
            it[role_description] = "Expedidor do sistema"
        }
        Roles.insert {
            it[role_name] = "Cliente"
            it[role_description] = "Cliente do sistema"
        }
    }

    if (UsersRoles.selectAll().empty()){
        UsersRoles.insert {
            it[id_user] = 1
            it[id_role] = 1
        }
        UsersRoles.insert {
            it[id_user] = 2
            it[id_role] = 2
        }
        UsersRoles.insert {
            it[id_user] = 3
            it[id_role] = 3
        }
        UsersRoles.insert {
            it[id_user] = 4
            it[id_role] = 4
        }
    }

    if (Brands.selectAll().empty()) {
        Brands.insert {
            it[brand_name] = "Universal Music"
            it[brand_ph_content] = "fotouniversal"
        }
        Brands.insert {
            it[brand_name] = "Sony Music"
            it[brand_ph_content] = "fotosony"
        }
        Brands.insert {
            it[brand_name] = "Warner Music"
            it[brand_ph_content] = "fotowarner"
        }
        Brands.insert {
            it[brand_name] = "EMI"
            it[brand_ph_content] = "fotoemi"
        }
    }

    if (Categories.selectAll().empty()){
        Categories.insert {
            it[category_name] = "CD"
            it[category_ph_content] = "cd"
        }
        Categories.insert {
            it[category_name] = "DVD"
            it[category_ph_content] = "dvd"
        }
        Categories.insert {
            it[category_name] = "Vinil"
            it[category_ph_content] = "vinil"
        }
        Categories.insert {
            it[category_name] = "Multimidia"
            it[category_ph_content] = "multimidia"
        }
    }

    if (BrandsCategories.selectAll().empty()){
        BrandsCategories.insert {
            it[id_brand] = 1
            it[id_category] = 1
        }
        BrandsCategories.insert {
            it[id_brand] = 2
            it[id_category] = 2
        }
        BrandsCategories.insert {
            it[id_brand] = 3
            it[id_category] = 3
        }
        BrandsCategories.insert {
            it[id_brand] = 4
            it[id_category] = 4
        }
    }

    if (Products.selectAll().empty()){
        Products.insert {
            it[id_brand] = 1
            it[product_name] = "Vinil - Dark Side of the Moon"
            it[product_main_photo] = "vinil_ariana.jpeg"
            it[product_short_desc] = "Clássico do Pink Floyd"
            it[product_long_desc] = "Vinil remasterizado do álbum 'Dark Side of the Moon' do Pink Floyd, um dos álbuns mais icônicos da história da música."
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

        Products.insert {
            it[id_brand] = 2
            it[product_name] = "DVD - The Beatles Anthology"
            it[product_main_photo] = "vinil_ariana.jpeg"
            it[product_short_desc] = "Documentário sobre os Beatles"
            it[product_long_desc] = "Box com 5 DVDs que conta a história dos Beatles, com entrevistas, músicas e imagens raras."
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

        Products.insert {
            it[id_brand] = 3
            it[product_name] = "Vinil - Thriller (Michael Jackson)"
            it[product_main_photo] = "vinil_ariana.jpeg"
            it[product_short_desc] = "O álbum mais vendido de todos os tempos"
            it[product_long_desc] = "Edição especial do vinil de 'Thriller', de Michael Jackson, com áudio remasterizado e encarte."
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

        Products.insert {
            it[id_brand] = 4
            it[product_name] = "DVD - Nirvana Live at Reading"
            it[product_main_photo] = "vinil_ariana.jpeg"
            it[product_short_desc] = "Show histórico do Nirvana"
            it[product_long_desc] = "Gravação ao vivo do lendário show do Nirvana no festival de Reading, em 1992, com qualidade digital."
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

    if (ProductsCategories.selectAll().empty()){
        ProductsCategories.insert {
            it[id_product] = 1
            it[id_category] = 1
        }
        ProductsCategories.insert {
            it[id_product] = 2
            it[id_category] = 2
        }
        ProductsCategories.insert {
            it[id_product] = 3
            it[id_category] = 3
        }
        ProductsCategories.insert {
            it[id_product] = 4
            it[id_category] = 4
        }
    }
}

suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)

