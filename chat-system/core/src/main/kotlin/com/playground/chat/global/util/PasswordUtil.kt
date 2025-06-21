package com.playground.chat.global.util

import java.security.MessageDigest

object PasswordUtil {
    fun encode(password: String): String {
        val messageDigest = MessageDigest.getInstance("SHA-512")
        val digest = messageDigest.digest(password.toByteArray())

        val hexString = StringBuilder()

        for (byte in digest) {
            hexString.append(String.format("%02x", byte))
        }

        return hexString.toString()
    }

    fun match(plainPassword: String, encodedPassword: String): Boolean {
        return encode(plainPassword) == encodedPassword
    }
}
