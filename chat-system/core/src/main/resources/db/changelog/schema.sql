set global sql_mode = '';
set global time_zone = '+00:00';

set foreign_key_checks = 0;

drop table if exists user;
create table user
(
    id         binary(16) primary key,
    email      varchar(255)                        not null,
    password   varchar(255)                        not null,
    nickname   varchar(255)                        not null,
    created_at timestamp default CURRENT_TIMESTAMP not null
);

drop table if exists chat_room;
create table chat_room
(
    id         binary(16) primary key,
    owner_id   binary(16)                          not null,
    name       varchar(255)                        not null,
    created_at timestamp default CURRENT_TIMESTAMP not null,
    constraint chat_room_user_id_fk
        foreign key (owner_id) references user (id)
            on update cascade on delete cascade
);

drop table if exists chat_message;
create table chat_message
(
    id         binary(16) primary key,
    room_id    binary(16)                          not null,
    sender_id  binary(16)                          not null,
    content    text                                not null,
    created_at timestamp default CURRENT_TIMESTAMP not null,
    constraint chat_message_chat_room_id_fk
        foreign key (room_id) references chat_room (id)
            on delete cascade,
    constraint chat_message_user_id_fk
        foreign key (sender_id) references user (id)
            on delete cascade
);

create index chat_message_room_id_idx
    on chat_message (room_id);

create index chat_message_user_id_idx
    on chat_message (sender_id);

drop table if exists chat;
create table chat
(
    user_id         binary(16)                          not null,
    room_id         binary(16)                          not null,
    last_message_id binary(16)                          null,
    last_read_at    timestamp                           null,
    joined_at       timestamp default CURRENT_TIMESTAMP not null,
    primary key (user_id, room_id),
    constraint chat_chat_message_id_fk
        foreign key (last_message_id) references chat_message (id)
            on update cascade on delete cascade,
    constraint chat_user_user_id_fk
        foreign key (user_id) references user (id)
            on delete cascade,
    constraint chat_chat_room_id_fk
        foreign key (room_id) references chat_room (id)
            on delete cascade
);

create index chat_room_id_idx
    on chat (room_id);

set foreign_key_checks = 1;
