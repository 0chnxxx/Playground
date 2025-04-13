package com.playground.chat.config

import com.playground.chat.service.ChatSubscriber
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.PatternTopic
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig {
    @Value("\${spring.data.redis.host}")
    lateinit var host: String

    @Value("\${spring.data.redis.port}")
    var port: Int = 6379

    /**
     * Redis 연결을 위한 RedisConnectionFactory 설정
     */
    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory {
        return LettuceConnectionFactory(host, port)
    }

    /**
     * Redis 명령을 위한 RedisTemplate 설정
     */
    @Bean
    fun redisTemplate(
        redisConnectionFactory: RedisConnectionFactory
    ): RedisTemplate<String, Any> {
        val template = RedisTemplate<String, Any>()

        template.connectionFactory = redisConnectionFactory
        template.keySerializer = StringRedisSerializer()
        template.valueSerializer = Jackson2JsonRedisSerializer(Any::class.java)

        return template
    }

    /**
     * Redis Pub/Sub 메시지 리스닝을 위한 RedisMessageListenerContainer 설정
     */
    @Bean
    fun redisMessageListenerContainer(
        redisConnectionFactory: RedisConnectionFactory,
        chatSubscriber: ChatSubscriber
    ): RedisMessageListenerContainer {
        val container = RedisMessageListenerContainer()
        val listener = MessageListenerAdapter(chatSubscriber, "onMessage")
        val topic = PatternTopic("chat:*")

        container.connectionFactory = redisConnectionFactory
        container.addMessageListener(listener, topic)

        return container
    }
}
