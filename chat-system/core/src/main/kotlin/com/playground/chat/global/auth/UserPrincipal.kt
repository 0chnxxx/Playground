package com.playground.chat.global.auth

import java.security.Principal

class UserPrincipal(
    val id: Long,
    val passport: String? = null
): Principal {
    override fun getName(): String {
        return id.toString()
    }

    fun getBearerPassport(): String {
        return "Bearer $passport"
    }
}
