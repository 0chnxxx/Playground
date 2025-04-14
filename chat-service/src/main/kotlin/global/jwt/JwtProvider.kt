package com.playground.chat.global.auth

import com.playground.chat.global.util.logger
import com.playground.chat.global.data.TokenDto
import com.playground.chat.user.entity.UserEntity
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtProvider {
    private val log = logger()

    @Value("\${jwt.secret-key}")
    private lateinit var secretKey: String
    private lateinit var signingKey: SecretKey

    @PostConstruct
    fun init() {
        signingKey = Keys.hmacShaKeyFor(secretKey.toByteArray())
    }

    fun generate(userEntity: UserEntity): TokenDto {
        val now = System.currentTimeMillis()

        val accessToken = Jwts.builder()
            .claim("userId", userEntity.id)
            .issuedAt(Date(now))
            .expiration(Date(now + 360000))
            .signWith(signingKey)
            .compact()

        val refreshToken = Jwts.builder()
            .claim("userId", userEntity.id)
            .issuedAt(Date(now))
            .expiration(Date(now + 720000))
            .signWith(signingKey)
            .compact()

        return TokenDto(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
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
