package com.playground.chat.channel.service

import com.playground.chat.chat.data.event.ChatEventTopic
import com.playground.chat.global.log.logger
import jakarta.annotation.PostConstruct
import org.springframework.data.redis.connection.MessageListener
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.stereotype.Component
import java.util.*
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
    private val sessions = ConcurrentHashMap<UUID, MutableSet<UUID>>()

    /**
     * RoomId to UserId.count()
     */
    private val subscribers = ConcurrentHashMap<UUID, AtomicInteger>()

    /**
     * RoomId to Listener
     */
    private val listeners = ConcurrentHashMap<UUID, List<Pair<MessageListener, ChannelTopic>>>()

    @PostConstruct
    fun init() {
        container.addMessageListener(channelEventListener, ChannelTopic(ChatEventTopic.CHAT_ROOM_EVENT.topic))
    }

    /**
     * [동적 구독 함수]
     * session별 room들에 대한 listener 를 관리
     * room별로 listener를 1:1로 생성하여 중복되는 listener의 경우 count 증가
     * count가 0인 경우 listener 생성
     */
    fun subscribeToRooms(userId: UUID, roomIds: List<UUID>) {
        val subscribedRooms = sessions.computeIfAbsent(userId) { ConcurrentHashMap.newKeySet() }

        for (roomId in roomIds) {
            if (!subscribedRooms.add(roomId)) continue

            subscribe(roomId, userId)
        }
    }

    fun subscribeToRoom(userId: UUID, roomId: UUID) {
        val subscribedRooms = sessions[userId] ?: return

        if (subscribedRooms.add(roomId)) {
            subscribe(roomId, userId)
        }
    }

    private fun subscribe(roomId: UUID, userId: UUID) {
        subscribers.compute(roomId) { _, count ->
            if (count == null) {
                listeners
                    .computeIfAbsent(roomId) {
                        listOf(
                            channelSendListener to ChannelTopic(ChatEventTopic.CHAT_MESSAGE_SEND.withChannel(roomId.toString())),
                            channelReadListener to ChannelTopic(ChatEventTopic.CHAT_MESSAGE_READ.withChannel(roomId.toString()))
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
    fun unsubscribeToUser(userId: UUID) {
        val subscribedRooms = sessions.remove(userId) ?: return

        for (roomId in subscribedRooms) {
            unsubscribe(roomId, userId)
        }
    }

    fun unsubscribeToRoom(roomId: UUID) {
        sessions.forEach { (userId, rooms) ->
            if (rooms.remove(roomId)) {
                unsubscribe(roomId, userId)
            }
        }
    }

    fun unsubscribeToUserRoom(userId: UUID, roomId: UUID) {
        val subscribedRooms = sessions[userId] ?: return

        if (subscribedRooms.remove(roomId)) {
            unsubscribe(roomId, userId)
        }
    }

    private fun unsubscribe(roomId: UUID, userId: UUID) {
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
