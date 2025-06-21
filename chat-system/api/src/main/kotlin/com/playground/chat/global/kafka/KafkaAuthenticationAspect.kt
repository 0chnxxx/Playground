package com.playground.chat.global.kafka

import com.playground.chat.global.auth.CustomPrincipal
import com.playground.chat.global.auth.PrincipalContext
import com.playground.chat.global.auth.PrincipalRole
import com.playground.chat.global.token.TokenClaim
import com.playground.chat.global.token.TokenProvider
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component
import org.springframework.transaction.support.TransactionSynchronization
import org.springframework.transaction.support.TransactionSynchronizationManager
import java.util.*

@Aspect
@Component
class KafkaAuthenticationAspect(
    private val tokenProvider: TokenProvider
) {
    private val passportHeader = "passport"

    @Pointcut("@annotation(org.springframework.kafka.annotation.KafkaListener)")
    fun kafkaConsumers() {}

    @Around("kafkaConsumers()")
    fun aroundKafkaListener(joinPoint: ProceedingJoinPoint): Any? {
        val record = joinPoint.args.find { it is ConsumerRecord<*, *> } as? ConsumerRecord<*, *>

        val passport = record
            ?.headers()
            ?.lastHeader(passportHeader)?.value()?.let { String(it) }

        val principal = passport
            ?.let { UUID.fromString(tokenProvider.parse(it, TokenClaim.ID).toString()) }
            ?.let { CustomPrincipal(it, PrincipalRole.USER, passport) }

        if (principal != null) {
            return PrincipalContext.operate(
                principal = principal,
                function = { joinPoint.proceed() },
                after = {
                    if (TransactionSynchronizationManager.isSynchronizationActive()) {
                        TransactionSynchronizationManager
                            .registerSynchronization(object : TransactionSynchronization {
                            override fun afterCompletion(status: Int) {
                                PrincipalContext.clear()
                            }
                        })
                    } else {
                        PrincipalContext.clear()
                    }
                }
            )
        }

        return joinPoint.proceed()
    }
}
