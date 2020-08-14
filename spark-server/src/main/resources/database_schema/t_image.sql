-- auto-generated definition
create table t_image
(
    id_image serial                not null
        constraint t_image_pk
            primary key,
    user_id  integer               not null
        constraint t_image_t_user_profile_user_profile_id_fk
            references t_user_profile,
    is_main  boolean default false not null
);

alter table t_image
    owner to sammy;

create unique index t_image_id_image_uindex
    on t_image (id_image);

