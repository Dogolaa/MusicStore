package com.musicstore.routes

import com.musicstore.exceptions.NotFoundException
import com.musicstore.model.User
import com.musicstore.repositories.user.UserRepository
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRoute(userRepository: UserRepository) {
    route("/api/users") {
        get {
            val users = userRepository.allUsers()

            call.respond(
                if (users.isEmpty()) HttpStatusCode.NoContent
                else users
            )
        }

        get("/{id}") {
            val id = call.parameters["id"]

            val user = userRepository.userById(id!!.toInt()) ?: throw NotFoundException(
                "User not found",
                "User with ID $id not found"
            )

            call.respond(user)
        }

        get("/email/{email}") {
            val email = call.parameters["email"]

            val user = userRepository.findByEmail(email!!) ?: throw NotFoundException(
                "User not found",
                "User with email $email not found"
            )

            call.respond(user)
        }

        put("/{id]") {
            val id = call.parameters["id"]

//            val user = userRepository.userById(id!!.toInt()) ?: throw NotFoundException(
//                "User not found",
//                "User with ID $id not found"
//            )

            val user = call.receive<User>()
            userRepository.updateUserById(id!!.toInt())


        }

        post {
            val user = call.receive<User>()
            userRepository.addUser(user)
            call.respond(HttpStatusCode.Created)
        }

        delete("/{id}") {
            val id = call.parameters["id"]
            val removed = userRepository.removeUser(id!!.toInt())

            if (removed) call.respond(HttpStatusCode.OK)
            else throw NotFoundException("User not found", "User with ID $id not found")
        }


    }
}