package com.playground.chat.channel.global.config

import com.playground.chat.channel.global.auth.AuthenticationHandshakeHandler
import com.playground.chat.channel.global.auth.AuthenticationHandshakeInterceptor
import com.playground.chat.channel.global.log.AccessLogInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig(
    private val accessLogInterceptor: AccessLogInterceptor,
    private val authenticationHandshakeHandler: AuthenticationHandshakeHandler,
    private val authenticationHandshakeInterceptor: AuthenticationHandshakeInterceptor
): WebSocketMessageBrokerConfigurer {
    /**
     * 웹소켓 설정
     */
    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry
            .addEndpoint("/ws")
            .setAllowedOriginPatterns("*")
            .addInterceptors(authenticationHandshakeInterceptor)
            .setHandshakeHandler(authenticationHandshakeHandler)
            .withSockJS()
    }

    /**
     * 웹소켓 메시지 브로커 설정
     */
    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.setApplicationDestinationPrefixes("/pub") // 클라이언트 -> 서버
        registry.enableSimpleBroker("/sub") // 서버 -> 클라이언트
    }

    /**
     * 웹소켓 인바운드 설정
     */
    override fun configureClientInboundChannel(registration: ChannelRegistration) {
        registration.interceptors(accessLogInterceptor)
    }
}
