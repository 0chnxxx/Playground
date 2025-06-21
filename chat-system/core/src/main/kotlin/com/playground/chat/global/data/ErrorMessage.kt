package com.playground.chat.global.data

interface ErrorMessage {
    val statusCode: Int
    val errorCode: String
    val message: String
}
