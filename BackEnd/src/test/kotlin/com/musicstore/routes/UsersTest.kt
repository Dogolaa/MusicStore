package com.musicstore.routes

import com.musicstore.mapping.UserTable
import com.musicstore.plugins.configureDatabases
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class UsersTest {
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
    fun `GET user returns 200`() = testApplication {
        environment { config = ApplicationConfig("application_test.yaml") }
        application {
            configureDatabases(environment.config)

            transaction {
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


}
