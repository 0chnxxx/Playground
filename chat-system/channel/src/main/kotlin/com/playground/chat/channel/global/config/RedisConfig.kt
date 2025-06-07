package com.playground.chat.channel.global.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig(
    private val objectMapper: ObjectMapper
) {
    @Value("\${spring.data.redis.host}")
    lateinit var host: String

    @Value("\${spring.data.redis.port}")
    var port: Int = 6379

    /**
     * Redis 명령을 위한 RedisTemplate 설정
     */
    @Bean
    fun redisTemplate(
        redisConnectionFactory: RedisConnectionFactory,
        redisSerializer: GenericJackson2JsonRedisSerializer
    ): RedisTemplate<String, String> {
        val template = RedisTemplate<String, String>()

        template.connectionFactory = redisConnectionFactory
        template.keySerializer = StringRedisSerializer()
        template.valueSerializer = redisSerializer

        return template
    }

    /**
     * Redis 연결을 위한 RedisConnectionFactory 설정
     */
    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory {
        return LettuceConnectionFactory(host, port)
    }

    /**
     * Redis Pub/Sub 메시지 리스닝을 위한 RedisMessageListenerContainer 설정
     */
    @Bean
    fun redisMessageListenerContainer(
        redisConnectionFactory: RedisConnectionFactory
    ): RedisMessageListenerContainer {
        val container = RedisMessageListenerContainer()

        container.setConnectionFactory(redisConnectionFactory)

        return container
    }

    @Bean
    fun redisSerializer(): GenericJackson2JsonRedisSerializer {
        return GenericJackson2JsonRedisSerializer(objectMapper)
    }
}
