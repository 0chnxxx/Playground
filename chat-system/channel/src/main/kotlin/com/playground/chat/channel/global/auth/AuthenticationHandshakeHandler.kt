package com.playground.chat.channel.global.auth

import com.playground.chat.global.auth.CustomPrincipal
import com.playground.chat.global.auth.PrincipalRole
import com.playground.chat.global.token.TokenClaim
import com.playground.chat.global.token.TokenProvider
import com.playground.chat.global.token.TokenType
import org.springframework.http.server.ServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.support.DefaultHandshakeHandler
import java.security.Principal
import java.util.*

@Component
class AuthenticationHandshakeHandler(
    private val tokenProvider: TokenProvider
): DefaultHandshakeHandler() {
    private val tokenAttribute = "token"

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
        val token = attributes[tokenAttribute] as String?

        if (token != null && tokenProvider.validate(token)) {
            val userId = UUID.fromString(tokenProvider.parse(token, TokenClaim.ID).toString())
            val passport = tokenProvider.generate(TokenType.PASSPORT, userId)

            return CustomPrincipal(
                id = userId,
                role = PrincipalRole.USER,
                passport = passport
            )
        } else {
            throw Exception("Unauthorized")
        }
    }
}
