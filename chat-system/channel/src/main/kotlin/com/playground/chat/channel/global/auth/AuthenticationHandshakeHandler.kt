package com.playground.chat.channel.global.auth

import com.playground.chat.global.auth.UserPrincipal
import com.playground.chat.global.auth.TokenProvider
import com.playground.chat.global.auth.TokenType
import org.springframework.http.server.ServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.support.DefaultHandshakeHandler
import java.security.Principal

@Component
class AuthenticationHandshakeHandler(
    private val tokenProvider: TokenProvider
): DefaultHandshakeHandler() {
    /**
     * [JWT 토큰 후처리 핸들러]
     * - 인터셉터에 의해 attribute에 저장된 토큰을 꺼내어 검증
     * - 서버 간 통신에 사용할 passport(JWT)를 가지는 인증 객체 UserPrincipal 생성
     */
    override fun determineUser(
        request: ServerHttpRequest,
        wsHandler: WebSocketHandler,
        attributes: MutableMap<String, Any>
    ): Principal? {
        val token = attributes["token"] as String?

        if (token != null && tokenProvider.validate(token)) {
            val userId = tokenProvider.parse(token, "userId").toString().toLong()
            val passport = tokenProvider.generate(TokenType.PASSPORT, userId)

            return UserPrincipal(
                id = userId,
                passport = passport
            )
        } else {
            throw Exception("Unauthorized")
        }
    }
}
