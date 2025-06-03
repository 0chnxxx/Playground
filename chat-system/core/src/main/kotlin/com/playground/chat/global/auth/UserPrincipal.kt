package com.playground.chat.global.auth

import java.security.Principal
import java.util.UUID

class UserPrincipal(
    val id: UUID,
    val passport: String? = null
): Principal {
    override fun getName(): String {
        return id.toString()
    }

    fun getBearerPassport(): String {
        return "Bearer $passport"
    }
}
