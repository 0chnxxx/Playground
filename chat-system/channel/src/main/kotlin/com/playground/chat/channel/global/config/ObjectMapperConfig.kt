package com.playground.chat.channel.global.config

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.PropertyNamingStrategies.LowerCamelCaseStrategy
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.kotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.TimeZone

@Configuration
class ObjectMapperConfig {
    @Bean
    fun objectMapper(): ObjectMapper {
        return ObjectMapper()
            .registerModule(kotlinModule())
            .registerModule(JavaTimeModule())
            .setPropertyNamingStrategy(LowerCamelCaseStrategy())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
            .enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING)
            .enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .setTimeZone(TimeZone.getTimeZone("UTC"))
    }
}
