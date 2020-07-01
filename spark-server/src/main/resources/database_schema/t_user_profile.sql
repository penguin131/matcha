create table "spark-db".t_user_profile
(
	user_profile_id serial not null,
	login varchar(256) not null,
	password varchar(60) not null,
	email varchar(256),
	first_name varchar(256),
	last_name varchar(256),
	confirmed bool default false not null,
	biography varchar(2048),
	sex int not null,
	sex_preferences int
);

create unique index t_user_profile_user_profile_id_uindex
	on "spark-db".t_user_profile (user_profile_id);

alter table "spark-db".t_user_profile
	add constraint t_user_profile_pk
		primary key (user_profile_id);
