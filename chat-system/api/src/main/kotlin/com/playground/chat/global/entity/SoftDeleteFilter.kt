package com.playground.chat.global.entity

import com.playground.chat.global.auth.PrincipalContext
import com.playground.chat.global.auth.PrincipalRole
import jakarta.persistence.EntityManager
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.hibernate.Session
import org.springframework.stereotype.Component

@Aspect
@Component
class SoftDeleteFilter(
    private val entityManager: EntityManager
) {
    @Pointcut("execution(* com.playground.chat..*Service.*(..))")
    fun methods() {}

    @Around("methods()")
    fun applyFilter(joinPoint: ProceedingJoinPoint): Any? {
        val principal = PrincipalContext.getPrincipal()
        val session = entityManager.unwrap(Session::class.java)

        if (principal != null) {
            when (principal.role) {
                PrincipalRole.USER -> {
                    session
                        .enableFilter("softDeleteFilter")
                        .setParameter("isDeleted", false)
                }

                PrincipalRole.ADMIN -> {
                    session
                        .disableFilter("softDeleteFilter")
                }
            }
        }

        return joinPoint.proceed()
    }
}
