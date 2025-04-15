package com.playground.chat.global.data

data class Page<T>(
    val totalPages: Int = 0,
    val totalElements: Long = 0,
    val isFirst: Boolean = false,
    val isLast: Boolean = false,
    val data: T
)
