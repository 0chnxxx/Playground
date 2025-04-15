package com.playground.chat.socket.service

import com.playground.chat.global.util.logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.connection.MessageListener
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

@Component
class SocketSubscriber(
    private val listener: SocketListener,
    private val container: RedisMessageListenerContainer
) {
    private val log = logger()

    @Value("\${websocket.channel.prefix}")
    private lateinit var channel: String

    /**
     * session 별 room 관리를 위한 Map
     */
    private val sessions = ConcurrentHashMap<String, MutableSet<String>>()

    /**
     * room 에 대한 session 카운트를 위한 Map
     */
    private val subscribers = ConcurrentHashMap<String, AtomicInteger>()

    /**
     * room 별 listener 관리를 위한 Map
     */
    private val listeners = ConcurrentHashMap<String, MessageListener>()

    /**
     * [동적 구독 함수]
     * session 별 room 들에 대한 listener 를 관리
     * room 별로 listener 를 1:1로 생성하여 중복되는 listener 의 경우 count 증가
     * count 가 0인 경우 listener 생성
     */
    fun subscribe(sessionId: String, roomIds: List<String>) {
        val subscribedRooms = sessions.computeIfAbsent(sessionId) { mutableSetOf() }

        for (roomId in roomIds) {
            if (!subscribedRooms.add(roomId)) continue

            subscribers.compute(roomId) { _, count ->
                if (count == null) {
                    listeners[roomId] = listener
                    container.addMessageListener(listener, ChannelTopic("${channel}:${roomId}"))

                    log.info("[✅ Chat Channel Subscribed] channel : {}", "${channel}:${roomId}")

                    AtomicInteger(1)
                } else {
                    count.incrementAndGet()
                    count
                }
            }
        }
    }

    /**
     * [동적 구독 취소 함수]
     * session 별 room 들에 대한 listener 를 관리
     * session 종료 시 구독 했던 room 들을 가져와 0명이 될 때까지 count 감소
     * count 가 0 인 경우 listener 삭제
     */
    fun unsubscribe(sessionId: String) {
        val subscribedRooms = sessions.remove(sessionId) ?: return

        for (roomId in subscribedRooms) {
            subscribers.computeIfPresent(roomId) { _, count ->
                val remaining = count.decrementAndGet()

                if (remaining <= 0) {
                    listeners.remove(roomId)?.let { listener ->
                        container.removeMessageListener(listener, ChannelTopic("${channel}:${roomId}"))
                    }

                    log.info("[❌ Chat Channel Unsubscribed] channel : {}", "${channel}:${roomId}")

                    null
                } else {
                    count
                }
            }
        }
    }
}
