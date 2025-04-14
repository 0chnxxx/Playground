package com.playground.chat.socket.config

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig(
    private val jwtHandshakeHandler: JwtHandshakeHandler,
    private val jwtHandshakeInterceptor: JwtHandshakeInterceptor
): WebSocketMessageBrokerConfigurer {
    /**
     * 웹소켓 설정
     */
    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry
            .addEndpoint("/ws")
            .setAllowedOriginPatterns("*")
            .setHandshakeHandler(jwtHandshakeHandler)
            .addInterceptors(jwtHandshakeInterceptor)
            .withSockJS()
    }

    /**
     * 웹소켓 메시지 브로커 설정
     */
    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.enableSimpleBroker("/chat", "/user") // 서버 -> 클라이언트
        registry.setApplicationDestinationPrefixes("/") // 클라이언트 -> 서버
    }
}
