package com.playground.chat.chat.data.response

data class ChatMessageDto(
    val roomId: String,
    val sender: Sender,
    val content: String,
    val isMine: Boolean,
    val timestamp: Long = System.currentTimeMillis()
) {
    data class Sender(
        val userId: String,
        val name: String
    )
}
