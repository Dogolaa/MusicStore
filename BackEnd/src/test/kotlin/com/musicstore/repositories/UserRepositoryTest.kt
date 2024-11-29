package com.musicstore.repositories

import com.musicstore.mapping.UserTable
import com.musicstore.model.User
import com.musicstore.plugins.configureDatabases
import com.musicstore.plugins.hashPassword
import com.musicstore.plugins.verifyPassword
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class UserRepositoryTest {
    @BeforeTest
    fun setup() = testApplication {
        environment { config = ApplicationConfig("application_test.yaml") }
        application {
            configureDatabases(environment.config)

            transaction {
                UserTable.deleteAll()
            }
        }
    }

    @Test
    fun `Password is equal to password hash in database`() = testApplication {
        environment { config = ApplicationConfig("application_test.yaml") }
        application {
            configureDatabases(environment.config)

            transaction {
                UserTable.insert {
                    it[user_email] = "userforpassword@teste.com"
                    it[user_name] = "Usuario"
                    it[user_last_name] = "Teste"
                    it[user_password] = "senha_muito_dificil@123".hashPassword()
                    it[user_status] = 1
                    it[user_ph_content] = "teste"
                }
            }
        }

        val response = client.get("/api/users/email/userforpassword@teste.com")

        val userResponse = Json.decodeFromString<User>(response.bodyAsText())
        val passwordHash = userResponse.user_password

        assertEquals("senha_muito_dificil@123".verifyPassword(passwordHash), true)
    }
}