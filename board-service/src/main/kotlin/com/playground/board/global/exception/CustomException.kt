package com.playground.board.global.exception

import kotlin.RuntimeException

class CustomException(
    val errorCode: ErrorCode
): RuntimeException() {
}
