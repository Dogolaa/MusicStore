package com.musicstore.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.musicstore.model.request.UserLogin
import com.musicstore.repositories.role.RoleRepository
import com.musicstore.repositories.user.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Application.configureSecurity(userRepository: UserRepository, roleRepository: RoleRepository) {
    val jwtAudience = environment.config.property("jwt.audience").getString()
    val jwtIssuer = environment.config.property("jwt.issuer").getString()
    val jwtRealm = environment.config.property("jwt.realm").getString()
    val jwtSecret = environment.config.property("jwt.secret").getString()
    authentication {
        jwt {
            realm = jwtRealm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(jwtSecret))
                    .withAudience(jwtAudience)
                    .withIssuer(jwtIssuer)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(jwtAudience)) JWTPrincipal(credential.payload) else null
            }
            challenge { defaultScheme, realm ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }

    routing {
        post("/api/login") {
            val (email, password) = call.receive<UserLogin>()
            val user = userRepository.findByEmail(email)
            val role = roleRepository.roleByUserId(user?.id!!)

            if (password.verifyPassword(user.user_password)) {
                val token = JWT.create()
                    .withAudience(jwtAudience)
                    .withIssuer(jwtIssuer)
                    .withClaim("user_name", user.user_name)
                    .withClaim("user_last_name", user.user_last_name)
                    .withClaim("user_status", user.user_status)
                    .withClaim("user_email", user.user_email)
                    .withClaim("user_role", role)
                    .withExpiresAt(Date(System.currentTimeMillis() + 30 * 60 * 1000)) // 30 minutes
                    .sign(Algorithm.HMAC256(jwtSecret))
                call.respond(hashMapOf("token" to token))

            } else {
                call.respondText("A senha está incorreta!", status = HttpStatusCode.BadRequest)
            }
        }

        authenticate {
            get("/hello") {
                val principal = call.principal<JWTPrincipal>()
                val username = principal!!.payload.getClaim("username").asString()
                val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
                call.respondText("Hello, $username! Token is expired at $expiresAt ms.")
            }
        }
    }
}

//fun Route.requireRole(requiredRole: String) {
//    intercept(ApplicationCallPipeline.ApplicationPhase.Plugins) {
//        val principal = call.principal<JWTPrincipal>()
//        val userRole = principal!!.payload.getClaim("user_role").asString()
//
//        println(userRole)
//        println(requiredRole)
//        if (!requiredRole.equals(userRole, ignoreCase = true)) {
//            call.respond(HttpStatusCode.Forbidden, "Access Denied")
//            finish()
//        }
//    }
//}
