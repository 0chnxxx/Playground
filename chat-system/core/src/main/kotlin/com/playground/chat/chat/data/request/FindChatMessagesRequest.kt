package com.playground.chat.chat.data.request

data class FindChatMessagesRequest(
    val page: Int = 1,
    val size: Int = 10
)
