-- auto-generated definition
create table t_user_profile
(
    user_profile_id serial                        not null
        constraint t_user_profile_pk
            primary key,
    login           varchar(256)                  not null,
    password        varchar(93)                   not null,
    email           varchar(256),
    first_name      varchar(256),
    last_name       varchar(256),
    confirmed       boolean         default false not null,
    biography       varchar(2048),
    sex             integer                       not null,
    sex_preferences integer,
    confirmed_token varchar(40),
    latitude      numeric(25, 15) default 0.0,
    longitude      numeric(25, 15) default 0.0,
    rating          integer         default 0
);

alter table t_user_profile
    owner to sammy;

create unique index t_user_profile_user_profile_id_uindex
    on t_user_profile (user_profile_id);

create unique index t_user_profile_verification_token_uindex
    on t_user_profile (confirmed_token);

