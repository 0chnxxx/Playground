package com.playground.chat.global.auth

import java.security.Principal

class UserPrincipal(
    val id: Long
): Principal {
    override fun getName(): String {
        return id.toString()
    }
}
