package com.playground.chat.socket.config

import com.playground.chat.global.auth.JwtProvider
import com.playground.chat.global.auth.UserPrincipal
import org.springframework.http.server.ServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.support.DefaultHandshakeHandler
import java.security.Principal

@Component
class JwtHandshakeHandler(
    private val jwtProvider: JwtProvider
): DefaultHandshakeHandler() {
    /**
     * [JWT 토큰 후처리 핸들러]
     * 인터셉터에 의해 attribute에 저장된 토큰을 꺼내어 검증 후 Principal 생성
     */
    override fun determineUser(
        request: ServerHttpRequest,
        wsHandler: WebSocketHandler,
        attributes: MutableMap<String, Any>
    ): Principal? {
        val token = attributes["token"] as String?

        return if (token != null && jwtProvider.validate(token)) {
            val userId = jwtProvider.parse(token, "userId").toString().toLong()

            UserPrincipal(userId)
        } else {
            null
        }
    }
}
