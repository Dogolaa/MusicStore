package com.example.plugins

import at.favre.lib.crypto.bcrypt.BCrypt

fun String.hashPassword(): String {
    return BCrypt.withDefaults().hashToString(12, this.toCharArray())
}

fun String.verifyPassword(hashedPassword: String): Boolean {
    return BCrypt.verifyer().verify(this.toCharArray(), hashedPassword).verified
}