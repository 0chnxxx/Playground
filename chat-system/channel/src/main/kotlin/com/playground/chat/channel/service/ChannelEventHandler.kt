package com.playground.chat.channel.service

import com.playground.chat.global.log.logger
import com.playground.chat.global.auth.UserPrincipal
import com.playground.chat.channel.client.ChatApiClient
import com.playground.chat.chat.data.event.ChatRoomEvent
import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.SessionConnectEvent
import org.springframework.web.socket.messaging.SessionConnectedEvent
import org.springframework.web.socket.messaging.SessionDisconnectEvent

@Component
class ChannelEventHandler(
    private val chatApiClient: ChatApiClient,
    private val channelSubscriber: ChannelSubscriber
) {
    private val log = logger()

    /**
     * [채널 구독 핸들러]
     * - Handshake 핸들러와 인터셉터에 의해 가져온 Principal 의 passport(JWT)를 통해 API 서버와 통신
     * - 해당 유저가 참여하고 있는 채팅방 채널을 동적으로 구독
     */
    @EventListener
    fun handleChatConnect(event: SessionConnectEvent) {
        val accessor = StompHeaderAccessor.wrap(event.message)
        val principal = accessor.user!! as UserPrincipal
        val sessionId = accessor.sessionId!!
        val userId = principal.id

        log.info("[🌐 Chat Connect Attempt] sessionId: {}, userId: {}", sessionId, userId)

        val response = chatApiClient.findMyChatRooms(principal.getBearerPassport())
        val rooms = response.data
        val roomIds = rooms.map { it.id }.toList()

        channelSubscriber.subscribeToRooms(userId, roomIds)
    }

    @EventListener
    fun handleChatConnected(event: SessionConnectedEvent) {
        val accessor = StompHeaderAccessor.wrap(event.message)
        val principal = accessor.user!! as UserPrincipal
        val sessionId = accessor.sessionId!!
        val userId = principal.id

        log.info("[✅ Chat Connected] sessionId: {}, userId: {}", sessionId, userId)
    }

    /**
     * [채널 구독 해제 핸들러]
     * - 해당 유저가 참여하고 있는 채팅방 채널을 동적으로 구독 해제
     */
    @EventListener
    fun handleChatDisconnect(event: SessionDisconnectEvent) {
        val accessor = StompHeaderAccessor.wrap(event.message)
        val principal = accessor.user!! as UserPrincipal
        val sessionId = accessor.sessionId!!
        val userId = principal.id

        channelSubscriber.unsubscribeToUser(userId)

        log.info("[❌ Chat Disconnected] sessionId: {}, userId: {}", sessionId, userId)
    }

    @EventListener
    fun handleChatRoomEvent(event: ChatRoomEvent) {
        when (event.type) {
            ChatRoomEvent.Type.CREATE -> channelSubscriber.subscribeToRoom(event.userId, event.roomId)
            ChatRoomEvent.Type.JOIN -> channelSubscriber.subscribeToRoom(event.userId, event.roomId)
            ChatRoomEvent.Type.LEAVE -> channelSubscriber.unsubscribeToUserRoom(event.userId, event.roomId)
            ChatRoomEvent.Type.DELETE -> channelSubscriber.unsubscribeToRoom(event.roomId)
        }
    }
}
