package com.playground.chat.global.auth

import com.playground.chat.global.log.logger
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Date
import java.util.UUID
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
            .claim("userId", userId)
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
            Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)

            return true
        } catch (e: Exception) {
            log.info("[❌ Fail Jwt Validate] {}", e.message)
            return false
        }
    }

    fun parse(token: String, key: String): Any? {
        try {
            return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .payload[key]
        } catch (e: Exception) {
            log.info("[❌ Fail Jwt Parse] {}", e.message)
            return null
        }
    }
}
