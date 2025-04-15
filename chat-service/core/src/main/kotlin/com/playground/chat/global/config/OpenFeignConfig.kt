package com.playground.chat.global.config

import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Configuration

@Configuration
@EnableFeignClients(
    basePackages = [
        "com.playground.chat.chat.client"
    ]
)
class OpenFeignConfig
