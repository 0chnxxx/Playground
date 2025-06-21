package com.playground.chat.channel.global.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import com.playground.chat.global.security.CustomPrincipal
import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class CustomKafkaTemplate(
    private val mapper: ObjectMapper,
    private val kafkaTemplate: KafkaTemplate<String, String>
) {
    private val passportHeader = "passport"

    fun send(principal: CustomPrincipal?, channel: String, message: Any) {
        val (topic, key) = channel.split(":")
        val json = mapper.writeValueAsString(message)

        val record = ProducerRecord(
            topic,
            key,
            json
        )

        record.headers().add(passportHeader, principal?.passport?.toByteArray())

        kafkaTemplate.send(record)
    }
}
