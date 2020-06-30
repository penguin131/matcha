

create table if not exists my_db.t_user_profile
(
    user_profile_id int auto_increment
        primary key,
    sex             tinyint(1)           not null,
    sex_preferences int                  null,
    biography       varchar(2048)        null,
    password        varchar(60)          not null,
    login           varchar(255)         not null,
    email           varchar(256)         null,
    confirmed       tinyint(1) default 0 not null,
    first_name      varchar(256)         null,
    last_name       varchar(256)         null
);

create table if not exists my_db.t_relations
(
    relation_id int auto_increment
        primary key,
    user_1      int                  not null,
    user_2      int                  not null,
    is_friend   tinyint(1) default 0 null,
    constraint t_friends_t_user_profile__fk1
        foreign key (user_1) references my_db.t_user_profile (user_profile_id),
    constraint t_friends_t_user_profile__fk2
        foreign key (user_2) references my_db.t_user_profile (user_profile_id)
);
