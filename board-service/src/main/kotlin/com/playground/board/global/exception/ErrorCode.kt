package com.playground.board.global.exception

enum class ErrorCode(
    val status: Int,
    val code: String,
    val message: String
) {
    NOT_FOUND_ENDPOINT(404, "NOT_FOUND_ENDPOINT", "존재하지 않는 엔드포인트입니다."),
    NOT_FOUND_RESOURCE(404, "NOT_FOUND_RESOURCE", "존재하지 않는 리소스입니다."),
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR", "서버에 오류가 발생하였습니다.")
}
