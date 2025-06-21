package com.playground.chat.chat.data.exception

import com.playground.chat.global.data.ErrorMessage

enum class ChatErrorMessage(
    override val statusCode: Int,
    override val errorCode: String,
    override val message: String
): ErrorMessage {
    NOT_FOUND_CHAT(404, "NOT_FOUND_CHAT", "채팅이 존재하지 않습니다."),
    NOT_FOUND_ROOM(404, "NOT_FOUND_ROOM", "채팅방이 존재하지 않습니다."),
    NOT_FOUND_MESSAGE(404, "NOT_FOUND_MESSAGE", "채팅 메세지가 존재하지 않습니다."),
    FORBIDDEN_ROOM_OWNER(403, "FORBIDDEN_ROOM_OWNER", "방장 권한이 없습니다.")
}
