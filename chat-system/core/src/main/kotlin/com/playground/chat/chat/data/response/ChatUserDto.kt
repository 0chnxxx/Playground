package com.playground.chat.chat.data.response

data class ChatUserDto(
    val id: Long,
    val nickname: String,
    val isOwner: Boolean
)
