package com.playground.chat.global.auth

import com.playground.chat.global.util.logger
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Date
import javax.crypto.SecretKey

@Component
class TokenProvider {
    private val log = logger()

    @Value("\${jwt.secret-key}")
    private lateinit var secretKey: String
    private lateinit var signingKey: SecretKey

    @PostConstruct
    fun init() {
        signingKey = Keys.hmacShaKeyFor(secretKey.toByteArray())
    }

    fun generate(type: TokenType, userId: Long): String {
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
            log.info("❌ Fail Jwt Validate : {}", e.printStackTrace())
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
            log.info("❌ Fail Jwt Parse : {}", e.printStackTrace())
            return null
        }
    }
}
