-- auto-generated definition
create table t_message
(
    id_message serial not null
        constraint t_message_pk
            primary key,
    text       varchar(2048),
    date       bigint,
    "from"     integer
        constraint t_message_t_user_profile_user_profile_id_fk
            references t_user_profile,
    "to"       integer
        constraint t_message_t_user_profile_user_profile_id_fk_2
            references t_user_profile
);

alter table t_message
    owner to sammy;

create unique index t_message_id_message_uindex
    on t_message (id_message);

