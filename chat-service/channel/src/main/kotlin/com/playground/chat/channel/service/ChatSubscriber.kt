package com.playground.chat.channel.service

import com.playground.chat.global.log.logger
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

@Component
class ChatSubscriber(
    private val container: RedisMessageListenerContainer,
    private val chatMessageListener: ChatMessageListener,
    private val chatEventListener: ChatEventListener
) {
    private val log = logger()

    @Value("\${spring.data.redis.channel.chat-message.topic}")
    private lateinit var messageChannel: String

    @Value("\${spring.data.redis.channel.chat-event.topic}")
    private lateinit var eventChannel: String

    /**
     * UserId to Set(RoomId)
     */
    private val sessions = ConcurrentHashMap<String, MutableSet<String>>()

    /**
     * RoomId to Count(UserId)
     */
    private val subscribers = ConcurrentHashMap<String, AtomicInteger>()

    /**
     * RoomId to Topic
     */
    private val topics = ConcurrentHashMap<String, ChannelTopic>()

    @PostConstruct
    fun init() {
        container.addMessageListener(chatEventListener, ChannelTopic(eventChannel))
    }

    /**
     * [동적 구독 함수]
     * session별 room들에 대한 listener 를 관리
     * room별로 listener를 1:1로 생성하여 중복되는 listener의 경우 count 증가
     * count가 0인 경우 listener 생성
     */
    fun subscribeToRooms(userId: String, roomIds: List<String>) {
        val subscribedRooms = sessions.computeIfAbsent(userId) { ConcurrentHashMap.newKeySet() }

        for (roomId in roomIds) {
            if (!subscribedRooms.add(roomId)) continue

            subscribe(roomId, userId)
        }
    }

    fun subscribeToRoom(userId: String, roomId: String) {
        val subscribedRooms = sessions[userId] ?: return

        if (subscribedRooms.add(roomId)) {
            subscribe(roomId, userId)
        }
    }

    private fun subscribe(roomId: String, userId: String) {
        subscribers.compute(roomId) { _, count ->
            if (count == null) {
                topics
                    .computeIfAbsent(roomId) {
                        ChannelTopic("${messageChannel}:${roomId}")
                    }
                    .let { topic ->
                        container.addMessageListener(chatMessageListener, topic)
                    }

                log.info("[✅ Chat Channel Subscribed] userId: {}, roomId : {}", userId, roomId)

                AtomicInteger(1)
            } else {
                count.incrementAndGet()
                count
            }
        }
    }

    /**
     * [동적 구독 취소 함수]
     * session 별 room 들에 대한 listener 를 관리
     * session 종료 시 구독 했던 room 들을 가져와 0명이 될 때까지 count 감소
     * count 가 0 인 경우 listener 삭제
     */
    fun unsubscribeToUser(userId: String) {
        val subscribedRooms = sessions.remove(userId) ?: return

        for (roomId in subscribedRooms) {
            unsubscribe(roomId, userId)
        }
    }

    fun unsubscribeToRoom(roomId: String) {
        sessions.forEach { (userId, rooms) ->
            if (rooms.remove(roomId)) {
                unsubscribe(roomId, userId)
            }
        }
    }

    fun unsubscribeToUserRoom(userId: String, roomId: String) {
        val subscribedRooms = sessions[userId] ?: return

        if (subscribedRooms.remove(roomId)) {
            unsubscribe(roomId, userId)
        }
    }

    private fun unsubscribe(roomId: String, userId: String) {
        subscribers.computeIfPresent(roomId) { _, count ->
            val remaining = count.decrementAndGet()

            if (remaining <= 0) {
                topics[roomId]?.let { topic ->
                    container.removeMessageListener(chatMessageListener, topic)
                    topics.remove(roomId)
                }

                log.info("[❌ Chat Channel Unsubscribed] userId : {}, roomId : {}", userId, roomId)

                null
            } else {
                count
            }
        }
    }
}
