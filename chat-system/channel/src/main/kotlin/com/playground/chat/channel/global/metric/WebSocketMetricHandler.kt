package com.playground.chat.channel.global.metric

import io.micrometer.core.instrument.MeterRegistry
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.SessionConnectedEvent
import org.springframework.web.socket.messaging.SessionDisconnectEvent
import java.util.concurrent.atomic.AtomicInteger

@Component
class WebSocketMetricHandler(
    private val meterRegistry: MeterRegistry
) {
    private val connectionCounter = meterRegistry.gauge("websocket.active.connections", AtomicInteger(0))!!

    @EventListener
    fun handleSessionConnected(event: SessionConnectedEvent) {
        connectionCounter.incrementAndGet()
    }

    @EventListener
    fun handleSessionDisconnect(event: SessionDisconnectEvent) {
        connectionCounter.decrementAndGet()
    }
}
