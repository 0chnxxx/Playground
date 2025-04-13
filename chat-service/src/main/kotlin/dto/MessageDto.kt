package com.playground.chat.dto

data class MessageDto(
    val roomId: String,
    val sender: Sender,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
) {
    data class Sender(
        val userId: String,
        val name: String
    )
}
