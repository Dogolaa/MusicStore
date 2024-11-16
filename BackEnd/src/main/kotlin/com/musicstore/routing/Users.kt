package com.musicstore.routing

import com.musicstore.model.User
import com.musicstore.plugins.verifyPassword
import com.musicstore.repositories.user.UserRepository
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.JsonConvertException
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Routing.userRoutes(userRepository: UserRepository) {
    route("/api/users") {
        get {
            val users = userRepository.allUsers()

            if (users.isEmpty()) {
                call.respond(HttpStatusCode.NoContent, "No products found")
                return@get
            }

            call.respond(users)
        }

        post {
            try {
                val user = call.receive<User>()
                userRepository.addUser(user)
                call.respond(HttpStatusCode.Created)
            } catch (ex: IllegalStateException) {
                call.respond(HttpStatusCode.BadRequest)
            } catch (ex: JsonConvertException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        post("/login") {
            // Não criei outra classe apenas para isso, resolvi usar Map
            // Se houver necessidade podemos criar uma classe somente para login
            val login = call.receive<Map<String, String>>()
            val email = login["email"].toString()
            val password = login["password"].toString()

            val user = userRepository.findByEmail(email)

            if (password.verifyPassword(user!!.user_password)) {
                call.respondText("A senha está correta!")
            } else {
                call.respondText("A senha está incorreta!")
            }


        }
    }
}