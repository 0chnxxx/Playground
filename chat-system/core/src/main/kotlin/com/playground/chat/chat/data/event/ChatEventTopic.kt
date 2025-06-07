package com.playground.chat.chat.data.event

enum class ChatEventTopic(
    val topic: String
) {
    CHAT_ROOM_EVENT("chat-room-event"),
    CHAT_MESSAGE_SEND("chat-message-send"),
    CHAT_MESSAGE_READ("chat-message-read");

    fun withChannel(channel: String): String {
        return "$topic:$channel"
    }
}
