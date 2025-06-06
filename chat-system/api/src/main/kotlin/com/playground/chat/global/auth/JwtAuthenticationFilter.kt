package com.playground.chat.global.auth

import com.playground.chat.user.service.UserFinder
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.filter.OncePerRequestFilter
import java.security.Principal
import java.util.UUID

@Component
class JwtAuthenticationFilter(
    private val corsConfig: CorsConfiguration,
    private val tokenProvider: TokenProvider,
    private val userFinder: UserFinder
): OncePerRequestFilter() {
    private val excludedPaths = listOf("/users/login", "/users/register")

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (request.method == HttpMethod.OPTIONS.name()) {
            filterChain.doFilter(request, response)
            return
        }

        if (excludedPaths.contains(request.requestURI)) {
            filterChain.doFilter(request, response)
            return
        }

        val token = request.getHeader("Authorization")
            ?.takeIf { it.startsWith("Bearer ") }
            ?.substring(7)

        if (token != null && tokenProvider.validate(token)) {
            val userId = UUID.fromString(tokenProvider.parse(token, "userId").toString())
            val user = userFinder.findUser(userId)

            val principal = UserPrincipal(user.id!!)

            SecurityContext.setPrincipal(principal)

            val wrappedRequest = object : HttpServletRequestWrapper(request) {
                override fun getUserPrincipal(): Principal = principal
            }

            try {
                filterChain.doFilter(wrappedRequest, response)
            } finally {
                SecurityContext.clear()
            }
        } else {
            response.status = HttpStatus.UNAUTHORIZED.value()
            response.setHeader("Access-Control-Allow-Origin", corsConfig.allowedOrigins!!.joinToString(","))
            response.setHeader("Access-Control-Allow-Headers", corsConfig.allowedHeaders!!.joinToString(","))
            response.setHeader("Access-Control-Allow-Methods", corsConfig.allowedMethods!!.joinToString(","))
            response.setHeader("Access-Control-Allow-Credentials", corsConfig.allowCredentials!!.toString())
        }
    }
}
