DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    id         BINARY(16) PRIMARY KEY,
    email      VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    image      TEXT         NULL,
    nickname   VARCHAR(255) NOT NULL,
    role       VARCHAR(255) NOT NULL,
    created_at TIMESTAMP    NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    updated_at TIMESTAMP    NULL,
    updated_by VARCHAR(255) NULL,
    is_deleted TINYINT(1)   NOT NULL
);

DROP TABLE IF EXISTS chat_room;
CREATE TABLE chat_room
(
    id         BINARY(16) PRIMARY KEY,
    owner_id   BINARY(16)   NOT NULL,
    image      TEXT         NULL,
    name       VARCHAR(255) NOT NULL,
    created_at TIMESTAMP    NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    updated_at TIMESTAMP    NULL,
    updated_by VARCHAR(255) NULL,
    is_deleted TINYINT(1)   NOT NULL,
    CONSTRAINT chat_room_user_id_fk
        FOREIGN KEY (owner_id) REFERENCES `user` (id)
            ON UPDATE CASCADE ON DELETE CASCADE
);

DROP TABLE IF EXISTS chat_message;
CREATE TABLE chat_message
(
    id         BINARY(16) PRIMARY KEY,
    room_id    BINARY(16)   NOT NULL,
    sender_id  BINARY(16)   NULL,
    type       VARCHAR(255) NOT NULL,
    content    TEXT         NOT NULL,
    created_at TIMESTAMP    NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    updated_at TIMESTAMP    NULL,
    updated_by VARCHAR(255) NULL,
    is_deleted TINYINT(1)   NOT NULL,
    CONSTRAINT chat_message_chat_room_id_fk
        FOREIGN KEY (room_id) REFERENCES chat_room (id)
            ON DELETE CASCADE,
    CONSTRAINT chat_message_user_id_fk
        FOREIGN KEY (sender_id) REFERENCES `user` (id)
            ON DELETE CASCADE
);

CREATE INDEX chat_message_room_id_idx ON chat_message (room_id);
CREATE INDEX chat_message_user_id_idx ON chat_message (sender_id);

DROP TABLE IF EXISTS chat;
CREATE TABLE chat
(
    user_id         BINARY(16)   NOT NULL,
    room_id         BINARY(16)   NOT NULL,
    last_message_id BINARY(16)   NULL,
    last_read_at    TIMESTAMP    NULL,
    joined_at       TIMESTAMP    NOT NULL,
    created_at      TIMESTAMP    NOT NULL,
    created_by      VARCHAR(255) NOT NULL,
    updated_at      TIMESTAMP    NULL,
    updated_by      VARCHAR(255) NULL,
    is_deleted      TINYINT(1)   NOT NULL,
    PRIMARY KEY (user_id, room_id),
    CONSTRAINT chat_user_user_id_fk
        FOREIGN KEY (user_id) REFERENCES `user` (id)
            ON DELETE CASCADE,
    CONSTRAINT chat_chat_room_id_fk
        FOREIGN KEY (room_id) REFERENCES chat_room (id)
            ON DELETE CASCADE
);

CREATE INDEX chat_room_id_idx ON chat (room_id);
