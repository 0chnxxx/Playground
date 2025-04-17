package com.playground.chat.chat.repository

import com.playground.chat.chat.data.request.FindChatMessagesRequest
import com.playground.chat.chat.data.request.FindChatRoomsRequest
import com.playground.chat.chat.data.response.ChatMessageDto
import com.playground.chat.chat.data.response.ChatRoomDto
import com.playground.chat.chat.entity.*
import com.playground.chat.global.data.Pagination
import com.playground.chat.user.entity.QUserEntity
import com.playground.chat.user.entity.UserEntity
import com.querydsl.core.types.Projections
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository

@Repository
class ChatRepository(
    private val entityManager: EntityManager,
    private val jpaQueryFactory: JPAQueryFactory
) {
    fun findChatRooms(userId: Long, request: FindChatRoomsRequest): Pagination<List<ChatRoomDto>> {
        val qChat = QChatEntity("chat")
        val qRoom = QChatRoomEntity("room")
        val qMessage = QChatMessageEntity("message")
        val qLastMessage = QChatMessageEntity("lastMessage")

        val lastMessage = JPAExpressions
            .select(qMessage.id.max())
            .from(qMessage)
            .where(qMessage.room.eq(qRoom))

        val totalCount = jpaQueryFactory
            .select(qRoom.id.count())
            .from(qRoom)
            .leftJoin(qLastMessage).on(qLastMessage.id.eq(lastMessage))
            .fetchOne() ?: 0

        val rooms = jpaQueryFactory
            .select(
                Projections.constructor(
                    ChatRoomDto::class.java,
                    qRoom.id,
                    qRoom.name,
                    qLastMessage.content,
                    qLastMessage.createdAt,
                    JPAExpressions
                        .select(qChat.id.count())
                        .from(qChat)
                        .where(qChat.room.id.eq(qRoom.id)),
                    JPAExpressions
                        .select(qChat.id.isNotNull)
                        .from(qChat)
                        .where(qChat.room.id.eq(qRoom.id), qChat.user.id.eq(userId))
                )
            )
            .from(qRoom)
            .leftJoin(qLastMessage).on(qLastMessage.id.eq(lastMessage))
            .offset((request.page - 1) * request.size.toLong())
            .limit(request.size.toLong())
            .fetch()

        return Pagination.of(
            totalCount = totalCount,
            page = request.page,
            size = request.size,
            data = rooms
        )
    }

    fun findChatRoomsByUserId(userId: Long): List<ChatRoomDto> {
        val qChat = QChatEntity("chat")
        val qRoom = QChatRoomEntity("room")
        val qMessage = QChatMessageEntity("message")
        val qLastMessage = QChatMessageEntity("lastMessage")

        val lastMessage = JPAExpressions
            .select(qMessage.id.max())
            .from(qMessage)
            .where(qMessage.room.eq(qRoom))

        return jpaQueryFactory
            .select(
                Projections.constructor(
                    ChatRoomDto::class.java,
                    qRoom.id,
                    qRoom.name,
                    qLastMessage.content,
                    qLastMessage.createdAt,
                    JPAExpressions
                        .select(qChat.id.count())
                        .from(qChat)
                        .where(qChat.room.id.eq(qRoom.id)),
                    JPAExpressions
                        .select(qChat.id.isNotNull)
                        .from(qChat)
                        .where(qChat.room.id.eq(qRoom.id), qChat.user.id.eq(userId))
                )
            )
            .from(qRoom)
            .join(qChat).on(qChat.room.id.eq(qRoom.id))
            .leftJoin(qLastMessage).on(qLastMessage.id.eq(lastMessage))
            .where(qChat.user.id.eq(userId))
            .fetch()
    }

    fun findChatRoomByRoomId(roomId: Long): ChatRoomEntity? {
        val qRoom = QChatRoomEntity("room")

        return jpaQueryFactory
            .selectFrom(qRoom)
            .where(qRoom.id.eq(roomId))
            .fetchOne()
    }

    fun findChatMessagesByRoomId(
        userId: Long,
        roomId: Long,
        request: FindChatMessagesRequest
    ): Pagination<List<ChatMessageDto>> {
        val qUser = QUserEntity("user")
        val qRoom = QChatRoomEntity("room")
        val qMessage = QChatMessageEntity("message")

        val totalCount = jpaQueryFactory
            .select(qMessage.id.count())
            .from(qMessage)
            .join(qMessage.room, qRoom)
            .join(qMessage.sender, qUser)
            .where(qMessage.room.id.eq(roomId))
            .fetchOne() ?: 0

        val rooms = jpaQueryFactory
            .select(
                Projections.constructor(
                    ChatMessageDto::class.java,
                    qRoom.id,
                    qUser.id,
                    qUser.nickname,
                    qMessage.content,
                    qUser.id.eq(userId),
                    qMessage.createdAt
                )
            )
            .from(qMessage)
            .join(qMessage.room, qRoom)
            .join(qMessage.sender, qUser)
            .where(qMessage.room.id.eq(roomId))
            .orderBy(qMessage.createdAt.desc())
            .offset((request.page - 1) * request.size.toLong())
            .limit(request.size.toLong())
            .fetch()

        return Pagination.of(
            totalCount = totalCount,
            page = request.page,
            size = request.size,
            data = rooms
        )
    }

    fun saveChat(chat: ChatEntity) {
        entityManager.persist(chat)
    }

    fun saveChatRoom(room: ChatRoomEntity) {
        entityManager.persist(room)
    }

    fun saveChatMessage(message: ChatMessageEntity) {
        entityManager.persist(message)
    }

    fun deleteChatsByRoom(room: ChatRoomEntity) {
        val qChat = QChatEntity("chat")

        jpaQueryFactory
            .delete(qChat)
            .where(qChat.room.id.eq(room.id))
    }

    fun deleteChatByUserAndRoom(user: UserEntity, room: ChatRoomEntity) {
        val qChat = QChatEntity("chat")

        jpaQueryFactory
            .delete(qChat)
            .where(qChat.room.id.eq(room.id).and(qChat.user.id.eq(user.id)))
    }

    fun deleteChatRoom(room: ChatRoomEntity) {
        val qRoom = QChatRoomEntity("room")

        jpaQueryFactory
            .delete(qRoom)
            .where(qRoom.id.eq(room.id))
    }
}
