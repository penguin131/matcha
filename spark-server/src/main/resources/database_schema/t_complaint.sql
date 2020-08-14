-- auto-generated definition
create table t_complaint
(
    id_complaint serial  not null
        constraint t_complaint_pk
            primary key,
    from_user    integer not null
        constraint t_complaint_t_user_profile_user_profile_id_fk
            references t_user_profile,
    to_user      integer not null
        constraint t_complaint_t_user_profile_user_profile_id_fk_2
            references t_user_profile
);

alter table t_complaint
    owner to sammy;

create unique index t_complaint_id_complaint_uindex
    on t_complaint (id_complaint);

