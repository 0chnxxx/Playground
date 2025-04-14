package com.playground.chat.global.auth

import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class PrincipalArgumentResolver(
    private val jwtProvider: JwtProvider
): HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.getParameterAnnotation(Principal::class.java) != null
                && parameter.parameterType == UserPrincipal::class.java
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        val request = webRequest.nativeRequest as HttpServletRequest

        val token = request.getHeader("Authorization")
            .takeIf { it.startsWith("Bearer ") }
            ?.substring(7)

        if (token != null) {
            val userId = jwtProvider.parse(token, "userId").toString().toLong()

            return UserPrincipal(userId)
        } else {
            return null
        }
    }
}
