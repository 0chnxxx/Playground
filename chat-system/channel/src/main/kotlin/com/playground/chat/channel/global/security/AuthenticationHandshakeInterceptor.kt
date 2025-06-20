package com.playground.chat.channel.global.security

import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.HandshakeInterceptor

@Component
class AuthenticationHandshakeInterceptor: HandshakeInterceptor {
    private val tokenAttribute = "token"

    /**
     * [JWT 토큰 전처리 인터셉터]
     * WebSocket 연결 요청의 token 쿼리 파라미터를 꺼내어 attribute에 저장
     */
    override fun beforeHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        attributes: MutableMap<String, Any>
    ): Boolean {
        val servletRequest = (request as? ServletServerHttpRequest)?.servletRequest
        val token = servletRequest?.getParameter(tokenAttribute)

        if (!token.isNullOrEmpty()) {
            attributes[tokenAttribute] = token
        }

        return true
    }

    override fun afterHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        exception: Exception?
    ) {
        return
    }
}
