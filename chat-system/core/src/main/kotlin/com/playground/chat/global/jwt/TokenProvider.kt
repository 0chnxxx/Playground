package com.playground.chat.global.jwt

import com.playground.chat.global.log.logger
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class TokenProvider(
    @Value("\${jwt.secret-key}")
    private val secretKey: String
) {
    private val log = logger()

    val signingKey: SecretKey = Keys.hmacShaKeyFor(secretKey.toByteArray())

    fun generate(type: TokenType, userId: UUID): String {
        val now = System.currentTimeMillis()

        val expiration = when (type) {
            TokenType.ACESS -> 360000
            TokenType.REFRESH -> 720000
            TokenType.PASSPORT -> Int.MAX_VALUE
        }

        return Jwts.builder()
            .claim(TokenKey.ID.key, userId)
            .issuedAt(Date(now))
            .expiration(Date(now + expiration))
            .signWith(signingKey)
            .compact()
    }

    fun validate(token: String?): Boolean {
        if (token == null) {
            return false
        }

        try {
            val pureToken = extractPureToken(token)

            Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(pureToken)

            return true
        } catch (e: JwtException) {
            log.info("[❌ Fail Jwt Validate] {}", e.printStackTrace())
            return false
        }
    }

    fun parse(token: String, key: TokenKey): Any? {
        try {
            val pureToken = extractPureToken(token)

            return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(pureToken)
                .payload[key.key]
        } catch (e: JwtException) {
            log.info("[❌ Fail Jwt Parse] {}", e.printStackTrace())
            return null
        }
    }

    private fun extractPureToken(token: String): String {
        return if (token.startsWith("Bearer ")) {
            token.substring(7)
        } else {
            token
        }
    }
}
