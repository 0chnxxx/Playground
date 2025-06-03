set global sql_mode = '';

set foreign_key_checks = 0;

drop table if exists user;
create table user
(
    id         bigint auto_increment primary key,
    email      varchar(255)                        not null,
    password   varchar(255)                        not null,
    nickname   varchar(255)                        not null,
    created_at timestamp default CURRENT_TIMESTAMP not null
);

drop table if exists chat_room;
create table chat_room
(
    id         bigint auto_increment primary key,
    owner_id   bigint                              not null,
    name       varchar(255)                        not null,
    created_at timestamp default CURRENT_TIMESTAMP not null,
    constraint chat_room_user_id_fk
        foreign key (owner_id) references user (id)
            on update cascade on delete cascade
);

drop table if exists chat_message;
create table chat_message
(
    id         bigint auto_increment primary key,
    room_id    bigint                              not null,
    sender_id  bigint                              not null,
    content    text                                not null,
    created_at timestamp default CURRENT_TIMESTAMP not null,
    constraint chat_message_ibfk_1
        foreign key (room_id) references chat_room (id)
            on delete cascade,
    constraint chat_message_ibfk_2
        foreign key (sender_id) references user (id)
            on delete cascade
);

create index room_id
    on chat_message (room_id);

create index user_id
    on chat_message (sender_id);

drop table if exists chat;
create table chat
(
    user_id         bigint                              not null,
    room_id         bigint                              not null,
    last_message_id bigint                              null,
    last_read_at    timestamp                           null,
    joined_at       timestamp default CURRENT_TIMESTAMP not null,
    primary key (user_id, room_id),
    constraint chat_chat_message_id_fk
        foreign key (last_message_id) references chat_message (id)
            on update cascade on delete cascade,
    constraint chat_ibfk_1
        foreign key (user_id) references user (id)
            on delete cascade,
    constraint chat_ibfk_2
        foreign key (room_id) references chat_room (id)
            on delete cascade
);

create index room_id
    on chat (room_id);

set foreign_key_checks = 1;
