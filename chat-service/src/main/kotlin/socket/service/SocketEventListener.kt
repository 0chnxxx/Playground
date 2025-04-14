package com.playground.chat.socket.service

import com.playground.chat.chat.repository.ChatRepository
import com.playground.chat.chat.service.ChatService
import com.playground.chat.global.auth.UserPrincipal
import com.playground.chat.global.util.logger
import com.playground.chat.user.repository.UserRepository
import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.SessionConnectEvent
import org.springframework.web.socket.messaging.SessionConnectedEvent
import org.springframework.web.socket.messaging.SessionDisconnectEvent

@Component
class SocketEventListener(
    private val socketSubscriber: SocketSubscriber,
    private val chatRepository: ChatRepository
) {
    private val log = logger()

    @EventListener
    fun handleChatConnect(event: SessionConnectEvent) {
        val accessor = StompHeaderAccessor.wrap(event.message)
        val sessionId = accessor.sessionId!!
        val userId = accessor.user!!.name.toLong()

        log.info("[🌐 Chat Connect Attempt] sessionId: {}, userId: {}", sessionId, userId)
    }

    /**
     * [세션 유저 검증 & 채널 구독 핸들러]
     * - Handshake 핸들러와 인터셉터에 의해 가져온 JWT를 통해 유저를 DB에서 조회
     * - 임시 Principal 을 UserPrincipal 객체로 변경
     * - 해당 유저가 참여하고 있는 채팅방 채널을 동적으로 구독
     */
    @EventListener
    fun handleChatConnected(event: SessionConnectedEvent) {
        val accessor = StompHeaderAccessor.wrap(event.message)
        val sessionId = accessor.sessionId!!
        val userId = accessor.user!!.name.toLong()

        accessor.user = UserPrincipal(
            id = userId
        )

//        val user = userRepository.findById(userId)
//            .orElseThrow { Exception("User Not Found : $userId") }
//
//        val roomIds = user.rooms
//            .map { room -> room.id.toString() }
//            .toList()

        val rooms = chatRepository.findRoomByUserId(userId)
        val roomIds = rooms.map { it.id.toString() }.toList()

        socketSubscriber.subscribe(sessionId, roomIds)

        log.info("[✅ Chat Connected] sessionId: {}, userId: {}", sessionId, userId)
    }

    /**
     * [채널 구독 해제 핸들러]
     * - 해당 유저가 참여하고 있는 채팅방 채널을 동적으로 구독 해제
     */
    @EventListener
    fun handleChatDisconnect(event: SessionDisconnectEvent) {
        val accessor = StompHeaderAccessor.wrap(event.message)
        val sessionId = accessor.sessionId!!
        val userId = accessor.user!!.name.toLong()

        socketSubscriber.unsubscribe(sessionId)

        log.info("[❌ Chat Disconnected] sessionId: {}, userId: {}", sessionId, userId)
    }
}
