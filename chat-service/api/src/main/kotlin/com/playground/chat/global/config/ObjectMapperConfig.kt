package com.playground.chat.global.config

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.EnumNamingStrategies.CamelCaseStrategy
import com.fasterxml.jackson.databind.PropertyNamingStrategies.LowerCamelCaseStrategy
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.kotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Configuration
class ObjectMapperConfig {
    @Bean
    fun objectMapper(): ObjectMapper {
        return ObjectMapper()
            .registerModule(kotlinModule())
            .registerModule(javaTimeModule())
            .setPropertyNamingStrategy(LowerCamelCaseStrategy())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
            .enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
            .enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    }

    private fun javaTimeModule(): JavaTimeModule {
        val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        return JavaTimeModule().apply {
            addSerializer(LocalDateTime::class.java, object : JsonSerializer<LocalDateTime>() {
                override fun serialize(value: LocalDateTime?, gen: JsonGenerator?, serializers: SerializerProvider?) {
                    gen?.writeString(value?.format(pattern))
                }
            })

            addDeserializer(LocalDateTime::class.java, object : JsonDeserializer<LocalDateTime>() {
                override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): LocalDateTime {
                    return LocalDateTime.parse(p?.text, pattern)
                }
            })
        }
    }
}
