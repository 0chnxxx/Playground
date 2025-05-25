package com.playground.chat.chat.repository

import com.playground.chat.chat.data.request.FindChatMessagesRequest
import com.playground.chat.chat.data.request.FindChatRoomsRequest
import com.playground.chat.chat.data.response.ChatMessageDto
import com.playground.chat.chat.data.response.ChatRoomDto
import com.playground.chat.chat.data.response.ChatUserDto
import com.playground.chat.chat.data.response.MyChatRoomDto
import com.playground.chat.chat.entity.*
import com.playground.chat.global.data.Pagination
import com.playground.chat.user.entity.QUserEntity
import com.playground.chat.user.entity.UserEntity
import com.querydsl.core.types.ExpressionUtils
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.CaseBuilder
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.Collections

@Repository
class ChatRepository(
    private val entityManager: EntityManager,
    private val jpaQueryFactory: JPAQueryFactory
) {
    fun findChatRooms(userId: Long, request: FindChatRoomsRequest): Pagination<List<ChatRoomDto>> {
        val qRoom = QChatRoomEntity("room")
        val qMe = QChatEntity("me")
        val qMember = QChatEntity("member")
        val qLastMessage = QChatMessageEntity("lastMessage")

        val memberCount = JPAExpressions
            .select(qMember.count())
            .from(qMember)
            .where(qMember.room.id.eq(qRoom.id))

        val lastMessage = JPAExpressions
            .select(qLastMessage.id.max())
            .from(qLastMessage)
            .where(qLastMessage.room.id.eq(qRoom.id))

        val totalRoomCount = jpaQueryFactory
            .select(qRoom.id.countDistinct())
            .from(qRoom)
            .leftJoin(qMe).on(qMe.room.id.eq(qRoom.id), qMe.user.id.eq(userId))
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
                    memberCount,
                    qMe.isNotNull
                )
            )
            .from(qRoom)
            .leftJoin(qMe).on(qMe.room.id.eq(qRoom.id), qMe.user.id.eq(userId))
            .leftJoin(qLastMessage).on(qLastMessage.id.eq(lastMessage))
            .offset((request.page - 1) * request.size.toLong())
            .limit(request.size.toLong())
            .groupBy(qRoom.id)
            .fetch()

        return Pagination.of(
            totalCount = totalRoomCount,
            page = request.page,
            size = request.size,
            data = rooms
        )
    }

    fun findChatRoomsByUserId(userId: Long): List<MyChatRoomDto> {
        val qRoom = QChatRoomEntity("room")
        val qChat = QChatEntity("chat")
        val qMember = QChatEntity("member")
        val qLastMessage = QChatMessageEntity("lastMessage")
        val qUnreadMessage = QChatMessageEntity("unreadMessage")

        val memberCount = JPAExpressions
            .select(qMember.count())
            .from(qMember)
            .where(qMember.room.id.eq(qRoom.id))

        val lastMessage = JPAExpressions
            .select(qLastMessage.id.max())
            .from(qLastMessage)
            .where(qLastMessage.room.eq(qRoom))

        val unreadCount = JPAExpressions
            .select(qUnreadMessage.id.count())
            .from(qUnreadMessage)
            .where(qUnreadMessage.room.id.eq(qRoom.id), qUnreadMessage.createdAt.gt(qChat.lastReadAt))

        return jpaQueryFactory
            .select(
                Projections.constructor(
                    MyChatRoomDto::class.java,
                    qRoom.id,
                    qRoom.name,
                    qLastMessage.content,
                    unreadCount,
                    qLastMessage.createdAt,
                    memberCount
                )
            )
            .from(qRoom)
            .leftJoin(qRoom.chats, qChat)
            .leftJoin(qLastMessage).on(qLastMessage.id.eq(lastMessage))
            .where(qChat.user.id.eq(userId))
            .orderBy(qLastMessage.createdAt.desc())
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
        val qUnreadChat = QChatEntity("unreadChat")
        val qRoom = QChatRoomEntity("room")
        val qMessage = QChatMessageEntity("message")

        val totalCount = jpaQueryFactory
            .select(qMessage.id.count())
            .from(qMessage)
            .join(qMessage.room, qRoom)
            .join(qMessage.sender, qUser)
            .where(qMessage.room.id.eq(roomId))
            .fetchOne() ?: 0

        val messages = jpaQueryFactory
            .select(
                Projections.constructor(
                    ChatMessageDto::class.java,
                    qMessage.id,
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

        val messageIds = messages.map { it.messageId }.toList();

        val unreadUsers = if (messageIds.isNotEmpty()) {
            jpaQueryFactory
                .select(
                    qMessage.id,
                    qUnreadChat.user.id
                )
                .from(qMessage)
                .join(qUnreadChat).on(qUnreadChat.room.id.eq(roomId), qUnreadChat.lastReadAt.coalesce(qUnreadChat.joinedAt).lt(qMessage.createdAt))
                .join(qUnreadChat.user, qUser)
                .where(qMessage.id.`in`(messageIds))
                .fetch()
                .groupBy({ it.get(qMessage.id!!) }, { it.get(qUnreadChat.user.id)!! })
        } else {
            emptyMap()
        }

        val finalMessages = messages.map { message ->
            message.copy(unreadUserIds = unreadUsers[message.messageId] ?: emptyList())
        }

        return Pagination.of(
            totalCount = totalCount,
            page = request.page,
            size = request.size,
            data = finalMessages
        )
    }

    fun findLastChatMessageByRoomIdAndUserId(roomId: Long, userId: Long): ChatMessageEntity? {
        val qChat = QChatEntity("chat")
        val qMessage = QChatMessageEntity("message")

        return jpaQueryFactory
            .select(qChat.lastMessage)
            .from(qChat)
            .join(qChat.lastMessage, qMessage)
            .where(qChat.room.id.eq(roomId), qChat.user.id.eq(userId))
            .fetchOne()
    }

    fun findChatUsersByRoomId(userId: Long, roomId: Long): List<ChatUserDto> {
        val qUser = QUserEntity("user")
        val qChat = QChatEntity("chat")
        val qRoom = QChatRoomEntity("room")

        return jpaQueryFactory
            .select(
                Projections.constructor(
                    ChatUserDto::class.java,
                    qUser.id,
                    qUser.nickname,
                    qRoom.owner.id.eq(qUser.id)
                )
            )
            .from(qChat)
            .join(qChat.user, qUser)
            .join(qChat.room, qRoom)
            .where(qRoom.id.eq(roomId))
            .orderBy(
                CaseBuilder()
                    .`when`(qRoom.owner.id.eq(qUser.id))
                    .then(1)
                    .otherwise(0)
                    .desc()
            )
            .fetch()
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

    fun updateChatForMessage(roomId: Long, userId: Long, messageId: Long) {
        val qChat = QChatEntity("chat")

        jpaQueryFactory
            .update(qChat)
            .set(qChat.lastMessage.id, messageId)
            .set(qChat.lastReadAt, LocalDateTime.now())
            .where(
                qChat.room.id.eq(roomId),
                qChat.user.id.eq(userId)
            )
            .execute()
    }

    fun updateChatForLastMessage(roomId: Long, userId: Long) {
        val qChat = QChatEntity("chat")
        val qLastMessage = QChatMessageEntity("lastMessage")

        val lastMessage = JPAExpressions
            .select(qLastMessage.id.max())
            .from(qLastMessage)
            .where(qLastMessage.room.id.eq(roomId))

        jpaQueryFactory
            .update(qChat)
            .set(qChat.lastMessage.id, lastMessage)
            .set(qChat.lastReadAt, LocalDateTime.now())
            .where(
                qChat.room.id.eq(roomId),
                qChat.user.id.eq(userId)
            )
            .execute()
    }

    fun deleteChatRoom(room: ChatRoomEntity) {
        val qRoom = QChatRoomEntity("room")

        jpaQueryFactory
            .delete(qRoom)
            .where(qRoom.id.eq(room.id))
            .execute()
    }

    fun deleteChatsByRoom(room: ChatRoomEntity) {
        val qChat = QChatEntity("chat")

        jpaQueryFactory
            .delete(qChat)
            .where(qChat.room.id.eq(room.id))
            .execute()
    }

    fun deleteChatByUserAndRoom(user: UserEntity, room: ChatRoomEntity) {
        val qChat = QChatEntity("chat")

        jpaQueryFactory
            .delete(qChat)
            .where(qChat.room.id.eq(room.id).and(qChat.user.id.eq(user.id)))
            .execute()
    }
}
