create table if not exists my_db.t_user_profile
(
    user_profile_id int auto_increment
        primary key,
    sex             tinyint(1)    not null,
    sex_preferences int           null,
    biography       varchar(2048) null,
    password        varchar(60)   not null,
    login           varchar(255)  not null,
    email           varchar(256)  null,
    confirmed       int           not null,
    first_name      varchar(256)  null,
    last_name       varchar(256)  null
);
