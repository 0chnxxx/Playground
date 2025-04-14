package com.playground.chat.auth.util

import java.security.MessageDigest
import java.security.SecureRandom

object PasswordUtil {
    fun encode(password: String): String {
        val salt = generateSalt()
        val encodedPassword = hashPassword(password, salt)
        return "$salt:$encodedPassword"
    }

    fun match(plainPassword: String, encodedPassword: String): Boolean {
        val parts = encodedPassword.split(":")
        val salt = parts[0]
        val originalPassword = parts[1]
        val hashedPassword = hashPassword(plainPassword, salt)

        return originalPassword == hashedPassword
    }

    private fun hashPassword(password: String, salt: String): String {
        val messageDigest = MessageDigest.getInstance("SHA-512")

        messageDigest.update(salt.toByteArray())

        val digest = messageDigest.digest(password.toByteArray())

        return bytesToHex(digest)
    }

    private fun generateSalt(): String {
        val salt = ByteArray(16)
        val random = SecureRandom()

        random.nextBytes(salt)

        return bytesToHex(salt)
    }

    private fun bytesToHex(bytes: ByteArray): String {
        val hexString = StringBuilder()

        for (byte in bytes) {
            hexString.append(String.format("%02x", byte))
        }

        return hexString.toString()
    }
}
