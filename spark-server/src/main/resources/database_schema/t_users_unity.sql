-- auto-generated definition
create table t_users_unity
(
    t_users_unity_id serial not null
        constraint t_users_unity_pk
            primary key,
    user1_id         integer
        constraint t_users_unity_t_user_profile__fk1
            references t_user_profile,
    user2_id         integer
        constraint t_users_unity_t_user_profile__fk2
            references t_user_profile,
    confirmed        boolean default false
);

alter table t_users_unity
    owner to sammy;

create unique index t_users_unity_t_users_unity_id_uindex
    on t_users_unity (t_users_unity_id);

