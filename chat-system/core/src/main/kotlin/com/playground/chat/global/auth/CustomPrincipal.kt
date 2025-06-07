package com.playground.chat.global.auth

import java.security.Principal
import java.util.*

class CustomPrincipal(
    val id: UUID,
    val role: PrincipalRole,
    val passport: String? = null
): Principal {
    override fun getName(): String {
        return id.toString()
    }

    fun getBearerPassport(): String {
        return "Bearer $passport"
    }
}
