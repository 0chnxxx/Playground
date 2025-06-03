package com.playground.chat.channel.service

import com.playground.chat.global.log.logger
import jakarta.annotation.PostConstruct
import org.springframework.data.redis.connection.MessageListener
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

@Component
class ChannelSubscriber(
    private val container: RedisMessageListenerContainer,
    private val channelSendListener: ChannelSendListener,
    private val channelReadListener: ChannelReadListener,
    private val channelEventListener: ChannelEventListener
) {
    private val log = logger()

    /**
     * UserId to Set(RoomId)
     */
    private val sessions = ConcurrentHashMap<Long, MutableSet<Long>>()

    /**
     * RoomId to UserId.count()
     */
    private val subscribers = ConcurrentHashMap<Long, AtomicInteger>()

    /**
     * RoomId to Listener
     */
    private val listeners = ConcurrentHashMap<Long, List<Pair<MessageListener, ChannelTopic>>>()

    @PostConstruct
    fun init() {
        container.addMessageListener(channelEventListener, ChannelTopic("chat-room-event"))
    }

    /**
     * [동적 구독 함수]
     * session별 room들에 대한 listener 를 관리
     * room별로 listener를 1:1로 생성하여 중복되는 listener의 경우 count 증가
     * count가 0인 경우 listener 생성
     */
    fun subscribeToRooms(userId: Long, roomIds: List<Long>) {
        val subscribedRooms = sessions.computeIfAbsent(userId) { ConcurrentHashMap.newKeySet() }

        for (roomId in roomIds) {
            if (!subscribedRooms.add(roomId)) continue

            subscribe(roomId, userId)
        }
    }

    fun subscribeToRoom(userId: Long, roomId: Long) {
        val subscribedRooms = sessions[userId] ?: return

        if (subscribedRooms.add(roomId)) {
            subscribe(roomId, userId)
        }
    }

    private fun subscribe(roomId: Long, userId: Long) {
        subscribers.compute(roomId) { _, count ->
            if (count == null) {
                listeners
                    .computeIfAbsent(roomId) {
                        listOf(
                            channelSendListener to ChannelTopic("chat-message-send:${roomId}"),
                            channelReadListener to ChannelTopic("chat-message-read:${roomId}")
                        )
                    }
                    .let { topics ->
                        topics.forEach { topic ->
                            container.addMessageListener(topic.first, topic.second)
                        }
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
    fun unsubscribeToUser(userId: Long) {
        val subscribedRooms = sessions.remove(userId) ?: return

        for (roomId in subscribedRooms) {
            unsubscribe(roomId, userId)
        }
    }

    fun unsubscribeToRoom(roomId: Long) {
        sessions.forEach { (userId, rooms) ->
            if (rooms.remove(roomId)) {
                unsubscribe(roomId, userId)
            }
        }
    }

    fun unsubscribeToUserRoom(userId: Long, roomId: Long) {
        val subscribedRooms = sessions[userId] ?: return

        if (subscribedRooms.remove(roomId)) {
            unsubscribe(roomId, userId)
        }
    }

    private fun unsubscribe(roomId: Long, userId: Long) {
        subscribers.computeIfPresent(roomId) { _, count ->
            val remaining = count.decrementAndGet()

            if (remaining <= 0) {
                listeners[roomId]?.let { topics ->
                    topics.forEach { topic ->
                        container.removeMessageListener(topic.first, topic.second)
                    }

                    listeners.remove(roomId)
                }

                log.info("[❌ Chat Channel Unsubscribed] userId : {}, roomId : {}", userId, roomId)

                null
            } else {
                count
            }
        }
    }
}
