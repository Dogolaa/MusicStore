package com.musicstore.routes

import com.musicstore.mapping.UserTable
import com.musicstore.model.User
import com.musicstore.plugins.configureDatabases
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.testing.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import kotlin.test.*

class BrandsTest{
    @Test
    fun `GET user returns 200`() = testApplication {
        environment {
            config = ApplicationConfig("application_test.yaml")
        }

        val response = client.get("/api/users")
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `GET user with id that does not exist returns 404`() = testApplication {
        environment {
            config = ApplicationConfig("application_test.yaml")
        }

        val response = client.get("/api/users/100")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun `GET user with email that does not exist returns 404`() = testApplication {
        environment {
            config = ApplicationConfig("application_test.yaml")
        }

        val response = client.get("/api/users/email/emailinexistente")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun `GET user with email that exists returns 200`() = testApplication {
        environment {
            config = ApplicationConfig("application_test.yaml")
        }

        val response = client.get("/api/users/email/lucas@gmail.com")
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `POST user returns 201`() = testApplication {
        environment {
            config = ApplicationConfig("application_test.yaml")
        }

        application {
            configureDatabases(environment.config)

            transaction {
                UserTable.deleteWhere { UserTable.user_email eq "emailnaoexistrente@gmail.com" }
            }
        }

        val newUserJson = """
        {
            "user_email": "emailnaoexistrente@gmail.com",
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

        assertEquals(HttpStatusCode.Created, response.status)

    }


}
