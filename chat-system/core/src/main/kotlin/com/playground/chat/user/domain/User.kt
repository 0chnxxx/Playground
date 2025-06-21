package com.playground.chat.user.domain

import com.playground.chat.chat.domain.ChatRoom
import com.playground.chat.global.auth.PrincipalRole
import java.util.*

class User(
    var id: UUID? = null,
    var email: String,
    var password: String,
    var image: String? = null,
    var nickname: String,
    var role: PrincipalRole,
    var rooms: MutableList<ChatRoom> = mutableListOf()
)
