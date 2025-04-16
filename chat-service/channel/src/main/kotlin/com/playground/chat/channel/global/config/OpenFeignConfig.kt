package com.playground.chat.channel.global.config

import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Configuration

@Configuration
@EnableFeignClients(
    basePackages = [
        "com.playground.chat.channel.client"
    ]
)
class OpenFeignConfig
