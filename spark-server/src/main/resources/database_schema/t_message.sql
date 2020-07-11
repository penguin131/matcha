create table "spark-db".t_message
(
    id_message serial  not null
        constraint t_message_pk
            primary key,
    text       varchar(2048),
    user_unity integer not null
        constraint t_message_t_users_unity_t_users_unity_id_fk
            references "spark-db".t_users_unity,
    date       date
);

alter table "spark-db".t_message
    owner to sammy;

create unique index t_message_id_message_uindex
    on "spark-db".t_message (id_message);

