package com.playground.chat.global.log

import com.playground.chat.global.util.logger
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class AccessLogFilter: OncePerRequestFilter() {
    private val log = logger()

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val method = request.method
        val uri = request.requestURI
        val query = request.queryString?.let { "?$it" } ?: ""
        val startTime = System.currentTimeMillis()

        try {
            filterChain.doFilter(request, response)
        } finally {
            val endTime = System.currentTimeMillis()
            val duration = endTime - startTime
            val status = response.status

            log.info(
                "[🌐 HTTP Call] {} {}{} -> {} ({}ms)",
                method,
                uri,
                query,
                status,
                duration
            )
        }
    }
}
