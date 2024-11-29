package com.musicstore.routes

import com.musicstore.mapping.UserTable
import com.musicstore.model.User
import com.musicstore.plugins.configureDatabases
import com.musicstore.plugins.verifyPassword
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.testing.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import kotlin.test.*

class BrandsTest {
    @Test
    fun `GET user returns 200`() = testApplication {
        environment { config = ApplicationConfig("application_test.yaml") }
        application {
            configureDatabases(environment.config)

            transaction {
                UserTable.deleteAll()

                UserTable.insert {
                    it[user_email] = "email@teste.com"
                    it[user_name] = "Usuario"
                    it[user_last_name] = "Teste"
                    it[user_password] = "123456"
                    it[user_status] = 1
                    it[user_ph_content] = "teste"
                }
            }
        }
        val response = client.get("/api/users")
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `GET user with id that does not exist returns 404`() = testApplication {
        environment { config = ApplicationConfig("application_test.yaml") }
        application {
            configureDatabases(environment.config)

            transaction {
                UserTable.deleteWhere { id eq 100 }
            }
        }
        val response = client.get("/api/users/100")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun `GET user with email that does not exist returns 404`() = testApplication {
        environment { config = ApplicationConfig("application_test.yaml") }

        val response = client.get("/api/users/email/emailinexistente")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun `GET user with email that exists returns 200`() = testApplication {
        environment { config = ApplicationConfig("application_test.yaml") }
        application {
            configureDatabases(environment.config)

            transaction {
                UserTable.deleteAll()

                UserTable.insert {
                    it[user_email] = "email@teste.com"
                    it[user_name] = "Usuario"
                    it[user_last_name] = "Teste"
                    it[user_password] = "123456"
                    it[user_status] = 1
                    it[user_ph_content] = "teste"
                }
            }
        }

        val response = client.get("/api/users/email/email@teste.com")
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `POST user returns 201`() = testApplication {
        environment { config = ApplicationConfig("application_test.yaml") }

        application {
            configureDatabases(environment.config)

            transaction {
                UserTable.deleteWhere { user_email eq "emailnovo@gmail.com" }
            }
        }

        val newUserJson = """
        {
            "user_email": "emailnovo@gmail.com",
            "user_name": "Lucas",
            "user_last_name": "Souza",
            "user_password": "teste",
            "user_status": 1,
            "user_ph_content": "teste"
        }
    """.trimIndent()

        val response = client.post("/api/users") {
            contentType(ContentType.Application.Json)
            setBody(newUserJson)
        }

        assertEquals(HttpStatusCode.Created, response.status)

    }

//    @Test
//    fun `Password is equal to password hash in database`() = testApplication {
//        environment { config = ApplicationConfig("application_test.yaml") }
//
//        var newUserId: String? = null
//
//        application {
//            configureDatabases(environment.config)
//
//            newUserId = transaction {
//                UserTable.deleteAll()
//
//                UserTable.insertAndGetId {
//                    it[user_email] = "email@teste.com"
//                    it[user_name] = "Usuario"
//                    it[user_last_name] = "Teste"
//                    it[user_password] = "senha_muito_segura@123"
//                    it[user_status] = 1
//                    it[user_ph_content] = "teste"
//                }.toString()
//            }
//
//        }
//
//        println("Fora")
//        println(newUserId)
//
//        val response = client.get("/api/users/$newUserId")
//        val userResponse = Json.decodeFromString<User>(response.bodyAsText())
//        println("AAAAAAAAAAAAAAAA")
//        println(userResponse)
//        val passwordHash = userResponse.user_password
//
//        assertEquals("senha_muito_segura@123".verifyPassword(passwordHash), true)
//    }


}
