package com.playground.chat.chat.repository

import com.playground.chat.chat.data.request.FindChatMessagesRequest
import com.playground.chat.chat.data.request.FindChatRoomsRequest
import com.playground.chat.chat.data.response.ChatMessageDto
import com.playground.chat.chat.data.response.ChatRoomDto
import com.playground.chat.chat.data.response.ChatUserDto
import com.playground.chat.chat.data.response.MyChatRoomDto
import com.playground.chat.chat.entity.*
import com.playground.chat.global.data.Page
import com.playground.chat.user.entity.QUserEntity
import com.playground.chat.user.entity.UserEntity
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.CaseBuilder
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.*

@Repository
class ChatRepository(
    private val entityManager: EntityManager,
    private val jpaQueryFactory: JPAQueryFactory
) {
    fun findChat(roomId: UUID, userId: UUID): ChatEntity? {
        val qChat = QChatEntity("chat")

        return jpaQueryFactory
            .selectFrom(qChat)
            .where(
                qChat.room.id.eq(roomId),
                qChat.user.id.eq(userId)
            )
            .fetchOne()
    }

    fun findChatRoomByRoomId(roomId: UUID): ChatRoomEntity? {
        val qRoom = QChatRoomEntity("room")

        return jpaQueryFactory
            .selectFrom(qRoom)
            .where(qRoom.id.eq(roomId))
            .fetchOne()
    }

    fun findChatRooms(userId: UUID, request: FindChatRoomsRequest): Page<List<ChatRoomDto>> {
        val qRoom = QChatRoomEntity("room")
        val qChat = QChatEntity("chat")
        val qMyChat = QChatEntity("myChat")
        val qFirstChat = QChatEntity("firstChat")
        val qMessage = QChatMessageEntity("message")
        val qLastMessage = QChatMessageEntity("lastMessage")

        val joinedAt = JPAExpressions
            .select(qFirstChat.joinedAt)
            .from(qFirstChat)
            .where(
                qFirstChat.room.id.eq(qRoom.id),
                qFirstChat.user.id.eq(userId)
            )

        val lastMessage = JPAExpressions
            .select(qLastMessage.id.max())
            .from(qLastMessage)
            .where(
                qLastMessage.room.id.eq(qRoom.id),
                qLastMessage.createdAt.goe(joinedAt)
            )

        val memberCount = JPAExpressions
            .select(qChat.count())
            .from(qChat)
            .where(qChat.room.id.eq(qRoom.id))

        val totalRoomCount = jpaQueryFactory
            .select(qRoom.id.countDistinct())
            .from(qRoom)
            .leftJoin(qRoom.chats, qMyChat)
            .on(
                qMyChat.room.id.eq(qRoom.id),
                qMyChat.user.id.eq(userId)
            )
            .leftJoin(qRoom.messages, qLastMessage)
            .on(qLastMessage.id.eq(lastMessage))
            .fetchOne() ?: 0

        val rooms = jpaQueryFactory
            .select(
                Projections.constructor(
                    ChatRoomDto::class.java,
                    qRoom.id,
                    qRoom.image,
                    qRoom.name,
                    qMessage.content,
                    qMessage.createdAt,
                    memberCount,
                    qMyChat.isNotNull
                )
            )
            .from(qRoom)
            .leftJoin(qRoom.chats, qMyChat)
            .on(
                qMyChat.room.id.eq(qRoom.id),
                qMyChat.user.id.eq(userId)
            )
            .leftJoin(qRoom.messages, qMessage)
            .on(qMessage.id.eq(lastMessage))
            .offset((request.page - 1) * request.size.toLong())
            .limit(request.size.toLong())
            .groupBy(qRoom.id)
            .orderBy(qMessage.createdAt.desc(), qMessage.id.desc())
            .fetch()

        return Page.of(
            totalCount = totalRoomCount,
            page = request.page,
            size = request.size,
            content = rooms
        )
    }

    fun findChatRoomsByUserId(userId: UUID): List<MyChatRoomDto> {
        val qRoom = QChatRoomEntity("room")
        val qChat = QChatEntity("chat")
        val qFirstChat = QChatEntity("firstChat")
        val qMessage = QChatMessageEntity("message")
        val qLastMessage = QChatMessageEntity("lastMessage")
        val qUnreadMessage = QChatMessageEntity("unreadMessage")

        val joinedAt = JPAExpressions
            .select(qFirstChat.joinedAt)
            .from(qFirstChat)
            .where(
                qFirstChat.room.id.eq(qRoom.id),
                qFirstChat.user.id.eq(userId)
            )

        val lastMessage = JPAExpressions
            .select(qLastMessage.id.max())
            .from(qLastMessage)
            .where(
                qLastMessage.room.id.eq(qRoom.id),
                qLastMessage.createdAt.goe(joinedAt)
            )

        val memberCount = JPAExpressions
            .select(qChat.count())
            .from(qChat)
            .where(qChat.room.id.eq(qRoom.id))

        val unreadCount = JPAExpressions
            .select(qUnreadMessage.id.count())
            .from(qUnreadMessage)
            .where(
                qUnreadMessage.room.id.eq(qRoom.id),
                qUnreadMessage.createdAt.gt(qChat.lastReadAt),
                qUnreadMessage.sender.id.ne(userId)
            )

        return jpaQueryFactory
            .select(
                Projections.constructor(
                    MyChatRoomDto::class.java,
                    qRoom.id,
                    qRoom.image,
                    qRoom.name,
                    qMessage.content,
                    qMessage.createdAt,
                    memberCount,
                    unreadCount
                )
            )
            .from(qRoom)
            .leftJoin(qRoom.chats, qChat)
            .leftJoin(qRoom.messages, qMessage)
            .on(qMessage.id.eq(lastMessage))
            .where(qChat.user.id.eq(userId))
            .groupBy(qRoom.id)
            .orderBy(qMessage.createdAt.desc(), qMessage.id.desc())
            .fetch()
    }

    fun findChatRoomOwner(roomId: UUID): UserEntity? {
        val qRoom = QChatRoomEntity("room")

        return jpaQueryFactory
            .select(qRoom.owner)
            .from(qRoom)
            .where(qRoom.id.eq(roomId))
            .fetchOne()
    }

    fun findLastChatMessageByRoomId(roomId: UUID): ChatMessageEntity? {
        val qMessage = QChatMessageEntity("message")

        return jpaQueryFactory
            .selectFrom(qMessage)
            .where(qMessage.room.id.eq(roomId))
            .orderBy(qMessage.createdAt.desc(), qMessage.id.desc())
            .limit(1)
            .fetchOne()
    }

    fun findChatMessagesByRoomId(
        userId: UUID,
        roomId: UUID,
        request: FindChatMessagesRequest
    ): Page<List<ChatMessageDto>> {
        val qUser = QUserEntity("user")
        val qFirstChat = QChatEntity("firstChat")
        val qUnreadChat = QChatEntity("unreadChat")
        val qRoom = QChatRoomEntity("room")
        val qMessage = QChatMessageEntity("message")

        val joinedAt = JPAExpressions
            .select(qFirstChat.joinedAt)
            .from(qFirstChat)
            .where(
                qFirstChat.room.id.eq(qRoom.id),
                qFirstChat.user.id.eq(userId)
            )

        val totalCount = jpaQueryFactory
            .select(qMessage.id.count())
            .from(qMessage)
            .leftJoin(qMessage.room, qRoom)
            .leftJoin(qMessage.sender, qUser)
            .where(qMessage.room.id.eq(roomId))
            .fetchOne() ?: 0

        val messages = jpaQueryFactory
            .select(
                Projections.constructor(
                    ChatMessageDto::class.java,
                    qMessage.id,
                    qMessage.sender.id,
                    qMessage.sender.image,
                    qMessage.sender.nickname,
                    qMessage.type.stringValue(),
                    qMessage.content,
                    qMessage.sender.id.eq(userId),
                    qMessage.createdAt
                )
            )
            .from(qMessage)
            .leftJoin(qMessage.room, qRoom)
            .leftJoin(qMessage.sender, qUser)
            .where(
                qMessage.room.id.eq(roomId),
                qMessage.createdAt.goe(joinedAt)
            )
            .orderBy(qMessage.createdAt.desc(), qMessage.id.desc())
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
                .leftJoin(qUnreadChat)
                .on(
                    qUnreadChat.room.id.eq(roomId),
                    qUnreadChat.lastReadAt.coalesce(qUnreadChat.joinedAt).lt(qMessage.createdAt)
                )
                .leftJoin(qUnreadChat.user, qUser)
                .where(qMessage.id.`in`(messageIds))
                .fetch()
                .groupBy({ it.get(qMessage.id) }, { it.get(qUnreadChat.user.id) })
        } else {
            emptyMap()
        }

        val finalMessages = messages.map { message ->
            val userIds = unreadUsers[message.messageId]?.filter { it != null }?.map { it as UUID }

            message.copy(unreadUserIds = userIds ?: emptyList())
        }

        return Page.of(
            totalCount = totalCount,
            page = request.page,
            size = request.size,
            content = finalMessages
        )
    }

    fun findChatUsersByRoomId(userId: UUID, roomId: UUID): List<ChatUserDto> {
        val qUser = QUserEntity("user")
        val qChat = QChatEntity("chat")
        val qRoom = QChatRoomEntity("room")

        return jpaQueryFactory
            .select(
                Projections.constructor(
                    ChatUserDto::class.java,
                    qUser.id,
                    qUser.image,
                    qUser.nickname,
                    qRoom.owner.id.eq(qUser.id),
                    qUser.id.eq(userId)
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
                    .desc(),
                qUser.nickname.asc()
            )
            .fetch()
    }

    fun saveChat(chat: ChatEntity) {
        entityManager.persist(chat)
    }

    fun updateChatForLastMessage(roomId: UUID, userId: UUID, messageId: UUID) {
        val qChat = QChatEntity("chat")

        jpaQueryFactory
            .update(qChat)
            .set(qChat.lastMessageId, messageId)
            .set(qChat.lastReadAt, Instant.now())
            .where(
                qChat.room.id.eq(roomId),
                qChat.user.id.eq(userId),
            )
            .execute()
    }

    fun saveChatRoom(room: ChatRoomEntity) {
        entityManager.persist(room)
    }

    fun saveChatMessage(message: ChatMessageEntity) {
        entityManager.persist(message)
    }

    fun deleteChatRoom(room: ChatRoomEntity) {
        val qRoom = QChatRoomEntity("room")

        jpaQueryFactory
            .update(qRoom)
            .set(qRoom.isDeleted, true)
            .where(qRoom.id.eq(room.id))
            .execute()
    }

    fun deleteChatsByRoom(room: ChatRoomEntity) {
        val qChat = QChatEntity("chat")

        jpaQueryFactory
            .update(qChat)
            .set(qChat.isDeleted, true)
            .where(qChat.room.id.eq(room.id))
            .execute()
    }

    fun deleteChatByUserAndRoom(user: UserEntity, room: ChatRoomEntity) {
        val qChat = QChatEntity("chat")

        jpaQueryFactory
            .update(qChat)
            .set(qChat.isDeleted, true)
            .where(
                qChat.room.id.eq(room.id),
                qChat.user.id.eq(user.id)
            )
            .execute()
    }
}
